<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](http://doctoc.herokuapp.com/)*

- [App](#app)
  - [Setting cart ID](#setting-cart-id)
    - [First load](#first-load)
    - [Editing - Cancel](#editing---cancel)
    - [Editing - Happy path](#editing---happy-path)
  - [Add VIP](#add-vip)
    - [Happy path](#happy-path)
    - [Cancel - Confirmed](#cancel---confirmed)
    - [Cancel - Not Confirmed](#cancel---not-confirmed)
    - [Missing name](#missing-name)
    - [Missing DOB](#missing-dob)
    - [Missing phone number](#missing-phone-number)
    - [Missing all fields](#missing-all-fields)
    - [Invalid DOB](#invalid-dob)
    - [Malformed DOB](#malformed-dob)
    - [Future DOB](#future-dob)
    - [Duplicate](#duplicate)
    - [Server communication error](#server-communication-error)
  - [View/Edit VIP](#viewedit-vip)
    - [Happy path - not gold](#happy-path---not-gold)
    - [Happy path - gold in last 30 days](#happy-path---gold-in-last-30-days)
    - [Happy path - lifetime gold](#happy-path---lifetime-gold)
    - [Cancel - Confirmed](#cancel---confirmed-1)
    - [Cancel - Not Confirmed](#cancel---not-confirmed-1)
    - [ID not found](#id-not-found)
    - [Edit fields](#edit-fields)
    - [Missing name](#missing-name-1)
    - [Missing DOB](#missing-dob-1)
    - [Missing phone number](#missing-phone-number-1)
    - [Missing all fields](#missing-all-fields-1)
    - [Invalid DOB](#invalid-dob-1)
    - [Malformed DOB](#malformed-dob-1)
    - [Future DOB](#future-dob-1)
    - [Duplicate](#duplicate-1)
    - [Server communication error - At lookup](#server-communication-error---at-lookup)
    - [Server communication error - At edit](#server-communication-error---at-edit)
  - [Delete VIP](#delete-vip)
    - [Happy path](#happy-path-1)
    - [Do not confirm](#do-not-confirm)
    - [Server communication error](#server-communication-error-1)
  - [Record VIP Purchase](#record-vip-purchase)
    - [Happy path - not gold](#happy-path---not-gold-1)
    - [Happy path - has gold](#happy-path---has-gold)
    - [Happy path - becomes gold](#happy-path---becomes-gold)
    - [Happy path - clear](#happy-path---clear)
    - [Canceling purchase - Confirmed](#canceling-purchase---confirmed)
    - [Canceling purchase - Not Confirmed](#canceling-purchase---not-confirmed)
    - [Empty purchase](#empty-purchase)
    - [Server communication error](#server-communication-error-2)
  - [Pre-order Item](#pre-order-item)
    - [Happy path](#happy-path-2)
    - [Cancel - Confirmed](#cancel---confirmed-2)
    - [Cancel - Not Confirmed](#cancel---not-confirmed-2)
    - [No slots - Not bestseller](#no-slots---not-bestseller)
    - [No slots - Bestseller](#no-slots---bestseller)
    - [Insufficient slots - Not bestseller](#insufficient-slots---not-bestseller)
    - [Insufficent slots - Bestseller](#insufficent-slots---bestseller)
    - [Empty preorder](#empty-preorder)
    - [Missing date](#missing-date)
    - [Invalid date](#invalid-date)
    - [Server communication error](#server-communication-error-3)
  - [Generate Daily Report](#generate-daily-report)
    - [Happy path](#happy-path-3)
    - [No purchases or preorders](#no-purchases-or-preorders)
    - [Server communication error](#server-communication-error-4)
- [Server](#server)
  - [Add VIP](#add-vip-1)
    - [Happy path](#happy-path-4)
    - [Missing name](#missing-name-2)
    - [Missing DOB](#missing-dob-2)
    - [Missing phone number](#missing-phone-number-2)
    - [Missing all fields](#missing-all-fields-2)
    - [Invalid DOB](#invalid-dob-2)
    - [DOB in future](#dob-in-future)
    - [Duplicate](#duplicate-2)
  - [Delete VIP](#delete-vip-1)
    - [Happy path](#happy-path-5)
    - [Missing ID](#missing-id)
    - [ID not found](#id-not-found-1)
  - [Edit VIP](#edit-vip)
    - [Happy path](#happy-path-6)
    - [Missing ID](#missing-id-1)
    - [ID not found](#id-not-found-2)
    - [Missing name](#missing-name-3)
    - [Missing DOB](#missing-dob-3)
    - [Missing phone number](#missing-phone-number-3)
    - [Missing all fields](#missing-all-fields-3)
    - [Invalid DOB](#invalid-dob-3)
    - [DOB in future](#dob-in-future-1)
    - [Duplicate](#duplicate-3)
  - [Get Inventory](#get-inventory)
    - [Happy path](#happy-path-7)
  - [Get VIP](#get-vip)
    - [Happy path](#happy-path-8)
    - [Missing ID](#missing-id-2)
    - [ID not found](#id-not-found-3)
  - [Preorder Report](#preorder-report)
    - [Happy path](#happy-path-9)
  - [Purchases Report](#purchases-report)
    - [Happy path](#happy-path-10)
  - [Place Preorder](#place-preorder)
    - [Happy path](#happy-path-11)
    - [Empty preorder](#empty-preorder-1)
    - [Invalid quantity](#invalid-quantity)
    - [Invalid item](#invalid-item)
    - [Not enough slots - bestseller](#not-enough-slots---bestseller)
    - [Not enough slots - not bestseller](#not-enough-slots---not-bestseller)
    - [Missing VIP ID](#missing-vip-id)
    - [VIP customer not found](#vip-customer-not-found)
  - [Place Purchase](#place-purchase)
    - [Happy path - not gold](#happy-path---not-gold-2)
    - [Happy path - has gold](#happy-path---has-gold-1)
    - [Happy path - becomes gold](#happy-path---becomes-gold-1)
    - [Happy path - with preorders](#happy-path---with-preorders)
    - [Happy path - with preorders (deferred)](#happy-path---with-preorders-deferred)
    - [Empty purchase](#empty-purchase-1)
    - [Zero quantity with other purchases](#zero-quantity-with-other-purchases)
    - [Zero quantity only](#zero-quantity-only)
    - [Invalid quantity](#invalid-quantity-1)
    - [Invalid item](#invalid-item-1)
    - [Missing VIP ID](#missing-vip-id-1)
    - [VIP customer not found](#vip-customer-not-found-1)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# App

## Setting cart ID

### First load

1. Start the app for the first time
 * Alternately, in the Android menu, go to Settings and clear all data for the app, then open the app
 * Expected: On load, a dialog box should appear asking the user to enter a cart ID
 * Expected: The dialog box should have an OK button and no cancel button
2. Tap the back button
 * Expected: Nothing happens; the back button does not cancel the dialog.
3. With an empty dialog box, tap OK
 * Expected: Nothing happens; the OK button does not cancel the dialog.
4. Try to enter non-numeric characters
 * Expected: The input is rejected; the field only accepts integers
5. Enter an integer in the field
6. Tap OK
 * Expected: The user is taken to the main menu
 * Expected: The cart ID the user entered is populated in the "Cart ID" field
7. Tap the back button
 * Expected: The user exits the app
8. Restart the device
9. Start the app again
 * Expected: The user is taken to the main menu. No dialog box appears.
 * Expected: The cart ID the user entered is populated in the "Cart ID" field

### Editing - Cancel

1. From the main menu, tap the cart ID
 * Expected: A dialog box should appear asking the user to enter a cart ID. The current cart ID is prefilled.
 * Expected: The dialog box should have an OK button and a cancel button
2. Clear the input field
3. Tap OK
 * Expected: Nothing happens; the OK button does not cancel the dialog
4. Try to enter non-numeric characters
 * Expected: The input is rejected; the field only accepts integers
5. Tap Cancel
 * Expected: The dialog is dismissed. The "Cart ID" field on the main menu is unchanged.

### Editing - Happy path

1. From the main menu, tap the cart ID
 * Expected: A dialog box should appear asking the user to enter a cart ID. The current cart ID is prefilled.
 * Expected: The dialog box should have an OK button and a cancel button
2. Enter a new integer to the input field
3. Tap OK
 * Expected: The user is taken to the main menu
 * Expected: The cart ID the user entered is populated in the "Cart ID" field
4. Tap the back button
 * Expected: The user exits the app
5. Restart the device
6. Start the app again
 * Expected: The user is taken to the main menu. No dialog box appears.
 * Expected: The cart ID the user entered is populated in the "Cart ID" field

## Add VIP

### Happy path

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields
3. Tap the "Submit" button
 * Expected: A success message is displayed with the new VIP's ID
4. Dismiss the success message
 * Expected: We are taken to the main menu
5. Tap the "Edit" button
6. Enter the VIP ID from step 3 and submit the form.
 * Expected: The VIP's information is displayed, and all fields are identical to those in step 2.
 
### Cancel - Confirmed

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields
3. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
4. Tap "Yes"
 * Expected: We are taken to the main menu. Nothing has been added to the database.
 
### Cancel - Not Confirmed

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields
3. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
4. Tap "No"
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Missing name

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except leave the name field blank.
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing name
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Missing DOB

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except leave the DOB field blank.
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing DOB
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Missing phone number

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except leave the phone number field blank.
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing phone number
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Missing all fields

1. From the app's main menu, tap the "Add" button
2. Do not enter any information in any field.
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of all the missing fields.
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen.

### Invalid DOB

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except enter a date of birth that isn't a real date
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an erroneous DOB
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Malformed DOB

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except enter a date of birth in an incorrect format
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an erroneous DOB
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Future DOB

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields, except enter a date of birth in the future
3. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an erroneous DOB
4. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Duplicate

1. From the app's main menu, tap the "Add" button
2. Enter valid, unique information in all fields
3. Tap the "Submit" button
 * Expected: A success message is displayed with the new VIP's ID
4. Dismiss the success message
 * Expected: We are taken to the main menu
5. Tap the "Add VIP" button
6. Enter the same information from step 2 in all fields
7. Tap the "Submit" button
 * Expected: A failure message is displayed saying the user's information already exists
8. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

### Server communication error

1. Disconnect the phone from all data services (Wi-Fi, 4G, etc.), so it cannot communicate with the server
2. From the app's main menu, tap the "Add VIP" button
3. Enter valid, unique information in all fields
4. Tap the "Submit" button
 * Expected: After a suitable timeout, a failure message is displayed, informing the user of a server communication error
5. Dismiss the failure message
 * Expected: We remain at the "Add VIP" screen. All fields are populated with their previous values.

## View/Edit VIP

### Happy path - not gold

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID for a user who does not have gold status
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
 * Expected: There is a non-editable field stating the user's total VIP points, and the word "GOLD" does not appear next to it
4. Tap the Android "Back" button
 * Expected: We are taken back to the main menu.

### Happy path - gold in last 30 days

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID for a user who has gold status because of purchases in the last 30 days, but not lifetime gold status
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
 * Expected: There is a non-editable field stating the user's total VIP points, and the word "GOLD" appears next to it
4. Tap the Android "Back" button
 * Expected: We are taken back to the main menu.

### Happy path - lifetime gold

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID for a user who has lifetime gold status
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
 * Expected: There is a non-editable field stating the user's total VIP points, and the words "GOLD" appears next to it
4. Tap the Android "Back" button
 * Expected: We are taken back to the main menu.
 
### Cancel - Confirmed

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name, DOB, and phone number
5. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
6. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Cancel - Not Confirmed

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name, DOB, and phone number
5. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
6. Tap "No"
 * Expected: We remain at the "Edit VIP" screen. The fields are unchanged from what the user entered.

### ID not found

1. From the app's main menu, tap the "Edit" button
2. Enter an invalid ID
3. Tap the "Submit" button
 * Expected: A failure message is displayed informing the user that no VIP member has that ID
4. Dismiss the failure message
 * Expected: We remain at the ID entry screen

### Edit fields

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name, DOB, and phone number
5. Tap the "Submit" button
 * Expected: A success message is displayed
6. Dismiss the success message
 * Expected: We are taken to same screen we saw after step 3, except the fields are updated with their new values.
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: We are taken back to the main menu

### Missing name

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's DOB and phone number, but make the user's name blank
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing name
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The DOB and phone number fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Missing DOB

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name and phone number, but make the user's DOB blank
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing DOB
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The name and phone number fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Missing phone number

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name and DOB, but make the user's phone number blank
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of a missing phone number
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The name and DOB fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Missing all fields

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Make all editable fields blank
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of the missing fields
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The fields remain blank.
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Invalid DOB

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name and phone number, but enter a date that does not exist
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an invalid DOB
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The name and phone number fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Malformed DOB

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name and phone number, but enter a date that is malformed
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an invalid DOB
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The name and phone number fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Future DOB

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Edit the user's name and phone number, but enter a date that is in the future
5. Tap the "Submit" button
 * Expected: A failure message is displayed, informing the user of an invalid DOB
6. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3. The name and phone number fields are unchanged. 
 * Expected: The VIP points the user has does not change
7. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
8. Tap "Yes"
 * Expected: We are taken back to the main menu. No changes have been made to the user in the database.

### Duplicate

1. From the app's main menu, tap the "Add VIP" button
2. Enter valid, unique information in all fields
3. Tap the "Submit" button
 * Expected: A success message is displayed with the new VIP's ID
4. Dismiss the success message
 * Expected: We are taken to the main menu
5. Tap the "Edit" button
6. Enter a valid ID for a different user
7. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
8. Enter the same information from step 2 in all fields
9. Tap the "Submit" button
 * Expected: A failure message is displayed saying the user's information already exists
10. Dismiss the failure message
 * Expected: We remain at the "Edit VIP" screen. All fields are populated with their previous values.

### Server communication error - At lookup

1. Disconnect the phone from all data services (Wi-Fi, 4G, etc.)
2. From the app's main menu, tap the "Edit" button
3. Enter a valid ID
4. Tap the "Submit" button
 * Expected: A failure message is displayed
7. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3, with the ID field still populated.

### Server communication error - At edit

1. From the app's main menu, tap the "Edit" button
2. Enter a valid ID
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are editable ones for name, DOB, and phone number.
4. Exit the app using the Home button, disconnect the phone from all data services (Wi-Fi, 4G, etc.), and re-enter the app
5. Edit the user's name, DOB, and phone number
6. Tap the "Submit" button
 * Expected: A failure message is displayed
7. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 5.

## Delete VIP

### Happy path

1. From the app's main menu, tap the "Delete" button
2. Enter a valid ID for a user
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Delete" button
 * Expected: A modal confirmation dialog appears asking the user if they are sure
5. Tap "Yes"
 * Expected: All purchase information for the user is retained. Any future preorders the user has, if any, are retained.
 * Expected: We are taken to the main menu
6. Tap "Edit"
7. Enter the former user's ID
8. Tap the "Submit" button
 * Expected: The user is not found

### Do not confirm

1. From the app's main menu, tap the "Delete" button
2. Enter a valid ID for a user
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Delete" button
 * Expected: A modal confirmation dialog appears asking the user if they are sure
5. Tap "No"
 * Expected: We are taken back to the screen seen after step 3.
 * Expected: The user remains in the system

### Server communication error

1. From the app's main menu, tap the "Delete" button
2. Enter a valid ID for a user
3. Tap the "Submit" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Exit the app using the Home button, disconnect the phone from all data services (Wi-Fi, 4G, etc.), and re-enter the app
5. Tap the "Delete" button
 * Expected: A modal confirmation dialog appears asking the user if they are sure
6. Tap "Yes"
 * Expected: A failure message is displayed
7. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 3.

## Record VIP Purchase

### Happy path - not gold

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the item list
10. Select a dessert and tap Add Item
 * Expected: We are taken to the screen from after step 8, except the dessert is listed, along with its price, and the Total is the price of the coffee + refill + dessert. The VIP Points count is updated correctly.
11. Tap the "Record purchase" button
 * Expected: We are taken back to the View/Edit screen for this user. The user's VIP points total and last 30 days total has been updated with the points from the purchase.
 * Expected: The purchase is recorded in the database and affects any Reports generated for this day.

### Happy path - has gold

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "GOLD status" is displayed at the top. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its (discounted) price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the item list
10. Select a dessert and tap Add Item
 * Expected: We are taken to the screen from after step 8, except the dessert is listed, along with its price, and the Total is the price of the coffee + refill + dessert. The VIP Points count is updated correctly.
11. Tap the "Record purchase" button
 * Expected: We are taken back to the View/Edit screen for this user. The user's VIP points total and last 30 days total has been updated with the points from the purchase.
 * Expected: The purchase is recorded in the database and affects any Reports generated for this day.

### Happy path - becomes gold

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the item list
10. Add a dessert repeatedly, enough to make the user become gold
11. Tap the "Record purchase" button
 * Expected: We are taken back to the View/Edit screen for this user. The user's VIP points total and last 30 days total has been updated with the points from the purchase and the new gold status is reflected here.
 * Expected: The purchase is recorded in the database and affects any Reports generated for this day.
 
### Happy path - clear

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the item list
10. Select a dessert and tap Add Item
 * Expected: We are taken to the screen from after step 8, except the dessert is listed with (2) appended, along with its price, and the Total is the price of the coffee + refill + dessert. The VIP Points count is updated correctly.
11. Tap the "Clear Items" button
 * Expected: All items are removed from the purchase.

### Canceling purchase - Confirmed

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
10. Tap "Yes"
 * Expected: We are taken back to the View/Edit screen for this user. No purchase was recorded and their VIP points total is unchanged.

### Canceling purchase - Not Confirmed

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
7. Tap the item list
8. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 6, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
9. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
10. Tap "No"
 * Expected: We remain at the purchase screen, still listing the Coffee and Refill

### Empty purchase

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
6. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 4, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly."
7. Tap "Clear Items"
 * Expected: We are taken to the screen from after step 4.
8. Tap the "Record purchase" button
 * Expected: A dialog appears stating that the purchase is empty.
 * Expected: We stay at the same screen. No purchase is recorded.

### Server communication error

1. From the app's main menu, tap the "Purchase" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Purchase" button
 * Expected: We are taken to a screen allowing the user to add items to a purchase. "Total $0.00" is displayed at the bottom.
5. Exit the app using the Home button, disconnect the phone from all data services (Wi-Fi, 4G, etc.), and re-enter the app
6. Tap the item list
 * Expected: A list of items appears, which includes all desserts, coffee, and refill.
7. Select Coffee and tap Add Item
 * Expected: We are taken to the screen from after step 5, except the Coffee is listed, along with its price, and the Total is the price of the coffee. The VIP Points count is updated correctly.
8. Tap the item list
9. Select Refill and tap Add Item
 * Expected: We are taken to the screen from after step 7, except the Refill is listed, along with its price, and the Total is the price of the coffee + refill. The VIP Points count is updated correctly.
10. Tap the item list
11. Select a dessert and tap Add Item
 * Expected: We are taken to the screen from after step 9, except the dessert is listed, along with its price, and the Total is the price of the coffee + refill + dessert. The VIP Points count is updated correctly.
13. Tap the "Make purchase" button
 * Expected: A failure message is displayed
14. Dismiss the failure message
 * Expected: We are taken to same screen we saw after step 12.

## Pre-order Item

### Happy path

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is known to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * Expected: We are taken back to the View/Edit VIP screen for the user
 * Expected: The preorder was recorded in the database and will appear in reports, as well as affect the "Record Purchase" screen's behavior on the day of the preorder.
 
### Cancel - Confirmed

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is known to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
10. Tap "Yes"
 * Expected: We are taken back to the View/Edit screen for this user. No preorder was recorded.

### Cancel - Not Confirmed

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is known to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the Android "Back" button
 * Expected: A modal confirmation dialog appears informing the user that they will lose their entered information if they cancel
10. Tap "No"
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### No slots - Not bestseller

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is not a bestseller, and is known not to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * An error dialog appears informing the user that there are 0 slots for [dessert].
10. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### No slots - Bestseller

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is a bestseller, and is known not to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * An error dialog appears informing the user that there are 0 slots for [dessert].
10. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### Insufficient slots - Not bestseller

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is not a bestseller, and is known not to have only one open preorder slot for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 2 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * An error dialog appears informing the user that there are 1 slots for [dessert].
10. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### Insufficent slots - Bestseller

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is a bestseller, and is known not to have only one open preorder slot for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 2 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * An error dialog appears informing the user that there are 1 slots for [dessert].
10. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### Empty preorder

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a valid date in the date field
6. Tap the "Submit" button
 * An error dialog appears informing the user that the preorder is empty.
7. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 5. No preorder was recorded.
 
### Missing date

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Tap the Add Item button
 * Expected: A list of desserts is displayed
6. Tap a dessert
 * Expected: A dialog to enter a quantity is displayed
7. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 4, but the dessert is listed in the Preorder
8. Tap the "Submit" button
 * An error dialog appears informing the user that the date field is empty.
9. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 7. No preorder was recorded.

### Invalid date

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
5. Enter a date in the past in the date field
6. Tap the Add Item button
 * Expected: A list of desserts is displayed
7. Tap a dessert which is known to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
8. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
9. Tap the "Submit" button
 * Expected: An error dialog appears informing the user that the date is invalid.
10. Dismiss the error dialog
 * Expected: We are taken back to the screen as seen after step 8. No preorder was recorded.

### Server communication error

1. From the app's main menu, tap the "Preorder" button
2. Enter a valid ID for a user
3. Tap the "Find VIP" button
 * Expected: We are taken to a screen for the given VIP ID
 * Expected: All fields are correctly populated, and there are uneditable ones for name, DOB, and phone number.
4. Exit the app using the Home button, disconnect the phone from all data services (Wi-Fi, 4G, etc.), and re-enter the app
5. Tap the "Preorder" button
 * Expected: We are taken to a screen with a date field, an empty list, and an "Add Item" button
6. Enter a valid date in the date field
7. Tap the Add Item button
 * Expected: A list of desserts is displayed
8. Tap a dessert which is known to have open preorder slots for that date
 * Expected: A dialog to enter a quantity is displayed
9. Type the quantity 1 and confirm
 * Expected: We are taken to the screen from after step 5, but the dessert is listed in the Preorder
10. Tap the "Submit" button
 * Expected: A failure message is displayed
11. Dismiss the failure message
 * Expected: We are taken back to the screen as seen after step 9. No preorder was recorded.

## Generate Daily Report

### Happy path

1. Ensure that there are both purchases and preorders for today
2. From the app's main menu, tap the "Daily Report" button
 * Expected: A report is displayed. The report contains two sections, one of which contains all purchases for the day, the second contains all preorders for the day.
 * Expected: In the purchase section, a table is displayed with the columns "Time", "VIP ID", and "Total". At the bottom is a sum of the purchases made.
 * Expected: Each purchase made today is displayed in the table, ordered by time, ascending
 * Expected: In the preorders section, a table is displayed with the columns "VIP ID" and "Total". At the bottom is a sum of the preorders made.
 * Expected: Each preorder for today is displayed in the table, ordered by VIP ID, ascending
3. Tap one of the purchases.
 * Expected: We are taken to a screen that displays the purchase details. It is similar to the "Record Purchase" screen but is not editable.
4. Press the Android "Back" button
 * Expected: We are taken to the screen from after step 1.
5. Tap one of the preorders.
 * Expected: We are taken to a screen that displays the preorder details. It is similar to the "Preorder" screen but is not editable.
6. Press the Android "Back" button
 * Expected: We are taken to the screen from after step 1.
7. Tap the Android "Back" button
 * Expected: We are taken back to the main menu
 
### No purchases or preorders

1. Ensure that there are no purchases and preorders for today
2. From the app's main menu, tap the "Daily Report" button
 * Expected: A report is displayed. The report contains two sections, one of which contains all purchases for the day, the second contains all preorders for the day.
 * Expected: In the purchase section, a table is displayed with the columns "Time", "VIP ID", and "Total". At the bottom is a sum of the purchases made.
 * Expected: As there are no purchases, the table is empty. The sum is "$0.00"
 * Expected: In the preorders section, a table is displayed with the columns "VIP ID" and "Total". At the bottom is a sum of the preorders made.
 * Expected: As there are no preorders, the table is empty. The sum is "$0.00"
3. Tap the Android "Back" button
 * Expected: We are taken back to the main menu

### Server communication error

1. Disconnect the phone from all data services (Wi-Fi, 4G, etc.), so it cannot communicate with the server
2. From the app's main menu, tap the "Daily Report" button
 * Expected: After a suitable timeout, a failure message is displayed, informing the user of a server communication error
3. Dismiss the failure message
 * Expected: We remain at the main menu

# Server

## Add VIP

### Happy path

1. Issue an Add VIP command to the server per the API documentation, with a unique name, date of birth, and phone number
 * Expected: A "201 Created" response, whose payload is a JSON object containing "name", "dob", and "phone exactly" as passed, "card_number" with a unique integer ID, and "points" which is 0.
 * Expected: The database contains a new entry with all data returned in the response payload

### Missing name

1. Issue an Add VIP command to the server per the API documentation, with a unique date of birth and phone number, but missing name
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_name" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing DOB

1. Issue an Add VIP command to the server per the API documentation, with a unique name and phone number, but missing date of birth
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing phone number

1. Issue an Add VIP command to the server per the API documentation, with a unique name and date of birth, but missing phone number
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_phone" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing all fields

1. Issue an Add VIP command to the server per the API documentation, but missing all fields.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing "error_name", "error_dob", and "error_phone" keys which map to human-readable error messages.
 * Expected: The database is unchanged

### Invalid DOB

1. Issue an Add VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, but the date of birth is not in yyyy-mm-dd format.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### DOB in future

1. Issue an Add VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, but the date of birth is in the future.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Duplicate

1. Issue an Add VIP command to the server per the API documentation, with a duplicate name, date of birth, and phone number.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

## Delete VIP

### Happy path

1. Issue a Delete VIP command to the server per the API documentation, with an ID field that maps to a VIP customer in the database
 * Expected: A "204 No Content" response with no payload.
 * Expected: The VIP customer is marked as active=false in the database

### Missing ID

1. Issue a Delete VIP command to the server per the API documentation, missing an ID field
 * Exected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### ID not found

1. Issue a Delete VIP command to the server per the API documentation, with an ID field containing an integer that is not an ID for a VIP customer
 * Exected: A "404 Not Found" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

## Edit VIP

### Happy path

1. Issue an Edit VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, and an ID field that maps to a VIP customer in the database
 * Expected: A "200 OK" response, whose payload is a JSON object containing "name", "dob", and "phone exactly" as passed, "card_number" with a unique integer ID, and "points" which is however many points the user had.
 * Expected: The database entry for that VIP customer has been updated with all data returned in the response payload

### Missing ID

1. Issue an Edit VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, but missing the ID field
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### ID not found

1. Issue an Edit VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, with an ID field containing an integer that is not an ID for a VIP customer
 * Expected: A "404 Not Found" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing name

1. Issue an Edit VIP command to the server per the API documentation, with a unique date of birth and phone number, and an ID field that maps to a VIP customer in the database, but missing the name field
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_name" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing DOB

1. Issue an Edit VIP command to the server per the API documentation, with a unique name and phone number, and an ID field that maps to a VIP customer in the database, but missing the date of birth field
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing phone number

1. Issue an Edit VIP command to the server per the API documentation, with a unique date of birth and name, and an ID field that maps to a VIP customer in the database, but missing the phone number field
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_phone" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing all fields

1. Issue an Edit VIP command to the server per the API documentation, with a unique date of birth and phone number, and an ID field that maps to a VIP customer in the database, but missing the name field
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing "error_name", "error_dob", and "error_phone" keys which map to human-readable error messages.
 * Expected: The database is unchanged

### Invalid DOB

1. Issue an Edit VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, and an ID field that maps to a VIP customer in the database, but the date of birth is not in yyyy-mm-dd format.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### DOB in future

1. Issue an Edit VIP command to the server per the API documentation, with a unique name, date of birth, and phone number, and an ID field that maps to a VIP customer in the database, but the date of birth is in the future.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error_dob" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Duplicate

1. Issue an Edit VIP command to the server per the API documentation, with a duplicate name, date of birth, and phone number, and an ID field that maps to a VIP customer in the database
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

## Get Inventory

### Happy path

1. Issue a Get Inventory command to the server per the API documentation.
 * Expected: A "200 OK" response, whose payload is a JSON object containing all desserts, coffee, and refill.

## Get VIP

### Happy path

1. Issue a Get VIP command to the server per the API documentation, with an ID field that maps to a VIP customer in the database
 * Expected: A "200 OK" response, whose payload is a JSON object containing "name", "dob", "phone", "card_number", and "points" with correct values from the database.

### Missing ID

1. Issue a Get VIP command to the server per the API documentation, missing the ID field
 * Expected: A "400 Bad Request" response, with no payload.

### ID not found

1. Issue a Get VIP command to the server per the API documentation, with an ID field that does not map to a VIP customer in the database
 * Expected: A "404 Not Found" response, with no payload.

## Preorder Report

### Happy path

1. Issue a Preorder Report command to the server per the API documentation.
 * Expected: A "200 OK" response, containing all data for the preorder report.

## Purchases Report

### Happy path

1. Issue a Purchases Report command to the server per the API documentation.
 * Expected: A "200 OK" response, containing all data for the purchases report.

## Place Preorder

### Happy path

1. Issue a Place Preorder command to the server per the API documentation, with one dessert (with open slots), and a valid VIP customer ID
 * Expected: A "200 OK" response, containing all data for the created preorder
 * Expected: The preorder was created in the database with the correct information.

### Empty preorder

1. Issue a Place Preorder command to the server per the API documentation, with no desserts, and a valid VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Invalid quantity

1. Issue a Place Preorder command to the server per the API documentation, with one dessert with a quantity of -1, and a valid VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Invalid item

1. Issue a Place Preorder command to the server per the API documentation, with one dessert (which does not actually exist in the database) with a quantity of 1, and a valid VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Not enough slots - bestseller

1. Issue a Place Preorder command to the server per the API documentation, with one dessert that is a bestseller (with no open slots), and a valid VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Not enough slots - not bestseller

1. Issue a Place Preorder command to the server per the API documentation, with one dessert that is not a bestseller (with no open slots), and a valid VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing VIP ID

1. Issue a Place Preorder command to the server per the API documentation, with one dessert (with an open slot), and no VIP customer ID
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### VIP customer not found

1. Issue a Place Preorder command to the server per the API documentation, with one dessert (with an open slot), and a VIP customer ID that does not exist in the database
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

## Place Purchase

### Happy path - not gold

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee and one refill, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database

### Happy path - has gold

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee and one refill, and a valid VIP customer ID (which has gold status).
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database

### Happy path - becomes gold

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee, one refill, and enough desserts to give the customer gold status, and a valid VIP customer ID (which does not gold status, but will with the completion of the purchase).
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database

### Happy path - with preorders

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee, one refill, and one dessert (which the customer has an unfulfilled preorder for), a valid VIP customer ID (which has gold status), and an indication that this purchase will fulfill the preorder.
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database
 * Expected: The preorder is marked as fulfilled in the database

### Happy path - with preorders (deferred)

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee, one refill, and one dessert (which the customer has an unfulfilled preorder for), and a valid VIP customer ID (which has gold status).
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database
 * Expected: The preorder is not marked as fulfilled in the database

### Empty purchase

1. Issue a Place Purchase command to the server per the API documentation, containing no purchases, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Zero quantity with other purchases

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee, one refill, and a zero-quantity of a dessert, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "201 Created" response, whose payload is a JSON object containing the purchase information, and a correct total
 * Expected: The purchase is added to the database

### Zero quantity only

1. Issue a Place Purchase command to the server per the API documentation, containing a dessert with zero quantity, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Invalid quantity

1. Issue a Place Purchase command to the server per the API documentation, containing a coffee and a dessert with -1 quantity, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Invalid item

1. Issue a Place Purchase command to the server per the API documentation, containing a coffee and a nonexistent dessert ID with 1 quantity, and a valid VIP customer ID (which does not have gold status).
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### Missing VIP ID

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee and one refill, and no VIP customer ID.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

### VIP customer not found

1. Issue a Place Purchase command to the server per the API documentation, containing one coffee and one refill, and an invalid VIP customer ID.
 * Expected: A "400 Bad Request" response, whose payload is a JSON object containing an "error" key which maps to a human-readable error message.
 * Expected: The database is unchanged

