# Health Pass - Health Questionaire

## Table of Contents
1. [Overview](#Overview)
2. [Product Spec](#Product-Spec)
3. [Wireframes](#Wireframes)
4. [Schema](#Schema)
5. [Video Walkthrough](#Video Walkthrough)

## Overview

### Description
This app supports an organization's Coronavirus mitigation efforts by providing a health screening questionaire, location tracking and finding hospitals near you incase of an emergency based on real time statistics (number of beds, ICUs, hospital occupancy, etc). The location tracking feature can be used to support contact tracing.

Once the questionaire is completed, a QR code is generated. When the QR Code is scanned by building security personnel, it contains vital information crucial to let someone in a public place (example: Has been in cotact with someone tested covid-postive, has been abroad within the past 15 days, etc). If the it just says the word "Pass", there is nothing to worry about. Every time the QR code is scanned, the current location of the user is stored in a recycler view (most recent location first). When clicking on any location on the recycler view, it opens up google maps with a pin at that location to see exactly where you were. This can help in contact tracing.

In case of an emergency needing hospital care (covid related or not), users can find nearby hospitals through the app which is displayed on a recycler view. Clicking on any row in the recycler view allows you to view the statistics of the hospital (occupancy, address, number of beds, ICUs, etc) to make an informed decision about which hospital to go to. This is because there can be the possibility that a person goes to a hospital and realizes that all the bed are full. Our app helpd avoid that. It also allows you to open a google maps with a pin on the hospital to see the exact location.
### App Evaluation
- **Category:** Productivity
- **Mobile:** Mobile first experience
- **Story:** Allows users to answer health questions to gain access to a public place/building. Allows for contact tracing and finding nearby hospitals with occupancy statistics.
- **Market:** Business and Schools.
- **Habit:** Users must refill the questionairre every 24 hours to have the most updated information. Users can possibly gain access to multiple buildings which could leed to multiple uses through out the day.
- **Scope:** Can be used by any or all businesses/schools.

## Product Spec

### 1. User Stories (Required and Optional)

User Authentication 
Daily Health Check Questionnaire
QR Code Health Status Display for Facility Entry
Location History Capture
Nearby hospitals list and hospital capacity data

**Optional Nice-to-have Stories**

* Add multiple organizations (Admin authentication of organizations. Certain organizations might want customized questionaire's instead of a generic one.)
* Doctors Note to allow access
* Adding a feature to email results.

### 2. Screen Archetypes

* Login/Sign 
   * The User will be able to login/Sign up
   * Use Google loging authentication
   * Users will have/create an account
* Home 
   * User will have the choice to view resources, questionaire, qrcode, or past locations
* Questionare 
    * User will be prompted with a questionaire about their health
* Location Pages
    * Displays users past locations on a recylcer view
* Resource Page
    * Displays all nearby hospitals in a recylcer view


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
* QR Code Page
   * Home
* Location 
  * Google Maps
  * Home
* Google Maps 
  * Location
* Resource Page 
  * HospitalInformationView
  * Google Maps
* HospitalInformationView
  * Resources
* Google Maps
  * Resources
  

## Wireframes
<img src = 'https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/HealthCheckWireFrameV2.png?raw=true' height = '600px' width='475px'>

## Technology Stack

#### Required
* Firebase
* ZXING QR Code	Library	
* Google Maps API	
* ESRI’s DEFINITIVE HEALTHCARE: 
* Hospital Beds API 
* FCC Locations API

## Schema 

### Models
#### User 

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | userId        | String   | unique id for the user (default field) |
   | userName      | String   | name of the user |
   | answersArray  | ArrayofString | keep track of locations |
#### Answers

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | answerId      | String   | unique id for the location (default field) |
   | coordinates   | String   | location of answers|
   | answer1        | String   | answer for a question|
   | answer2        | String   | answer for a question|
   | answer3        | String   | answer for a question|
   | authorID      | String | keep track of locations |
   
   
## Video Walkthroughs

### Questionaire

<img src='https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/gif1.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Nearby hospitals

<img src='https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/gif2.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### Past Locations

<img src='https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/gif3.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

### QR code generator

Use a scanner app on your phone to see the issues.

<img src='https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/gif4.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />Use a scanner app on your phone to see the issues (It should only say "Pass" there are no issues).<img src='https://github.com/Android-TechFellow-Summer2020/HealthCheck/blob/master/gif5.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
