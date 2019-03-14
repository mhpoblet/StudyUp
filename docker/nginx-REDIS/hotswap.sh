#!/bin/bash
  
set -e
#sed -i "s/server .*:6379/server $1:6379/" ../etc/nginx/nginx.conf
if grep -q $1 ../etc/nginx/nginx.conf
then
        echo "Warning:Same Argument"
else
        sed -i "s/server .*:6379/server $1:6379/" ../etc/nginx/nginx.conf
        exec /usr/sbin/nginx -s reload
fi
