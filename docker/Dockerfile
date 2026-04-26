FROM eclipse-temurin:8-jre-jammy

# 创建 app 用户和组，并创建必要的数据目录和日志目录
RUN groupadd -r app && useradd -r -g app app \
    && mkdir -p /data/upload /app/logs \
    && chown -R app:app /data /app

WORKDIR /app
COPY xiaohe-admin.jar app.jar

# 切换到非 root 用户运行
USER app

EXPOSE 8002

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Duser.timezone=Asia/Shanghai -jar /app/app.jar"]