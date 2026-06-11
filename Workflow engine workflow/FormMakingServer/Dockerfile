FROM majiajue/jdk1.8

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

WORKDIR /fmaking

EXPOSE 8025

ADD ./fmaking-0.0.1-SNAPSHOT.jar ./

CMD sleep 10;java -Djava.security.egd=file:/dev/./urandom -jar fmaking-0.0.1-SNAPSHOT.jar