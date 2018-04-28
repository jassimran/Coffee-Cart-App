#!/usr/bin/python
import unittest
import webapp2
import json
import json_util
import main
from pprint import pprint
from google.appengine.api import memcache
from google.appengine.ext import db
from google.appengine.ext import testbed

from customer import Customer
from item import Item
from purchase import Purchase

def addVip(test, params, returnCode=201):     
    return sendRequest(test, 'POST', params=params, returnCode=returnCode, uri='/vip/')

def sendRequest(test, method, uid=None, params=None, returnCode=200, uri='/purchase/'):
    if uid:
        uri = uri + str(uid)
    request = webapp2.Request.blank(uri)
    request.method = method
    if params:
        request.body = json.dumps(params)
    response = request.get_response(main.app)
    test.assertEqual(response.status_int, returnCode)
    return json_util.decodeDict(json.loads(response.body))

class TestPurchase(unittest.TestCase):
    def setUp(self):
        # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()

    def testCreatePurchase(self):     
        main.addTestItems()

        params = {"name": "Coffee Dude", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        add_reply = addVip(self, params, 201)
        customer = Customer.byID(add_reply["card_number"])

        all_items = Item.getItems()
        item_list = [all_items[1].itemID, all_items[3].itemID]

        print
        print("Testing Happy path...")
        params = {'card_number': customer.card_number, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params)

        print("Testing customer points updated...")
        customer2 = Customer.byID(add_reply["card_number"])
        self.assertNotEqual(customer.points, customer2.points)
        pointsDelta = customer2.points - customer.points

        item_list = [all_items[2].itemID, all_items[3].itemID]
        params = {'card_number': customer.card_number, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params)

        # This one should have VIP discount
        print("Testing customer points updated with discount...")
        customer3 = Customer.byID(add_reply["card_number"])
        pointsDelta2 = customer3.points - customer2.points
        self.assertNotEqual(pointsDelta, pointsDelta2)

        print("Testing missing VIP ID...")
        params = {'card_number': None, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing VIP ID not found...")
        params = {'card_number': 9999, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing empty order...")
        item_list = []
        params = {'card_number': customer.card_number, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing invalid item...")
        item_list = [999]
        params = {'card_number': customer.card_number, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing purchase daily report...")
        reply = sendRequest(self, 'GET', uri='/dailypurchase/')
        #pprint(reply)
        self.assertNotEqual(len(reply["purchases"]), 0)
        

