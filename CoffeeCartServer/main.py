#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2
from webapp2 import Route
import json
import json_util
import jinja_util
import datetime
from pprint import pprint
import logging
import logging.config
from customer import Customer
from item import Item
from purchase import Purchase
from preorder import Preorder
from string import rstrip
import localdate

# read initial config file
logging.config.fileConfig('logging.conf')
log = logging.getLogger('webServer')


#########################################
########  PageHandler Functions  ########
#########################################

class PageHandler(webapp2.RequestHandler):
    def write(self, *a, **kw):
        self.response.out.write(*a, **kw)

    def renderJson(self, d):
        json_txt = json.dumps(d)
        self.response.headers['Content-Type'] = 'application/json; charset=UTF-8'
        self.write(json_txt)

    def renderStr(self, template, **params):
        #params['user'] = self.user
        return jinja_util.renderStr(template, **params)

    def render(self, template, **kw):
        self.write(self.renderStr(template, **kw))


class VIPHandler(PageHandler):
    # Get VIP customer info
    def get(self, uid=None):
        #log.debug("Get VIP")
        have_error = False
        params = {}

        if not uid:
            self.error(404)
            params['error'] = "No vip card number provided."
            self.renderJson(params)
        else:
            self.uid = int(uid)
            #make sure the user doesn't already exist
            customer =  Customer.byID(self.uid)
            if customer:
                self.renderJson(customer.asDict())
            else:
                self.error(404)
                params['error'] = "The customer info requested could not be found."
                self.renderJson(params)
                       

    # Add VIP customer
    def post(self):
        #log.debug("Add VIP")
        params = {}
        try:
            params = json_util.decodeDict(json.loads(self.request.body))
        except ValueError:
            self.error(400)
            self.renderJson({'error': "The information provided was incomplete."})

        if self.checkParamsErrors(params):
            params['error'] = "The information provided was incomplete or incorrect."
            self.error(400)
            self.renderJson(params)
        else:
            self.name = params['name']
            self.dob = self.checkDOB(params['dob'])
            self.phone = params['phone']

            #make sure the user doesn't already exist
            if Customer.findCustomer(self.name, self.dob, self.phone):
                msg = 'That user already exists.'
                error_dict = {'error': msg}
                self.error(400)
                self.renderJson(error_dict)
            else:
                customer = Customer.register(self.name, self.dob, self.phone)
                self.response.set_status(201)
                self.renderJson(customer.asDict())

    # Edit VIP customer info
    def put(self, uid=None):
        #log.debug("Edit VIP")
        have_error = False
        params = {}
        try:
            params = json_util.decodeDict(json.loads(self.request.body))
        except ValueError:
            self.error(400)
            self.renderJson({'error': "The information provided was incomplete."})

        if not uid:
            params['error_card_number'] = "No vip card number provided."
            have_error = True

        if self.checkParamsErrors(params) or have_error:
            params['error'] = "The information provided was incomplete."
            self.error(400)
            self.renderJson(params)
        else:
            #make sure the user doesn't already exist
            self.uid = int(uid)
            customer = Customer.byID(self.uid)
            if customer:
                self.name = params['name']
                self.dob = self.checkDOB(params['dob'])
                self.phone = params['phone']

                new_info_cust = Customer.findCustomer(self.name, self.dob, self.phone)
                if not new_info_cust:
                    if customer.updateInfo(self.uid, name=self.name, dob=self.dob, phone=self.phone):
                        return_val = {"complete": "The vip account was successfully updated."}
                        self.renderJson(return_val)
                else:
                    msg = 'The info provided matches that of an existing customer.'
                    error_dict = {'error': msg}
                    self.error(400)
                    self.renderJson(error_dict)
            else:
                msg = 'The vip card number provided does not exist.'
                error_dict = {'error': msg}
                self.error(404)
                self.renderJson(error_dict)

    # Delete VIP customer
    def delete(self, uid=None):
        #log.debug("Delete VIP")
        have_error = False
        params = {}

        if not uid:
            params['error'] = "No vip card number provided."
            self.error(400)
            self.renderJson(params)
        else:
            self.uid = int(uid)
            customer = Customer.byID(self.uid)
            if customer:
                if customer.deleteCustomer(self.uid):
                    return_val = {"complete": "The vip account given was successfully deleted."}
                    self.response.set_status(204)
                    self.renderJson(return_val)
            else:
                error_dict = {'error': 'The vip card number provided does not exist.'}
                self.error(404)
                self.renderJson(error_dict)

    def checkParamsErrors(self, params):
        have_error = False
        if not 'name' in params.keys() or params['name'] == "":
            params['error_name'] = "No name provided."
            have_error = True

        if not 'dob' in params.keys() or params['dob'] == "":
            params['error_dob'] = "No date of birth provided."
            have_error = True

        elif not self.checkDOB(params['dob']):
            params['error_dob'] = "Invalid date of birth provided."
            have_error = True

        if not 'phone' in params.keys() or params['phone'] == "":
            params['error_phone'] = "No phone number provided."
            have_error = True

        return have_error


    def checkDOB(self, inputDOB):
        split = inputDOB.split('-')
        today = localdate.getToday('est')

        if len(split) != 3:
            return None
        # Check Month
        if int(split[1]) > 12 or int(split[1]) < 1:
            return None
        # Check Day
        if int(split[2]) > 31 or int(split[2]) < 1:
            return None
        #Check Year
        if int(split[0]) < 1900:
            return None

        try:
            dob_date = datetime.datetime.strptime(inputDOB, "%Y-%m-%d").date()
            if(today >= dob_date):
                return inputDOB
        except ValueError:
            return None

