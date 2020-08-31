# Load Master
********************************************************************************
                                                 L O A D   M A S T E R                              
                                               P r o j e c t   N o t e s                            
********************************************************************************

The Load Master Project is the culmination of a bunch of different attempts and failures to try and bring about a simple load tracking and accounting package for one-truck owner/operator businesses. I believe that I finally have figured out exactly what I want this system to do and how to do it. The next step is to actually implement my ideas into code and see what comes of it. This file contains the overview of how things should work...

1. Basic Overview
-----------------
The typical way that a one-truck owner/operator runs freight is as follows:
    
    1. Locates a load by whatever means the owner uses.
    2. Books the load.
    3. Makes the pick-up(s).
    4. Makes the delivery(ies).
    5. Completes the load paperwork.
    6. Submits the load paperwork, including the proof of delivery (POD) for
       payment on the load.
    7. Receives a settlement for the load (after submitting the load paperwork,
       including an invoice, if necessary).
    8. Distributes settlement to multiple expense and asset accounts for his/her
       accounting system.
    9. Repeats the process for the next load.

In this general overview, there are other details, such as arrival/departure notices for each stop on the load. These types of activities will be included within the work flow of Load Master. Other details that could take place during a typical load, include:

    a. Arriving at a pick-up.
    b. Departing from a pick-up.
    c. Taking an advance on payment of the load.
    d. Arriving at a delivery.
    e. Departing from a delivery.
    f. Closing a load.

During the course of a load, the owner/operator may:

    i. Fuel up the truck.
   ii. Replace tires on the truck and/or trailer.
  iii. Have a breakdown and repair.
   iv. Have PM service performed.

With all of this in mind, it would be nice to be able to have some of the accounting automated to place the above processes directly into the accounting system at the time they are performed. Therefore, Load Master will not be in the least a standard accounting or load tracking system. Instead, Load Master will have very few functions that are based on how owner/operators run the course of their business. The way Load Master will handle all of this is:

    A. Book the load.
        This feature will allow the owner/operator to actually enter the details of the load they are preparing to run. This wizard will allow for the entry of all of the load details, such as trip and order numbers, pick-up and delivery customers (unlimited in the number of stops on the load), gross pay for the load, weight of the load's freight, number of miles on the load, piece count of the load, BOL number, as well as various other load-related information.

    B. Arrive/Depart Stops.
        This feature will allow the driver to track his/her status on the load by letting the system know when they have arrived and departed the stops that make up the load. This information will update the load progress bar on the bottom of the window in the status area.

    C. Enter Fuel Purchase.
        This feature will allow the driver to enter a fuel purchase at any time, whether currently on a booked load or not. The fuel purchase requires various information regarding the purchase, such as the number of gallons purchase, the price per gallon, diesel or DEF, whether or not it is a partial fill-up, location of the purchase, odometer reading at the time of the purchase, etc. This information allows the system to track the fuel expense for the truck and be able to provide the fuel economy and expense per mile.

    D. Enter Service Purchase.
        This feature allows the driver to enter the purchase of a service or repair on the truck or trailer. This type of a purchase entry, like the fuel purchase, may be entered whether or not the driver is currently on a booked load and also requires certain information, such as: odometer reading, type of service or repair, component system to which the service or repair belongs, date of the service or repair, etc. By providing as much data as is practical, the system will be able to track the cost per mile of services/repairs.

    E. Enter Other Purchases.
        This feature allows the driver to enter any other, uncategorized, purchase, whether or not currently running on a booked load. By using this feature, the accounting system behind Load Master will be able to assist the owner/operator with budgeting for their business by showing where their money comes from and where it goes. The more purchases that are tracked in this system, the better understanding of the cash flow of the business can be had.

    F. Close Load.
        This feature simply provides a means by which the owner/operator can let the system know that a load has been completed. The Close Load feature should only be used once a load has been totally completed, including sending in the POD/Signed BOL. If the system settings have the company set up as independent, then closing the load will print a detailed invoice for the load.

    G. Settle Load.
        This feature allows the owner/operator to split his/her load settlement into as many accounts as necessary. For example, if the O/O is leased to a carrier, then the settlement sheet s/he receives once the load is complete will have a breakdown of how much was retained by the carrier to which they are leased for, but not limited to:

            - Bobtail insurance
            - Tractor liability insurance
            - Trailer liability insurance
            - QualComm/PeopleNet fees
            - Advances
            - Advance fees
            - Baseplates
            - Electronic bank deposit
            - Reserve account deposit

        The Settle Load feature allows the driver to split the gross load payment out to as many accounts as necessary, which allows the accounting system to be able to accurately break down every expense and revenue to a per mile value. Using the Settle Load feature, like all of the Purchase Entry features, allows the owner/operator to have the best information available to know the financial health of their business.
