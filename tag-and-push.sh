#!/bin/bash

# use the time as a tag
UNIXTIME=$(date +%s)

# docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
docker tag mongodbbaremetalconsumer_consumer:latest kurron/mongodb-bare-metal-consumer:latest
docker tag mongodbbaremetalconsumer_consumer:latest kurron/mongodb-bare-metal-consumer:${UNIXTIME}
docker images

# Usage:  docker push [OPTIONS] NAME[:TAG]
docker push kurron/mongodb-bare-metal-consumer:latest
docker push kurron/mongodb-bare-metal-consumer:${UNIXTIME}