class PurchaseHandler(PageHandler):
    def post(self):
        #log.debug("PurchaseHandler")
        have_error = False
        params = {}
        try:
            params = json_util.decodeDict(json.loads(self.request.body))
        except ValueError:
            self.error(400)
            self.renderJson({'error': "The information provided was incomplete."})

        if not 'card_number' in params.keys():
            params['error_card_number'] = "No vip card number provided."
            have_error = True
        else:
            self.customer = Customer.byID(params["card_number"])
            if not self.customer:
                params['error_card_number'] = "The vip card number provided does not exist."
                have_error = True

        self.order_list = []
        if not 'item_list' in params.keys() or len(params['item_list']) == 0:
            params['error_items'] = "No list of items was provided."
            have_error = True
        else:
            error_items = "Invalid item ID provided:"
            have_item_error = False
            for itemID in params['item_list']:
                item = Item.byID(itemID)
                if not item:
                    error_items = error_items + " " + str(itemID) + ","
                    have_item_error = True
                else:
                    self.order_list.append(item)
            if have_item_error:
                params['error_items'] = rstrip(error_items, ',')
                have_error = True

        if not 'cart_number' in params.keys():
            params['error_cart_num'] = "The cart number where this order was placed was not provided."
            have_error = True

        if have_error:
            params['error'] = "The information provided was incomplete."
            self.error(400)
            self.renderJson(params)

        else:
            purcahse = Purchase.newPurchase(self.customer, self.order_list, params["cart_number"])
            if purcahse:
                result = {"complete": "Order successfully added.", "purchase": purcahse.asDict()}
                self.renderJson(result)
            else:
                params['error'] = "There was an error while completing the order."
                self.error(400)
                self.renderJson(params)
        

class PreOrderHandler(PageHandler):
    def post(self):
        #log.debug("PreOrderHandler")
        have_error = False
        params = {}
        try:
            params = json_util.decodeDict(json.loads(self.request.body))
        except ValueError:
            self.error(400)
            self.renderJson({'error': "The information provided was incomplete."})

        if not 'card_number' in params.keys():
            params['error_card_number'] = "No vip card number provided."
            have_error = True
        else:
            customer = Customer.byID(params["card_number"])
            if not customer:
                params['error_card_number'] = "The vip card number provided does not exist."
                have_error = True

        if not 'item_list' in params.keys() or len(params['item_list']) == 0:
            params['error_items'] = "No list of items preordered was provided."
            have_error = True
        else:
            error_items = "Invalid item ID provided:"
            have_item_error = False
            for itemID in params['item_list']:
                if not Item.byID(itemID):
                    error_items = error_items + " " + str(itemID) + ","
                    have_item_error = True
            if have_item_error:
                params['error_items'] = rstrip(error_items, ',')
                have_error = True

        if not 'cart_number' in params.keys():
            params['error_cart_num'] = "The cart number where the preorder was placed was not provided."
            have_error = True

        if not 'preorder_date' in params.keys():
            params['error_preorder_date'] = "The fulfillment date for the preorder was not provided."
            have_error = True
        else:
            self.pre_order_date = self.checkPreorderDate(params["preorder_date"])
            if not self.pre_order_date:
                params['error_preorder_date'] = "The fulfillment date provided for the preorder was not valid."
                have_error = True

        if have_error:
            params['error'] = "The information provided was incomplete."
            self.error(400)
            self.renderJson(params)

        else:
            result = Preorder.newPreorder(params['card_number'], self.pre_order_date, params['item_list'], params["cart_number"])
            if isinstance(result, bool):
                result = {"complete": "Preorder successfully added."}
                self.renderJson(result)
            else:
                params['error'] = "There was an error while completing the preorder."
                params['error_items'] = result
                self.error(400)
                self.renderJson(params)

    def checkPreorderDate(self, date):
        split = date.split('-')
        today = localdate.getToday('est')

        if len(split) != 3:
            return None
        # Check Month
        if int(split[1]) > 12 or int(split[1]) < 1:
            return None
        # Check Day
        if int(split[2]) > 31 or int(split[2]) < 1:
            return None
        #Check Year
        if int(split[0]) < 1900:
            return None

        try:
            order_date = datetime.datetime.strptime(date, "%Y-%m-%d").date()
            next_month = today + datetime.timedelta(31)
            if(today < order_date) and (order_date < next_month):
                return order_date
        except ValueError:
            return None
        

