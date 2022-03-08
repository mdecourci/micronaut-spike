FROM adoptopenjdk/openjdk16

RUN mkdir -p /opt/app

ENV PROJECT_HOME /opt/app

ENV KAFKA_PORT 19092
ENV KAFKA_PRODUCER_HOST 'kafka'
ENV KAFKA_PRODUCER_URL $KAFKA_PRODUCER_HOST':'$KAFKA_PORT

ENV JAVA_OPTS '-Dkafka.bootstrap.servers='$KAFKA_PRODUCER_URL

EXPOSE 9080

COPY target/books-0.1.jar /opt/app/books.jar

WORKDIR $PROJECT_HOME

ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app/books.jar