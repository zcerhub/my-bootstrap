#基础镜像
FROM openjdk:8-jre
#应用目录
WORKDIR /app

#将target下的包文件copy到镜像 /app目录， ADD命令会自动解压tar.gz文件
#版本升级时，需要修改为对应的tar.gz文件
ADD target/dap-paas-console-platform-5.1.1-SNAPSHOT-bin.tar.gz ./

#启动容器执行命令
ENTRYPOINT ["java"]
CMD ["-Djava.awt.headless=true","-Djava.net.preferIPv4Stack=true","-server","-Xmx1024m","-Xms1024m","-Xmn512m","-Xss512k","-XX:+DisableExplicitGC","-XX:+UseConcMarkSweepGC","-XX:+CMSParallelRemarkEnabled","-XX:LargePageSizeInBytes=128m","-XX:+UseFastAccessorMethods","-XX:+UseCMSInitiatingOccupancyOnly","-XX:CMSInitiatingOccupancyFraction=70","-classpath","./dap-paas-console-platform-5.1.1-SNAPSHOT/config:./dap-paas-console-platform-5.1.1-SNAPSHOT/lib/*:./dap-paas-console-platform-5.1.1-SNAPSHOT/frontend","com.dap.paas.console.PlatformBootstrap"]

#对外暴露接口
EXPOSE 9080