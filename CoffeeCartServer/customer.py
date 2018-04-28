#!/usr/bin/python
from google.appengine.ext import db
from google.appengine.api import memcache
from pprint import pprint
from purchase import Purchase

import logging
log = logging.getLogger('database')

customer_list_key = 'customer_list'

##########################
#  CustomerCounter DB
##########################
class CustomerCounter(db.Model):
    next_ID = db.IntegerProperty(required=True)

##########################
#  Customer DB
##########################
class Customer(db.Model):
    name = db.StringProperty(required=True)
    dob = db.StringProperty(required=True)
    phone = db.PhoneNumberProperty(required=True)
    points = db.IntegerProperty(required=True)
    card_number = db.IntegerProperty(required=True)
    
    def asDict(self):
        d = {'name': self.name,
             'dob': self.dob,
             'phone': self.phone,
             'card_number': self.card_number,
             'points': self.points,
             'vip_status': Purchase.findDiscountStatus(self)[0]
            }
        return d 

    @classmethod
    def getCustomers(cls, update=False):
        customer_list = memcache.get(customer_list_key)
        if customer_list is None:
            query = cls.all()
            customer_list = {}
            for customer in query.run():
                customer_list[customer.key().id()] = customer

        elif update:
            #log.debug("DB KEY QUERY!!!")
            u = cls.all(keys_only=True)
            missing_customer_list = []
            for customer_key in u.run():
                if customer_key.id() not in customer_list.keys():
                    missing_customer_list.append(customer_key)

            if missing_customer_list:
                #log.debug("DB QUERY!!!")
                for cust in cls.get(missing_customer_list):                  
                    customer_list[cust.key().id()] = cust
                memcache.set(customer_list_key, customer_list)

        return customer_list

    @classmethod
    def findNextVIPNumber(cls):
        customer_list = cls.getCustomers()
        highestCardNumber = 0
        for dbKey in customer_list:
            if highestCardNumber < customer_list[dbKey].card_number:
                highestCardNumber = customer_list[dbKey].card_number
        return highestCardNumber + 1

    @classmethod
    def byID(cls, card_number):
        customer_list = cls.getCustomers()
        for dbKey in customer_list:
            if customer_list[dbKey].card_number == card_number:
                return customer_list[dbKey]

    @classmethod
    def byName(cls, name):
        customer_list = cls.getCustomers()
        for dbKey in customer_list:
            if customer_list[dbKey].name == name:
                return customer_list[dbKey]

    @classmethod
    def findCustomer(cls, name, dob, phone):
        customer_list = cls.getCustomers()
        for dbKey in customer_list:
            if customer_list[dbKey].name == name and customer_list[dbKey].dob == dob and customer_list[dbKey].phone == phone:
                return customer_list[dbKey]

    @classmethod
    def updateInfo(cls, uid, name=None, dob=None, phone=None):
        customer_list = cls.getCustomers()
        customer = cls.byID(uid)
        if customer:
            if name:
                customer.name = name
            if dob:
                customer.dob = dob
            if phone:    
                customer.phone = phone
            customer.put()
            customer_list = cls.getCustomers(update = True)
            customer_list[customer.key().id()] = customer
            memcache.set(customer_list_key, customer_list)
            return True
        return False

    @classmethod
    def deleteCustomer(cls, uid):
        customer_list = cls.getCustomers()
        customer = cls.byID(uid)
        if customer:
            customer.delete()
            customer_list.pop(customer.key().id())
            memcache.set(customer_list_key, customer_list)
            return True
        return False

    @classmethod
    def register(cls, name, dob, phone):
        #vip_number = cls.findNextVIPNumber()
        customer_counter = CustomerCounter.get_or_insert("customer_counter", next_ID=1)

        new_cust = Customer(name=name, dob=dob, phone=phone, points=0, card_number=customer_counter.next_ID)

        cls.put(new_cust)
        customer_counter.next_ID = customer_counter.next_ID + 1
        customer_counter.put()

        customer_list = cls.getCustomers(update = True)
        if new_cust.key().id() not in customer_list.keys():
            customer_list[new_cust.key().id()] = new_cust
            memcache.set(customer_list_key, customer_list)
        return new_cust

    def updatePoints(self, points_earned):
        self.points = self.points + points_earned
        self.put()
        customer_list = Customer.getCustomers(update = True)
        customer_list[self.key().id()] = self
        memcache.set(customer_list_key, customer_list)
        return True
