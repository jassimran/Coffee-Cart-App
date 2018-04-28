#!/usr/bin/python
from google.appengine.ext import db
from google.appengine.api import memcache
from decimal import *
from pprint import pprint
import logging
log = logging.getLogger('database')

item_list_key = 'item_list'

# Set decimal precision
getcontext().prec = 2
##########################
#  Item DB
##########################
class Item(db.Model):
    price = db.IntegerProperty(required=True)
    preorderCount = db.IntegerProperty(required=True)
    bestSeller = db.BooleanProperty(required=True)
    itemType = db.StringProperty(required=True)
    itemID = db.IntegerProperty(required=True)
    points = db.IntegerProperty(required=True)

    normalPreorderCount = 5
    bestSellerPreorderCount = 3
    drinkPreorderCount = 0

    def asDict(self):
        d = {'itemType': self.itemType,
             'price': "%0.2f" % (Decimal(self.price) / 100),
             'preorderCount': self.preorderCount,
             'bestSeller': self.bestSeller,
             'itemID': self.itemID
            }
        return d

    def forApp(self):
        d = {'itemType': self.itemType,
             'price': "%0.2f" % (Decimal(self.price) / 100),
             'preorderCount': self.preorderCount
            }
        return d

    @classmethod
    def newItem(cls, price, itemType, bestSeller, isDrink=False):
        next_item_ID = Item.getNextItemID()

        preorderCount = 0
        if isDrink:
            preorderCount = Item.drinkPreorderCount
        elif bestSeller:
            preorderCount = Item.bestSellerPreorderCount
        else:
            preorderCount = Item.normalPreorderCount

        stored_price = int(Decimal(price) * 100)
        new_item = Item(price=stored_price,
                        itemType=itemType,
                        bestSeller=bestSeller,
                        preorderCount=preorderCount,
                        points= cls.getPoints(price),
                        itemID=next_item_ID)

        cls.put(new_item)
        item_list = cls.getItems(update = True)
        if new_item.itemID not in item_list.keys():
            item_list[new_item.itemID] = new_item
            memcache.set(item_list_key, item_list)
        return new_item

    @classmethod
    def getNextItemID(cls):
        item_list = cls.getItems()
        highestItemNumber = 0
        for dbKey in item_list:
            if highestItemNumber < item_list[dbKey].itemID:
                highestItemNumber = item_list[dbKey].itemID
        return highestItemNumber + 1

    @classmethod
    def getItems(cls, update=False):
        item_list = memcache.get(item_list_key)
        if item_list is None:
            query = cls.all()
            item_list = {}
            highestItemNumber = 0
            for item in query.run():
                item_list[item.key().id()] = item
                if highestItemNumber < item.itemID:
                    highestItemNumber = item.itemID
            Item.nextItemID = highestItemNumber + 1

        elif update:
            #log.debug("DB KEY QUERY!!!")
            u = cls.all(keys_only=True)
            missing_item_list = []
            for item_key in u.run():
                if item_key.id() not in item_list.keys():
                    missing_item_list.append(item_key)

            if missing_item_list:
                #log.debug("DB QUERY!!!")
                
                for item in cls.get(missing_item_list):                  
                    item_list[item.key().id()] = item
                    

                memcache.set(item_list_key, item_list)
        return item_list

    @classmethod
    def byID(cls, itemID):
        item_list = cls.getItems()
        for key in item_list.keys():
            if item_list[key].itemID == itemID:
                return item_list[key]

    @classmethod
    def byType(cls, itemType):
        item_list = cls.getItems()
        for key in item_list.keys():
            if item_list[key].itemType == itemType:
                return item_list[key]

    @classmethod
    def getInventory(cls):
        item_list = cls.getItems()
        inventory = {}
        for key in item_list:
            inventory[item_list[key].itemID] =  item_list[key].forApp()
        return inventory

    @classmethod
    def getPoints(cls, price):
        return int(round(Decimal(price) / 100))
