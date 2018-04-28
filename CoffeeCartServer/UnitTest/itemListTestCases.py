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
from item import Item

def sendRequest(test, method, uid=None, params=None):
    uri = '/getinventory.json'
    if uid:
        uri = uri + str(uid)
    request = webapp2.Request.blank(uri)
    request.method = method
    if params:
        request.body = json.dumps(params)
    response = request.get_response(main.app)
    test.assertEqual(response.status_int, 200)
    return json_util.decodeDict(json.loads(response.body))

class TestItemList(unittest.TestCase):
    def setUp(self):
        # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()    

    def testCreateItems(self):     
        #Item(price, itemType, bestSeller, isDrink=False)
        new_items = [ [2.00, "Coffee", False, True],
                      [3.00, "Chocolate Chip Cookie", False],
                      [4.50, "German Chocolate Cake", True],
                      [1.50, "Biscotti", False], 
                    ]

        for item in new_items:
            i = Item.newItem(*item)

        item_list = Item.getItems()
        for index in item_list:
            self.assertEqual(item_list[index].itemType, new_items[index-1][1]) #item_list is 1-based, new_items is 0-based

        self.assertEqual(Item.byID(4).itemType, "Biscotti")
        self.assertEqual(Item.byType("Coffee").itemID, 1)

    def testGetItems(self):
        # Add some test data
        main.addTestItems()
        item_list = sendRequest(self, 'GET')
        self.assertNotEqual(item_list, None)
        self.assertNotEqual((len(item_list.keys())), 0)
