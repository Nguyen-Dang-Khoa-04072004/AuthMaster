#! /bin/bash

terminate(){
  docker compose down
  clear
  exit 0
}

trap terminate SIGINT

docker compose up -d

sleep 5
./mvnw spring-boot::run