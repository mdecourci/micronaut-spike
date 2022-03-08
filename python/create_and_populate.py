import csv
from json import dumps

from kafka import KafkaProducer
from kafka.admin import KafkaAdminClient, NewTopic
from kafka.errors import TopicAlreadyExistsError

BOOTSTRAP_SERVERS = ["localhost:9092"]
TOPIC_NAME = "scans"
TOPIC_NAME1 = "analytics"

admin_client = KafkaAdminClient(bootstrap_servers=BOOTSTRAP_SERVERS,
                                client_id="GoCityTechTest")
kafka_producer = KafkaProducer(bootstrap_servers=BOOTSTRAP_SERVERS,
                               value_serializer=lambda x: dumps(x).encode('utf-8'))


def create_scans_topic():
    try:
        print("Creating scans topic....")
        admin_client.create_topics(new_topics=[NewTopic(name=TOPIC_NAME,
                                                        num_partitions=1,
                                                        replication_factor=1)],
                                   validate_only=False)

    except TopicAlreadyExistsError:
        print("Scans topic already exists. Continuing...")


def populate_scans_topic():
    print("Populating scans topic....")
    scans_count = 0
    # Read from CSV file in the same folder as the script
    with open('scans.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        next(csv_reader, None)  # skip the header row
        for row in csv_reader:
            data = {
                "passNumber": row[0],
                "scanDateTime": row[1],
                "attractionId": row[2]
            }
            kafka_producer.send(TOPIC_NAME, value=data)
            scans_count += 1

    print(f"Populated scans topic with {scans_count} messages")


if __name__ == "__main__":
    create_scans_topic()
    # populate_scans_topic()
