## Main URL
    gatechteam4proj2.appspot.com
    
All data transferred between the server and the Android client is done using the JSON format. The data should be in the body of the request and will be in the body of a response.
    
## VIP Customer Functions

### Add Customer
**Url:** /vip/

**HTTP Method:** POST

**Input Parameters:**

    {
      "name": value,
      "dob": value, (formatted yyyy-mm-dd)
      "phone": value
    }
        
**Return Parameters:**
If the operation is successful, the following will be the response.

    {
      "name": value,
      "dob": value,
      "phone": value,
      "card_number": value,
      "points": value,
      "vip_status": value
    }
    
If an error condition is encountered the response could contain some or all of the following.

    {
      "error": message,
      "error_name": message,
      "error_dob": message, 
      "error_phone": message
    }

### Delete Customer
**Url:** /vip/`<cardnumber>`

**HTTP Method:** DELETE

**Input Parameters:** None
	
**Return Parameters:**
If the operation is successful, the following will be the response.

    {
      "complete": "The vip account given was successfully deleted."
    }
    
If an error condition is encountered the response could contain one of the following.

    {
      "error": message,
    }


### Edit VIP
**Url:** /vip/`<cardnumber>`

**HTTP Method:** PUT

**Input Parameters:**

    {
      "name": value,
      "dob": value, (formatted yyyy-mm-dd)
      "phone": value
    }
    
**Return Parameters:**
If the operation is successful, the following will be the response.

    {
      "complete": "The vip account was successfully updated."
    }
    
If an error condition is encountered the response could contain some or all of the following.

    {
      "error": message,
      "error_dob": message, 
    }

### Get Customer Info
**Url:** /vip/`<cardnumber>`

**HTTP Method:** GET

**Input Parameters:** None

**Return Parameters:**
If the operation is successful, the following will be the response.

    {
      "name": value,
      "dob": value,
      "phone": value,
      "card_number": value,
      "points": value,
      "vip_status": value
    }
    
If an error condition is encountered the response could contain one of the following.

    {
      "error": message,
    }


## Ordering and Inventory Functions
### Get Inventory
**Url:** /getinventory.json

**HTTP Method:** GET

**Input Parameters:** None

**Return Parameters:**
If the operation is successful, the response will be a JSON table with the itemID numbers as the keys, each referencing another table containing the price of the item and a description. The following is and example:

    {
      "1": { "itemType": "Coffee", "price": "2.00"},
      "2": { "itemType": "Chocolate Chip Cookie", "price": "3.00"}
    }
    
### Daily Preorders Report
**Url:** /dailypreorder.json

**HTTP Method:** GET

**Input Parameters:** None

**Return Parameters:**

    {  "preorders": 
      [
        {
          "cart_number": 1,
          "customer_ID": 1,
          "date_placed": "2014-07-12",
          "date_to_fulfill": "2014-07-22",
          "item_ID": 15
        },
        {
          "cart_number": 1,
          "customer_ID": 1,
          "date_placed": "2014-07-12",
          "date_to_fulfill": 2014-07-22,
          "item_ID": 17
        }
      ]
    }

### Daily Purchases Report
**Url:** /dailypurchase.json

**HTTP Method:** GET

**Input Parameters:** None

**Return Parameters:**

    {   "purchases":
      [
        {
          "customer_ID": 1,
          "date": "2014-07-10",
          "discount_type": "None",
          "items_ordered": [[13, "Coffee", "2.00", 2],
                            [15, "Chocolate Chip Cookie", "3.00", 3]],
          "points_earned": 5,
          "total": "5.00",
          "cart_number": 1
        },
        {
          "customer_ID": 1,
          "date": "2014-07-10",
          "discount_type": "VIP",
          "items_ordered": [[13, "Coffee", "1.00", 1],
                            [15, "Chocolate Chip Cookie", "3.00", 3]],
          "points_earned": 4,
          "total": "4.00",
          "cart_number": 1
        }
      ]
    }

### Place Preorder
**Url:** /preorder/

**HTTP Method:** POST

**Input Parameters:**
The input parameters should be a table containing the card number of the customer and an array of the itemID corresponding to each of the items ordered. This is the numeric table index value of the item found in the Get Inventory result. If the same item is ordered multiple times, the ID should be included that number of times in the list.

    {
      "card_number": value,
      "preorder_date": value, 
      "item_list": [1, 2, 3],
      "cart_number": value
    }


**Return Parameters:**
A successful execution will return:

    {
      "complete": "Preorder successfully added."
    }

If an error condition is encountered the response could contain some or all of the following.

    {
      "error": message,
      "error_card_number": message,
      "error_preorder_date": message,      
      "error_items": message or table of error messages,
      "error_cart_num": message,
    }


### Place Purchase
**Url:** /purchase/

**HTTP Method:** POST

**Input Parameters:**
The input parameters should be a table containing the card number of the customer and an array of the itemID corresponding to each of the items ordered. This is the numeric table index value of the item found in the Get Inventory result. If the same item is ordered multiple times, the ID should be included that number of times in the list.

    {
      "card_number": value,
      "item_list": [1, 2, 3],
      "cart_number": value
    }


**Return Parameters:**
A successful execution will return:

    {
      "complete": "Order successfully added."
    }

If an error condition is encountered the response could contain some or all of the following.

    {
      "error": message,
      "error_card_number": message, 
      "error_items": message,
      "error_cart_num": message,
    }


