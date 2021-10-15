# default value: $1 = topic name, $2 = localhost:9092
docker-compose exec kafka kafka-topics \
  --delete \
  --topic $1 \
  --bootstrap-server $2