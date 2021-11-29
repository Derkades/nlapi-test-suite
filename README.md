# NamelessMC API Test Suite

## Usage
The API URL should be for a freshly installed website from the `v2` branch. This program may make unwanted changes 
using the API, don't run it on a NamelessMC install you care about.
```sh
mvn package shade:shade
java -jar target/nlapi-test-suite-dev.jar <api url> <enable debug true/false>
```