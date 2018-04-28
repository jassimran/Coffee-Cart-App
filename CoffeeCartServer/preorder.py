#!/usr/bin/python
import datetime
from google.appengine.ext import db
from google.appengine.api import memcache
from pprint import pprint
from item import Item
import localdate

preorder_list_key = 'preorder_list'

##########################
#  Preorder DB
##########################
class Preorder(db.Model):
    customer_ID = db.IntegerProperty(required=True)
    date_placed = db.DateProperty(required=True)
    date_to_fulfill = db.DateProperty(required=True)
    cart_number = db.IntegerProperty(required=True)
    item_ID = db.IntegerProperty(required=True)
    

    def asDict(self):
        d = {'customer_ID': self.customer_ID,
             'date_placed': self.date_placed.strftime("%Y-%m-%d"),
             'date_to_fulfill': self.date_to_fulfill.strftime("%Y-%m-%d"),        
             'cart_number': self.cart_number,
             'item_ID': self.item_ID,
            }
        return d

    @classmethod
    def newPreorder(cls, customer_ID, date_to_fulfill, items_ordered, cart_number):       
        preorder_failures = {}
        preorder_success = []
        current_preorder_counts = cls.preorderCountsForItemsByDate(items_ordered, date_to_fulfill)

        for item_ID in items_ordered:
            item = Item.byID(item_ID)
            if current_preorder_counts[item_ID] < item.preorderCount:
                new_preorder = Preorder(customer_ID=customer_ID,
                                        date_placed=localdate.getToday('est'),
                                        date_to_fulfill=date_to_fulfill,
                                        cart_number=cart_number,
                                        item_ID=item_ID)
                preorder_success.append(new_preorder)
                current_preorder_counts[item_ID] = current_preorder_counts[item_ID] + 1
            else:
                if item.preorderCount == 0:
                    preorder_failures[item_ID] = "Item cannot be preordered."
                else:
                    preorder_failures[item_ID] = "Not enough preorder slots available."


        if len(preorder_failures.keys()) == 0:
            for order in preorder_success:
                order.put()
            return True
        else:
            return preorder_failures

    @classmethod
    def preorderCountsForItemsByDate(cls, items_ordered, date):
        result = {}
        for item_ID in items_ordered:
            result[item_ID] = 0             

        query_results = cls.getPreordersForDateToFulfill(date)
        for preorder in query_results:
            if preorder.item_ID in result.keys():
                result[preorder.item_ID] = result[preorder.item_ID] + 1
        return result


    @classmethod
    def getPreordersForDateToFulfill(cls, date, cart_number=None):
        results = []
        end = date + datetime.timedelta(1)
        query_params = ["SELECT * FROM Preorder WHERE date_to_fulfill >= :1 AND date_to_fulfill < :2", date, end]
        if cart_number is not None:
            query_params[0] = query_params[0] + " AND cart_number = :3"
            query_params.append(cart_number)
        query = db.GqlQuery(*query_params)
        for purchase in query.run():
            results.append(purchase)
        return results

    @classmethod
    def getPreordersForDatePlaced(cls, date, cart_number=None):
        results = []
        end = date + datetime.timedelta(1)
        query_params = ["SELECT * FROM Preorder WHERE date_placed >= :1 AND date_placed < :2", date, end]
        if cart_number is not None:
            query_params[0] = query_params[0] + " AND cart_number = :3"
            query_params.append(cart_number)
        query = db.GqlQuery(*query_params)
        for purchase in query.run():
            results.append(purchase)
        return results

    @classmethod
    def todaysPreorders(cls, cart_number=None):
        results = []
        today = localdate.getToday('est')
        for preorder in cls.getPreordersForDatePlaced(today, cart_number):
            results.append(preorder.asDict())
        return results
