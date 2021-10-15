# default parameters: $1 = 1, $2 = localhost:9092, $3 = topic name
docker-compose exec kafka  \
  kafka-console-producer --request-required-acks $1 --broker-list $2 --topic $3