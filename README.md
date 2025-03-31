# Res:Harmonics Technical Test

A smart booking and availability system.

## How to Run

* Install MySQL and run 'dbsetup.sql'.
* From the project root, run 'mvn clean install', and then 'mvn spring-boot:run'.
* Install Postman and import 'rhtechtest.postman_collection' to try out the endpoints.

## To-do

* Add logic to place bookings in an optimised placement strategy.
* Add React/TypeScript UI.
* Add integration tests.
* Use Docker to automate environment setup (e.g. MySQL).