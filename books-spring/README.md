## Standard Spring Boot REST application

This simple Spring REST service endpoint demonstrates the code needed to handle a REST call and send a kafka message to
the "analytics module" Kafka listener.

### Development Effort

* Spring REST/Http Filter code - Simple
* Spring Kafka - Medium
* Kafka serializer/deserialiser required.
* Spring Integration Test (REST + Kafka) - Harder complex having to deal with Timing issues
* Java config class needed for Kafka Producer setup
* Java Test config class needed for Kafka Test listener setup

