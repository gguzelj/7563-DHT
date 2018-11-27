#!/bin/bash

# ./run_seeds 2 4
# $1: Cantidad de seeds
# $2: Cantidad de nodos

APP_PATH="controller/target"
JAR=$(echo ${APP_PATH}/*jar)
JAR_ARGS="-Xms100m -Xmx100m"
SEEDS=""

for (( c=8080; c<(8080+$1); c++ ))
do
    SEEDS="$SEEDS,http://127.0.0.1:${c}"
    nohup java -jar ${JAR_ARGS} -Dserver.port=${c} ${JAR} &
    echo $!
done


for (( c= (8080+$1); c<(8080+$2+$1); c++ ))
do
    nohup java -jar ${JAR_ARGS} -Dserver.port=${c} -Ddht.ring.seeds=${SEEDS:1} ${JAR} $>/dev/null &
    echo $!
done
