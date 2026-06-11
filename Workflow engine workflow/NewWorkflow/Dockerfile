FROM majiajue/jdk1.8

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

WORKDIR /newWorkflow

EXPOSE 37192

ADD ./newWorkflow-0.0.1-SNAPSHOT.jar ./

CMD sleep 10;java -Djava.security.egd=file:/dev/./urandom -jar newWorkflow-0.0.1-SNAPSHOT.jar