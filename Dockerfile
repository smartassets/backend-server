FROM nginx:alpine

COPY ./ui/ /usr/share/nginx/html

RUN cat /usr/share/nginx/html/js/main.js