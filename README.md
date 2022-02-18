# Spike comparing Spring Boot versus Micronaut.

## Spike Test Scenrio

_Gather analytics whenever a book isbn is queried on the REST API._

Http/Servlet filter classes intercept REST url's and sends kafka message to "analytics".

## Components

* "analytics" Micronaut Kafka Listener
* "books" Micronaut REST endpoint and Kafka client
* "books-spring" Spring REST endpoint and Kafka client

### Endpoints:

Book REST service endpoints:

* http://localhost:9080/book/{isbn}  - Micronaut REST API
* http://localhost:9070/book/{isbn}  - Spring Boot REST API

## Setup

* Use Java 16
* cd micronaut-spike/
* mvn clean install
* docker-compose up -d
* cd analytics
* java -jar target/analytics-0.1.jar
* cd ../books
* java -jar target/books-0.1.jar
* cd ../books-spring
* java -jar target/books-0.1.jar

## Timings (Crude)

### Service/JVM Startup

* "books" - Micronaut REST/Kafka client - approx 0.5s
* "analytics" - Micronaut Kafka Listener - approx 2secs
* "books-spring" - Spring boot REST/Kafka client - approx 4secs

# Conclusions

## Micronaut framework

### Pros

* Easier/Quicker to develop
* Can develop alongside SpringBoot
* Faster execution times - Micronaut application compilation, has stages that parse code annotations to generate
  intermediary real java classes.

Avoids runtime latency redirections that annotations bring (See generated intermidiary java classes "$<class name>
$Definition "files in target/classes/. Also parses spring annotations (hybrid/all Spring) if micronaut processor is on
the classpath and the Micronaut application annotation is declared in the main class.

### Cons

* Still new.
