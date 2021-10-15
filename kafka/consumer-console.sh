# default parameter: $1 localhost:9092, $2 = topic name
docker-compose exec kafka  \
  kafka-console-consumer --bootstrap-server $1 --topic $2 --new-consumer --from-beginning