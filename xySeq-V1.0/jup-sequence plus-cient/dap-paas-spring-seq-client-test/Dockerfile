#基础镜像
FROM  openjdk:8-jre

#应用目录
WORKDIR /app
#将target下的包文件copy到镜像 /app目录， ADD命令会自动解压tar.gz文件
#版本升级时，需要修改为对应的tar.gz文件
#COPY不解压
COPY target/dap-paas-spring-seq-demo-5.0.3-SNAPSHOT.jar ./

#启动容器执行命令
ENTRYPOINT ["java"]
CMD ["-jar","dap-paas-spring-seq-demo-5.0.3-SNAPSHOT.jar"]
#对外暴露接口
EXPOSE 8086