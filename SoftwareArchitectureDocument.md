# Software Architecture Document


This document describes the design for the Coffee Cart Management System (from here on referred to as the System) at a conceptual level, and provides some insight into the design decisions made.

## Overview

The main method in which a user will interact with the System is through the Android app to be developed as part of the project. The app will have an intuitive and simple to use interface that allows the user to perform all of the activities outlined in the project specification document. The VIP Customer information, inventory, purchase history and preorder history will be stored on a server that all coffee cart locations will have access to. This server will be hosted by Google App Engine.

## Rationale

The key point to the System architecture chosen is the use of a central server for data hosting. This is crucial for providing a consistent experience for both users of the System and customers across all coffee cart locations. It allows the customer to have a single account that is recognized at all locations, as well as having a single set of VIP points being calculated.

Another benefit of using a server for the data management of the System is that it provides for a better level of security and reliability in the event of a mobile device being used by a coffee cart becoming damaged or lost. All data will remain intact in such an occurrence and a minimal impact to daily business activities will be felt.

The desicion to use Google App Engine as the data hosting service was driven by a number of factors.

 - Data redundancy is automatically handled by Google, nearly eliminating the risk of hardware failures affecting the System.
 - All hardware is maintained and run by Google, preventing the customer from having to have the technical resources to manage an internally hosted system.
 - The pricing structure should allow for a minimal overhead cost of keeping the system running. The price is based off of usage, so having a system that is only intended for internal use will keep the recurring costs as low as possible.

## Conceptual View

The following diagram provides a visual representation of how the components of the system will interact with the user. The interaction and user interface presented will be on the Android device. The Android device will retrieve and store data on the server as necessary by the task being performed.

  ![alt text](https://github.com/gt-ud-softeng/cs6300-summer-team04/raw/master/6300Sum14Project2/Deliverable2/SoftwareArchDiagram.gif "Architecture Diagram")
