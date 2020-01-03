FROM nginx:alpine

RUN export ips=$(arp -a | cut -d '(' -f2 | cut -d ')' -f1 | grep 192.168); for i in ${ips}; do echo "checking $i"; export response=$(curl http://$i:8888/api/devices --connect-timeout 3); echo "This is the response $response"; if [[ ${response} == *"devices"* ]]; then export API=$i; fi;done; 

RUN sed -i "s/${API}/$API/g" ./ui/main.js

COPY ./ui/ /usr/share/nginx/html