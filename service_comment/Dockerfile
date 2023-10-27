# 基于jdk12的镜像
FROM openjdk:12

# 指定镜像的工作目录
WORKDIR /app

# 将jar服务包复制到进行的当前工作目录中
ADD /target/service_comment-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8023

# 设置环境变量
ARG MY_ENV_VAR
ENV DouyinSecretKey=$MY_ENV_VAR

# 设置时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' > /etc/timezone

# 容器运行时的启动命令
ENTRYPOINT ["java","-jar","/app/service_comment-0.0.1-SNAPSHOT.jar"]