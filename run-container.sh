#!/bin/bash

URI=${1:-mongodb://experiment:experiment@benchmarking-shard-00-00-annli.mongodb.net:27017,benchmarking-shard-00-01-annli.mongodb.net:27017,benchmarking-shard-00-02-annli.mongodb.net:27017/bare-metal-producer?ssl=true&replicaSet=benchmarking-shard-0&authSource=admin&journal=true&w=1&readConcernLevel=local&readPreference=secondaryPreferred&appName=bare-metal-producer}
MESSAGE_COUNT=${2:-16777216}

CMD="docker run \
            --cpus 1 \
            --env spring_data_mongodb_uri=${URI} \
            --interactive  \
            --name mongodb-producer \
            --network host \
            --rm \
            --tty \
            kurron/mongodb-bare-metal-consumer:latest \
            --number-of-messages-to-read=${MESSAGE_COUNT}"

echo ${CMD}
${CMD}