# README #

This app is a simple demo app of list and details.
 
### Technical Approach ###

* The app is a simplified version of the clean architecture (less abstraction).
* Organised into 3 modules
    * Domain: Holds the apps domain logic (rate polling). Pure java. Defines it's own dependencies.
    * Data: For accessing data
    * Presentation: Showing data to the user
* Uses dagger to wire the dependencies
* Uses RxJava for handling data flow

### Things to improve ### 
* The Repositories look very similar they should be unified