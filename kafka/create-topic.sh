# default value: $1 = localhost:9092, $2 = 1, $3 = 1, $4 = topic name
docker-compose exec kafka kafka-topics \
  --create \
  --bootstrap-server $1 \
  --replication-factor $2 \
  --partitions $3 \
  --topic $4