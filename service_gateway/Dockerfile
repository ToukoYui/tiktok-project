# 基于jdk12的镜像
FROM openjdk:12

# 指定镜像的工作目录
WORKDIR /app

# 将jar服务包复制到进行的当前工作目录中
ADD /target/service_gateway-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8020

# 容器运行时的启动命令
#ENTRYPOINT ["java","-jar","/app/service_gateway-0.0.1-SNAPSHOT.jar","--spring/profiles/active=prod"]
ENTRYPOINT ["java","-jar","/app/service_gateway-0.0.1-SNAPSHOT.jar"]