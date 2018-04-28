## Risk Assessment for Coffee Cart Rewards Management System

This document details the risk assessment for the Coffee Cart Rewards Management System for Android (hereinafter referred to as "the System"). It is an attempt to identify as many risks as possible for the successful development and deployment of the System, in order to allow for a more accurate understanding of the feasibility of the design and to help plan any risk mitigation strategies ahead of time.

## Risk Level Definitions

 - **Low** - Very unlikely that this will occur during development 
 - **Medium** - There is a 50-50 chance that this will occur during development
 - **High** - Very likely that this will occur during development

Any potential risk given a 'High' rating should have a corresponding plan of mitigation and control.


# Design Risks

 - **Use of a central server**
    The project specification does not explicitly call out the use of a server in the design of the System. It does state that each customer should "have only one VIP card", which strongly implies the use of a server since the System is to support carts at multiple locations. The use of a server will require a certain level of on-going support and maintenance, and the customer will need to be aware that this presents a recurring business expense. On the other hand, failing to implement a server creates the risk of delivering a product to the customer that does not meet their expectations. 
     - Risk rating: **High**
     - Mitigation strategy: Implement the System using a centralized server for data hosting.

 - **Server availability**
    Using a server to host the data presents the possibility of communications going down and the System being unable to operate as intended
     - Risk rating: **Low**

 - **Data retention**
    Having the data for the System centrally located results in a risk of data loss in the event of catastrophic hardware failure.
     - Risk rating: **Low**

 - **System security** 
   Having the access points to the system located on mobile devices creates a risk of loss or theft, which could compromise the security of the system.
     - Risk rating: **Low**

 - **Owner Unavailability**
   Ambiguities in the requirements exist which must be addressed by the customer. Unavailability of the customer to meet to provide appropriate requirements could result in the wrong system being delivered.
     - Risk rating: **Low**
 
 - **Ambiguous Requirements**
   Ambiguities in the requirements exist and may not be recognized. We may develop a system that is different than what the customer wants.
     - Risk rating: **Medium**
 
 - **Unanticipated Future Requirements**
   The owners may want to extent the functionality of the app in the future in unanticipated ways. Our design may not be able to easily accommodate any unanticipated future requirements.
     - Risk rating: **Medium**
 
# Execution Risks

 - **Use of a central server**
    This item also presents an execution risk to project in that it will greatly effect the complexity of the design. The Android app will have to handle all of the scenarios of connection difficulties and failures in as graceful manner as possible. It would be less than ideal if server down-time lead to a completely non-functioning system.
     - Risk rating: **Medium**

 - **On-time delivery of product**
   Adding a server to the project creates a much higher level of complexity and increases the number of hours required for development. This creates a certain amount of risk in delivering a complete product on schedule.
     - Risk rating: **Low**

 
# Testing Risks

 - **Testing hardware**
    Will the customer provide the hardware for performing testing on the final product? Or will unit testing and system simulation be adequate for this project?
     - Risk rating: **Low**
