#!/bin/sh

if [ -d /fonts ] && [ "$(ls -A /fonts)" ]; then
   cp -a /fonts/* /usr/local/share/fonts/ && fc-cache -f -v
fi

exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /docx-template.jar "$@"
