# README #

This app is a simple demo app of list and details.
 
### Technical Approach ###

* The app is a simplified version of the clean architecture (less abstraction).
* Organised into 3 modules
    * Domain: Holds the apps domain logic (mainly getting lists of data). Pure java. Defines it's own dependencies.
    * Data: For accessing data.
    * Presentation: Showing data to the user.
* Uses dagger to wire the dependencies.
* Uses RxJava for handling data flow.
* Everything returns an empty result rather than returning something on the onError. 
If something is coming on onError it's treated as fatal and the chain will break down.
* The presenters are pure java to make them unit testable

### Things to improve ### 
* The Repositories look very similar they should be unified.
* Only if the posts network call fails there is a recovery. For all the others there is no recovery if they fail they will crash the app.
* Caching is very basic. It's an in memory Subject. It can survive rotation but nothing else and it never invalidates it's cache.
* The views(activities and adapter) are not tested they should have espresso tests.
* This app is perfect for shared view transition animation.