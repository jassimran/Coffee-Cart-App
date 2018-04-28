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

def addVip(test, params, returnCode=201):     
    return sendRequest(test, 'POST', params=params, returnCode=returnCode)

def getVip(test, uid, returnCode=200):
    return sendRequest(test, 'GET', uid=uid, returnCode=returnCode)

def editVip(test, uid, params, returnCode=200):
    return sendRequest(test, 'PUT', uid=uid, params=params, returnCode=returnCode)

def deleteVip(test, uid, returnCode=204):
    return sendRequest(test, 'DELETE', uid=uid, returnCode=returnCode)

def sendRequest(test, method, uid=None, params=None, returnCode=200):
    uri = '/vip/'
    if uid:
        uri = uri + str(uid)
    request = webapp2.Request.blank(uri)
    request.method = method
    if params:
        request.body = json.dumps(params)
    response = request.get_response(main.app)
    test.assertEqual(response.status_int, returnCode)
    return json_util.decodeDict(json.loads(response.body))

class TestVIPHandler(unittest.TestCase):
    def setUp(self):
        # First, create an instance of the Testbed class.
        self.testbed = testbed.Testbed()
        # Then activate the testbed, which prepares the service stubs for use.
        self.testbed.activate()
        # Next, declare which service stubs you want to use.
        self.testbed.init_datastore_v3_stub()
        self.testbed.init_memcache_stub()    

    def testAddVIP(self):
        params = {"name": "Joe Schmoe", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        dateTest = [["1999-13-02", True, 400], ["1999-01-33", True, 400], ["1899-01-02", True, 400], ["2015-07-06", True, 400]]  
        print
        print("Testing Happy path...")
        reply = addVip(self, params, 201)
        self.assertEqual( ("error_dob" in reply.keys()), False)

        for date in dateTest:
            params["dob"] = date[0]
            reply = addVip(self, params, date[2])
            self.assertEqual( ("error_dob" in reply.keys()), date[1])

        print("Testing missing name...")
        # Try update with missing values
        params = {"dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        params = {"name": "", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing DOB...")
        params = {"name": "Edit VIP II", "phone": "(775) 448-1235"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing phone...")
        params = {"name": "Edit VIP II", "dob": "1999-01-03"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing all info...")
        # Edit values missing all info
        reply = addVip(self, None, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing future DOB...")
        # Edit values with future dob
        params = {"name": "Edit VIP II", "dob": "2015-01-03", "phone": "(775) 448-1235"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing invalid DOB...")
        # Edit values with invalid dob
        params = {"name": "Edit VIP II", "dob": "2015/01/03", "phone": "(775) 448-1235"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing duplicate info...")
         # Edit values with duplicate info and try update
        params = {"name": "Joe Schmoe", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        reply = addVip(self, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

    def testCardNumbers(self):
        params = {"name": "0", "dob": "1999-01-02", "phone": "(775) 448-1234"}  
        reply = addVip(self, params) 
        start = reply["card_number"] + 1
        for number in range(start, start+25):
            params["name"] = str(number)
            reply = addVip(self, params)
            self.assertEqual(reply["card_number"], number)
    
    def testGetVIP(self):
        # Add a test VIP customer
        params = {"name": "Get VIP", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        add_reply = addVip(self, params)

        print
        print("Testing Happy path...")
        # Test retrieving previously added customer
        info_reply = getVip(self, add_reply["card_number"])
        self.assertEqual(add_reply, info_reply)

        print("Testing missing ID...")
        # Test no UID condition on get
        reply = getVip(self, None, 404)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing ID not found...")
        # Test no UID condition on get
        reply = getVip(self, 999, 404)
        self.assertEqual(("error" in reply.keys()), True)

    def testEditVIP(self):
        # Add a test VIP customer
        params = {"name": "Edit VIP", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        add_reply = addVip(self, params)

        print
        print("Testing Happy path...")
        # Edit values and try update
        params = {"name": "Edit VIP II", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params)
        self.assertEqual(("complete" in reply.keys()), True)

        print("Testing missing all fields...")
        # Edit values missing all info
        reply = editVip(self, add_reply["card_number"], None, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing duplicate info...")
        # Edit values with duplicate info and try update
        params = {"name": "Edit VIP II", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing future DOB...")
        # Edit values with future dob
        params = {"name": "Edit VIP II", "dob": "2015-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing invalid DOB...")
        # Edit values with invalid dob
        params = {"name": "Edit VIP II", "dob": "2015/01/03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing name...")
        # Try update with missing values
        params = {"dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        params = {"name": "", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing DOB...")
        params = {"name": "Edit VIP II", "phone": "(775) 448-1235"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing phone...")
        params = {"name": "Edit VIP II", "dob": "1999-01-03"}
        reply = editVip(self, add_reply["card_number"], params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing ID not found...")
        # Try edit with bad VIP ID
        params = {"name": "Edit VIP II", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, 9999, params, 404)
        self.assertEqual(("error" in reply.keys()), True)

        print("Testing missing ID...")
        params = {"name": "Edit VIP II", "dob": "1999-01-03", "phone": "(775) 448-1235"}
        reply = editVip(self, None, params, 400)
        self.assertEqual(("error" in reply.keys()), True)

        # Test retrieving previously edited customer
        info_reply = getVip(self, add_reply["card_number"])
        for key in params.keys():
            if key in info_reply.keys():
                self.assertEqual(info_reply[key], params[key])

    def testDeleteVIP(self):
        # Add a test VIP customer
        params = {"name": "Delete VIP", "dob": "1999-01-02", "phone": "(775) 448-1234"}
        add_reply = addVip(self, params)
        
        print
        print("Testing Happy path...")
        # Try deleting the VIP just added
        reply = deleteVip(self, add_reply["card_number"])
        self.assertEqual(("complete" in reply.keys()), True)

        # Test retrieving previously edited customer
        info_reply = getVip(self, add_reply["card_number"], 404)
        self.assertEqual(("error" in info_reply.keys()), True)

        print("Testing missing ID...")
        # Try deleting the VIP just added
        reply = deleteVip(self, None, 400)
        self.assertEqual(("error" in info_reply.keys()), True)

        print("Testing ID not found...")
        # Try deleting the VIP just added
        reply = deleteVip(self, 9999, 404)
        self.assertEqual(("error" in info_reply.keys()), True)

        

