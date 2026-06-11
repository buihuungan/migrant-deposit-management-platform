FROM majiajue/jdk1.8

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

WORKDIR /eureka

EXPOSE 8080

ADD ./Eureka-Server1-0.0.1-SNAPSHOT-exec.jar ./

CMD sleep 10;java -Djava.security.egd=file:/dev/./urandom -jar Eureka-Server1-0.0.1-SNAPSHOT-exec.jar