class DailyPurchaseReportHandler(PageHandler):
    def get(self, cart_number=None):
        result = {}
        if cart_number is not None:
            cart_number = int(cart_number)
            result = { 'purchases': Purchase.todaysPurchases(cart_number) }
        else:
            result = { 'purchases': Purchase.todaysPurchases() }
        self.renderJson(result)

class DailyPreOrderReportHandler(PageHandler):
    def get(self, cart_number=None):
        result = {}
        if cart_number is not None:
            cart_number = int(cart_number)
            result = { 'preorders': Preorder.todaysPreorders(cart_number) }
        else:
            result = { 'preorders': Preorder.todaysPreorders() }
        self.renderJson(result)

class GetInventoryHandler(PageHandler):
    def get(self):
        item_list = Item.getInventory()
        self.renderJson(item_list)

class EditInventoryHandler(PageHandler):
    def get(self):
        self.write("Stuff")

    def post(self):
        log.debug("EditDessertsHandler")
        
        self.done()

    def done(self):
        result = {"complete": "Action complete"}
        self.renderJson(result)

class AddInventoryHandler(PageHandler):
    def post(self):
        have_error = False
        params = {}
        try:
            params = json_util.decodeDict(json.loads(self.request.body))
            pprint(params)
        except ValueError:
            self.error(400)
            self.renderJson({'error': "The information provided was incomplete."})

        if not 'price' in params.keys():
            params['error_price'] = "No price was provided."
            have_error = True

        if not 'type' in params.keys():
            params['error_type'] = "No type was provided."
            have_error = True

        if not 'best_seller' in params.keys():
            params['error_best_seller'] = "best_seller was not provided."
            have_error = True

        if not 'is_drink' in params.keys():
            params['is_drink'] = False

        item_data = [params['price'], params['type'], params['best_seller'], params['is_drink']]
        added_item = Item.newItem(*item_data)

        self.write("Done")

app = webapp2.WSGIApplication([
    ('/vip/', VIPHandler),
    Route('/vip/<uid:[0-9]+>', handler=VIPHandler),
    ('/purchase/', PurchaseHandler),
    ('/preorder/', PreOrderHandler),
    ('/dailypurchase.json', DailyPurchaseReportHandler),
    ('/dailypurchase/', DailyPurchaseReportHandler),
    Route('/dailypurchase/<cart_number:[0-9]+>', handler=DailyPurchaseReportHandler),
    ('/dailypreorder.json', DailyPreOrderReportHandler),
    ('/dailypreorder/', DailyPreOrderReportHandler),
    Route('/dailypreorder/<cart_number:[0-9]+>', handler=DailyPreOrderReportHandler),
    ('/getinventory.json', GetInventoryHandler),
    ('/editinventory.html', EditInventoryHandler),
    ('/addTestInventory', AddInventoryHandler),

], debug=True)

def addTestItems():
    #Populate the item database with some test cases
    #Item(price, itemType, bestSeller, isDrink=False)
    new_items = [ [2.00, "Coffee", False, True],
                  [2.00, "Coffee - Refill", False, True],
                  [3.00, "Chocolate Chip Cookie", False],
                  [4.50, "German Chocolate Cake", True],
                  [1.50, "Biscotti", False], 
                ]

    for item in new_items:
        i = Item.newItem(*item)

def main():
    # Set the logging level in the main function
    # See the section on Requests and App Caching for information on how
    # App Engine reuses your request handlers when you specify a main function
    logging.getLogger().setLevel(logging.DEBUG)
    webapp.util.run_wsgi_app(application)

if __name__ == '__main__':
    main()

