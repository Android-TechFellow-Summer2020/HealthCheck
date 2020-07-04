# Health Check - Health Questionaiore

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview

### Description
Health questionaire app which could be used to gain access to a building.

### App Evaluation
- **Category:** Productivity
- **Mobile:** Mobile first experience
- **Story:** Allows users to answer health questions to gain access to a building. App provides resources for failure to meet health requirements.
- **Market:** Business and Schools.
- **Habit:** A new QR Code is created everyday so users must use it everyday.Users can possibly gain access to multiple buildings which could leed to multiple uses through out the day.
- **Scope:** Health Check will first start as for one business or scholl but could expand to include multiple insrtitutions.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
* Allow users to sign up for a new account or login to an existing account
* Have a feed that shows users a feed of locations they have been
* Allow users to fill out questionaire 
* Provide resources
* Display QR Code
* Send Notifications

**Optional Nice-to-have Stories**

* Google Maps showing the locations
* Add multiple organizations
* Doctors Note to allow access
* Provide local hospitals and testers in Google Maps

### 2. Screen Archetypes

* Login/Sign 
   * The User will be able to login/Sign up
   * Use Google loging authentication
   * Users will have/create an account
   * Displays navigation bar at the bottom
* Home 
   * User will have the choice to view resources, questionaire, qrcode, or past locations
   * Displays navigation bar at the bottom
* Questionare 
    * User will be prompted with a questionaire about their health
* Location Pages
    * Displays users past locations
    * Displays navigation bar at the bottom
* Resource Page
    * Displays all resources
    * Link to local Hospitals and Testing areas


### 3. Navigation

**Flow Navigation**

* Login / Sign up 
   * Home
* Home 
   * Questionaire
   * Location
   * Resources
   * QR Code Page
* Questionaire 
   * QR Code Page
   * Failure Page
* QR Code Page
   * Home
* Failure Page 
  * Resource Page
* Location 
  * Google Maps
  * Home
* Google Maps 
  * Location
* Resource Page 
  * Home
  

## Wireframes

![barkWireframe](barkWireframe.png)



## Schema 

### Models
#### User 

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userId        | String   | unique id for the user (default field) |
   | userName      | String   | name of the user |
   | locations     | ArrayofString | keep track of locations |
   



