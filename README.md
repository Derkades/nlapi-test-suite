# NamelessMC API Test Suite

## Usage

The API URL should be for a freshly installed website from the `v2` branch. This program may make unwanted changes using
the API, don't run it on a NamelessMC install you care about.

```sh
mvn package
export NAMELESS_API_URL="https://yoursite.com/index.php?route=/api/v2"
export NAMELESS_API_KEY="insert_api_key"
# export NAMELESS_DEBUG=1
# export NAMELESS_EXIT_ON_FAIL=1
java -jar target/nlapi-test-suite-dev.jar
```
