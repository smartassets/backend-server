#!/bin/sh

export ips=$(arp -a | cut -d '(' -f2 | cut -d ')' -f1 | grep 192.168);
for i in ${ips}; do 
    echo "checking $i"; 
    export response=$(curl http://$i:8888/api/devices --connect-timeout 3); 
    echo "This is the response $response"; 
    if [[ ${response} == *"devices"* ]]; then 
        export API=$i;
        echo "lalalalalallaalla"
        sed -i '' -e "s/DEVICES_API/$i/g" ${PWD}/ui/js/main.js; 
        echo "Setting configuration for the devices..."
        local_ip=$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1');
        curl --header "Content-Type: application/json" \
            --request POST \
            --data "{\"mqttserver\":\"${local_ip}\"}" \
            http://$i:8888/api/config

    fi; 
done;

is_replaced=$(cat ${PWD}/ui/js/main.js | grep 'DEVICES_API')
if [[ ! -z ${is_replaced} ]]; then
    exit 1;
fi;