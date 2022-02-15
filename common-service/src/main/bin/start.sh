JAVA_AGENTS=""

if [ "$APP_ARMS_AGENT" = "true" ]; then
  JAVA_AGENTS="${JAVA_AGENTS} ${ARMS_AGENT} -Darms.licenseKey=${RYS_ARMS_LICENSE_KEY} -Darms.appName=${APP_NAME}"
fi

if [ "$APP_SKYWALKING_AGENT" = "true" ]; then
  JAVA_AGENTS="${JAVA_AGENTS} ${SKYWALKING_AGENT}"
fi

if [ "$APP_JACOCO_AGENT" = "true" ]; then
  JAVA_AGENTS="${JAVA_AGENTS} ${JACOCO_AGENT}"
fi

if [ "x$CFG_EXT" != "x" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Dspring.config.location=/opt/app/application.${CFG_EXT}"
fi

java -server \
  -Djava.security.egd=file:/dev/./urandom \
  -Duser.timezone=GMT+08 \
  ${JAVA_AGENTS} \
  ${JAVA_OPTS} \
  -jar /opt/app/${APP_NAME}.jar


java -server -Djava.security.egd=file:/dev/./urandom -Duser.timezone=GMT+08 -javaagent:/opt/app/skywalking/skywalking-agent.jar -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -Xloggc:/opt/app/logs/gc.log -Xmx4096M -Xms2048M -XX:NewSize=768M -XX:MaxNewSize=768M -XX:SurvivorRatio=8 -jar /opt/app/ims.jar