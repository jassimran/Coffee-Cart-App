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
import datetime
from customer import Customer
from item import Item
from preorder import Preorder

def addVip(test, params, returnCode=201):     
    return sendRequest(test, 'POST', params=params, returnCode=returnCode, uri='/vip/')

def sendRequest(test, method, uid=None, params=None, returnCode=200, uri='/preorder/'):
    if uid:
        uri = uri + str(uid)
    request = webapp2.Request.blank(uri)
    request.method = method
    if params:
        request.body = json.dumps(params)
    response = request.get_response(main.app)
    test.assertEqual(response.status_int, returnCode)
    return json_util.decodeDict(json.loads(response.body))

class TestPreorder(unittest.TestCase):
    def setUp(self):
        # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()    

    def testCreatePreorder(self):     
        main.addTestItems()

        params = {"name": "Coffee Dude", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        add_reply = addVip(self, params, 201)
        customer = Customer.byID(add_reply["card_number"])

        all_items = Item.getItems()
        today = datetime.date.today()
        order_date = (today + datetime.timedelta(10)).strftime("%Y-%m-%d")

        print
        print("Testing Happy path...")
        item_list = [all_items[5].itemID]
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params)

        print("Testing preorder with Coffee...")
        item_list = [all_items[1].itemID, all_items[2].itemID]
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)
        #pprint(reply)

        item_list = [all_items[3].itemID]
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        for i in range(5):
            reply = sendRequest(self, 'POST', params=params)

        print("Testing preorder with over regular limit...")
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        item_list = [all_items[4].itemID]
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        for i in range(3):
            reply = sendRequest(self, 'POST', params=params)

        print("Testing preorder with over best seller limit...")
        reply = sendRequest(self, 'POST', params=params, returnCode=400)
        #pprint(reply)

        print("Testing missing VIP ID...")
        params = {'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing VIP ID not found...")
        params = {'card_number': 9999, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing empty preorder...")
        item_list = []
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing invalid item...")
        item_list = [999]
        params = {'card_number': customer.card_number, 'preorder_date': order_date, 'item_list': item_list, 'cart_number': 1}
        reply = sendRequest(self, 'POST', params=params, returnCode=400)

        print("Testing preorder daily report...")
        reply = sendRequest(self, 'GET', uri='/dailypreorder/')
        self.assertNotEqual(len(reply["preorders"]), 0)
        #pprint(reply)

