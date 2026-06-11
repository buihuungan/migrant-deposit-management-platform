FROM majiajue/jdk1.8

workdir /

copy ./config /config
copy ./jeecg-boot-module-system/target/jeecg-boot-module-system-2.4.0.jar /jeecg-boot-module-system-2.4.0.jar
copy ./startup.sh /startup.sh
run chmod 755 /startup.sh
run ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo "Asia/Shanghai" > /etc/timezone
entrypoint ["/startup.sh"]
