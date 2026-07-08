FROM crpi-kf13onfj0v8f6jax.cn-shanghai.personal.cr.aliyuncs.com/qiantongkeji/eclipse-temurin:17-jre

# 设置 UTF-8 环境（关键！）
ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8

# ====== 代理（仅构建阶段用）======
#ARG http_proxy
#ARG https_proxy
#ENV http_proxy=$http_proxy
#ENV https_proxy=$https_proxy

# 设置时区和应用目录
ENV TZ=Asia/Shanghai \
    APP_HOME=/usr/app/jar

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime \
 && echo $TZ > /etc/timezone \
 && mkdir -p $APP_HOME

WORKDIR $APP_HOME

# 拷贝产物
COPY ./target/*.jar ./qknow.jar

EXPOSE 8090

ENTRYPOINT ["java", "-Xms512m", "-Xmx2g", "-Dspring.profiles.active=prod", "-jar", "qknow.jar"]
CMD ["--server.port=8090"]
