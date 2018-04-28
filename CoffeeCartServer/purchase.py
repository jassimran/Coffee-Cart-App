#!/usr/bin/python
import datetime
from google.appengine.ext import db
from google.appengine.api import memcache
from decimal import *
from pprint import pprint
from item import Item
import pickle
import localdate

purchase_list_key = 'purchase_list'

#############################
#  ItemListProperty DB class
#############################
class ItemListProperty(db.BlobProperty):
    def validate(self, value):
        try:
            result = pickle.dumps(value)
            return value
        except pickle.PicklingError, e:
            return super(ItemListProperty, self).validate(value)

    def get_value_for_datastore(self, model_instance):
        result = super(ItemListProperty, self).get_value_for_datastore(model_instance)
        result = pickle.dumps(result)
        return db.Blob(result)

    def make_value_from_datastore(self, value):
        try:
            value = pickle.loads(str(value))
        except:
            pass
        return super(ItemListProperty, self).make_value_from_datastore(value)


Points_Gold_Discount_Life = 5000
Points_Gold_Discount_30_Days = 500
ItemsEligibleForDiscount = ["Coffee - Refill"]

##########################
#  Purchase DB
##########################
class Purchase(db.Model):
    customer_ID = db.IntegerProperty(required=True)
    date = db.DateProperty(required=True)
    total = db.IntegerProperty(required=True)
    points_earned = db.IntegerProperty(required=True)
    # Use a Pickled array of items, each an array of properties: [ID, type, sale price, points]
    # [[4, "Cookie", 2.25, 2], [1, "Coffee", 1.50, 2]]
    items_ordered = ItemListProperty(required=True)
    discount_type = db.StringProperty(required=True)
    cart_number = db.IntegerProperty(required=True)

    def asDict(self):
        order = []
        for index in range(len(self.items_ordered)):
            order.append(self.items_ordered[index])
            order[index][2] = "%0.2f" % (Decimal(self.items_ordered[index][2]) / 100)

        d = {'customer_ID': self.customer_ID,
             'date': self.date.strftime("%Y-%m-%d"),
             'total': "%0.2f" % (Decimal(self.total) / 100),
             'points_earned': self.points_earned,
             'items_ordered': order,
             'discount_type': self.discount_type,
             'cart_number': self.cart_number
            }
        return d

    @classmethod
    def newPurchase(cls, customer, items_ordered, cart_number):
        
        discount_type, discountMultiplier, pointsIn30Days = cls.findDiscountStatus(customer)
        order_discount = "None"
        #tally the points and cost
        points_earned = 0
        order_total = 0
        item_array = []
        for index in range(len(items_ordered)):
            item = items_ordered[index]
            if item.itemType in ItemsEligibleForDiscount:
                item.price = int(Decimal(item.price * discountMultiplier))
                order_discount = discount_type
            item.points = Item.getPoints(item.price)

            points_earned = points_earned + item.points
            order_total = order_total + item.price
            # create sub-array
            item_data = []
            item_data.append(item.itemID)
            item_data.append(item.itemType)
            item_data.append(item.price)
            item_data.append(item.points)
            # add to return array
            item_array.append(item_data)

        #Create purchase
        new_purcahse = Purchase(customer_ID = customer.card_number,
                                date = localdate.getToday('est'),
                                total = order_total, 
                                points_earned = points_earned,
                                items_ordered = item_array,
                                discount_type = order_discount,
                                cart_number = cart_number)

        customer.updatePoints(points_earned)
        #store the purcahse
        cls.put(new_purcahse)
        return new_purcahse

    @classmethod
    def findDiscountStatus(cls, customer):
        discountMultiplier = 1
        discount_type = "None"

        pointsIn30Days= cls.customerPointsLast30Days(customer.card_number)


        if (customer.points >= Points_Gold_Discount_Life) or (pointsIn30Days >= Points_Gold_Discount_30_Days):
            discountMultiplier = 0
            discount_type = "Gold"
        else:
            discountMultiplier = 0.5
            discount_type = "VIP"

        return discount_type, discountMultiplier, pointsIn30Days

    @classmethod
    def customerPointsLast30Days(cls, customer_ID):
        today = localdate.getToday('est')
        monthEnd = today + datetime.timedelta(-30)

        query = db.GqlQuery("SELECT * FROM Purchase " +
                "WHERE customer_ID = :1 AND date >= :2",
                customer_ID, monthEnd)

        pointsIn30Days = 0
        yesterday = today + datetime.timedelta(-1)
        for purchase in query.run():
            pointsIn30Days = pointsIn30Days + purchase.points_earned
            # If purchase was today
            if purchase.date >= yesterday:
                pointsIn30Days = pointsIn30Days + purchase.points_earned

        return pointsIn30Days

    @classmethod
    def todaysPurchases(cls, cart_number=None):
        today = localdate.getToday('est')
        yesterday = today + datetime.timedelta(-1)
        return cls.getPurchaseHistoryForPeriod(today, yesterday, cart_number)

    @classmethod
    def getPurchaseHistoryForPeriod(cls, start, end, cart_number=None):
        results = []
        query_params = ["SELECT * FROM Purchase WHERE date <= :1 AND date > :2", start, end]
        if cart_number is not None:
            query_params[0] = query_params[0] + " AND cart_number = :3"
            query_params.append(cart_number)
        query = db.GqlQuery(*query_params)

        for purchase in query.run():
            results.append(purchase.asDict())
        return results
