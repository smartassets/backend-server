function check_error() {
    if [ "$?" != 0 ]; then
        echo "[ERROR] ${1}"
        cd ${2}
        exit 1
    fi
}
working_dir=$1
if [[  -z ${working_dir} ]]; then
    working_dir=${HOME}
else
    mkdir -p ${working_dir}
fi
previous_dir=$(pwd)
curl -s --unix-socket /var/run/docker.sock http://ping > /dev/null
check_error 'Docker is not running' ${previous_dir}

echo "Docker is running proceeding with the setup..."
if [ -d ${working_dir}/iot ]; then
    echo "Project exists - re-using from ${working_dir}/iot"
else
    echo "Cloning into ${working_dir}/iot..."
    cd ${working_dir}
    git clone https://github.com/enchobelezirev/iot.git
    check_error 'Error cloning repository!' ${previous_dir}
fi

cd ${working_dir}/iot
echo "Setting up server..."
kafka_started=$(docker ps -a | grep kafka);
if [[ ${kafka_started} != "" ]]; then
    echo "Docker swarm is running - restarting server..."
    docker-compose restart
else
    docker-compose up -d
fi

check_error 'Error starting server!' ${previous_dir}
echo "Waiting kafka-connect to start..."
while true; do
    kafka_res_status_code=$(curl -I http://localhost:8083/connectors 2> /dev/null | head -1 | cut -d ' ' -f2)
    if [[ ${kafka_res_status_code} == "200" ]]; then
        echo "Kafka-connect started"
        break;
    fi
    if [[ ${kafka_res_status_code} == "500" ]]; then
        echo "[ERROR] Error starting kafka-connect. Check logs: docker logs kafka-connect -f"
        exit 1;
    fi
done

echo "Setting up connectors..."
curl -s -X POST -H 'Content-Type: application/json' http://localhost:8083/connectors -d '{"name":"jdbc-sink-humidity","config":{"connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector","value.converter":"org.apache.kafka.connect.json.JsonConverter","value.converter.schemas.enable":true,"connection.url":"jdbc:postgresql://postgres:5432/postgres","connection.user":"postgres","connection.password":"","auto.create":"true","topics":"humidity"}}' > /dev/null
check_error 'Error setting connector jdbc-sink-humidity!' ${previous_dir}

curl -s -X POST -H 'Content-Type: application/json' http://localhost:8083/connectors -d '{"name":"mqtt-source-humidity-custom","config":{"connector.class":"org.korlenko.kafka.connector.mqtt.connector.MqttSourceConnector","tasks.max":"1","kafka.humidity.topic":"humidity","mqtt.client_id":"mqtt-kafka-power-humidity","mqtt.server_uris":"tcp://mosquitto:1883","mqtt.topic":"humidity","message_processor_class":"org.korlenko.kafka.connector.mqtt.processor.impl.MqttJsonProcessor"}}' > /dev/null
check_error 'Error setting connector mqtt-source-humidity-custom!' ${previous_dir}

curl -s -X POST -H 'Content-Type: application/json' http://localhost:8083/connectors -d '{"name":"mqtt-source-temp-custom","config":{"connector.class":"org.korlenko.kafka.connector.mqtt.connector.MqttSourceConnector","tasks.max":"1","kafka.temperature.topic":"temperature","mqtt.client_id":"mqtt-kafka-power-temp","mqtt.server_uris":"tcp://mosquitto:1883","mqtt.topic":"temperature","message_processor_class":"org.korlenko.kafka.connector.mqtt.processor.impl.MqttJsonProcessor"}}' > /dev/null
check_error 'Error setting connector mqtt-source-temp-custom!' ${previous_dir}

curl -s -X POST -H 'Content-Type: application/json' http://localhost:8083/connectors -d '{"name":"jdbc-sink-temp","config":{"connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector","value.converter":"org.apache.kafka.connect.json.JsonConverter","value.converter.schemas.enable":true,"connection.url":"jdbc:postgresql://postgres:5432/postgres","connection.user":"postgres","connection.password":"","auto.create":"true","topics":"temperature"}}' > /dev/null
check_error 'Error setting connector jdbc-sink-temp!' ${previous_dir}


echo "Setup finished. You can check kafka-connect logs using the command: docker logs kafka-connect -f"
