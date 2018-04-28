# Introduction

This document describes the vision for the Coffee Cart Rewards Management System for Android (hereinafter referred to as "the System").

## Definitions

* **Coffee cart manager**: An employee of LameDucks who will be responsible for the operation of the System.
* **GOLD level**: A state VIP customers can earn by earning enough VIP points, either in a certain period of time, or in total.
* **LameDucks**: Our customer, a business operating various coffee carts in the city of Atlanta.
* **System**: Short name for the Coffee Cart Rewards Management System for Android, the application we have been contracted to develop for LameDucks.
* **Team 04**: The team developing the System.
* **VIP customer**: A customer who has agreed to participate in the loyalty card program at LameDucks.
* **VIP point**: Points earned by a VIP customer when making a purchase. Each dollar a VIP customer spends adds a VIP point to the customer's balance.

# Problem Statement

We have been contracted to develop an Android application for coffee cart managers to use to track customers who have agreed to participate in a loyalty card program. The application should be able to remember personal data for VIP customers, remember purchases by VIP customers, calculate the price for a given purchase, calculate VIP point totals for customers, and track dessert preorders. The users will be coffee cart managers, who are not expected to be overly technical, so the System must be easy to use. The System must also be able to track VIP customers throughout the network of LameDucks coffee carts, so a VIP customer may visit any cart and have her VIP points tracked and rewards available to her.

# Stakeholders and Users

## Stakeholders

| Name      | Represents | Role          |
| --------- | ---------- | ------------- |
| Brad      | LameDucks  | Product Owner |
| Janet     | LameDucks  | Product Owner |
| Casey     | Team 04    | Engineer      |
| David     | Team 04    | Engineer      |
| Jassimran | Team 04    | Engineer      |
| Jeremy    | Team 04    | Engineer      |

## Users

There is only one user archetype for the System, which is that of a coffee cart manager. The coffee cart manager is a nontechnical user who is already familiar with the LameDucks loyalty card program and is given an Android device by LameDucks management. This Android device will have the System preinstalled by LameDucks management, who are familiar with the process to install and update .apk files to Android devices.

Because the Android device is provided by management, the System environment is guaranteed to be stock Android 4.4 (KitKat). No other versions of Android need be supported.

The users will be using the System in a fast-paced business environment in a variety of circumstances. To accommodate entry mistakes, all VIP customer information must be editable and all transactions must be able to be voided.

# Product

## Overview and Features

The System shall consist of two components, an Android application and a server component with which the Android application shall interact. The System shall allow the user to:

* Add and remove VIP customers.
* Edit the personal data of VIP customers.
* Add purchases of the coffee cart, by VIP customers only.
  * Purchases so generated should automatically calculate the purchase price, taking into account VIP status.
* Edit or void previous purchases, in case of user error.
* Add and remove dessert preorders by VIP customers, up to a certain limit (per cart).
* Generate daily purchase and preorder reports.

## Assumptions

* The user will always have a working, charged Android device with the System preinstalled on it, and with a Wi-Fi or data connection available at all times during business hours. Any contingency plan for malfunctioning devices shall be the responsibility of LameDucks management.
* A server will be available on which the server component of the System can be installed.
* All users will have good vision, basic knowledge of using Android devices, and full knowledge of the loyalty card program and operational aspects of the coffee cart itself. No help screens will be developed.
* The devices provided to managers shall be smartphones. No tablet interface shall be developed.
* The devices provided to managers shall have Android 4.4. (KitKat) or newer installed.

## Non-functional Requirements

* The System must be easy to use by cart managers given the assumptions stated in the previous subsection.
* The System must have a responsive UI
  * Actions should not wait for a server response for the UI to update, but instead server communication should occur asynchronously. Server communication errors should be obvious to the user.


