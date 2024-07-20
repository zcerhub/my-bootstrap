#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=${DEPLOY_DIR}/config
LOGS_FILE=""
SERVER_NAME=dap-paas-spring-seq-server
LOGS_DIR=""
SERVER_PORT=`sed '/server.port/!d;s/.*=//' $CONF_DIR/application.properties | tr -d '\r'`
MAIN_CLASS=com.dap.sequence.server.SequenceServerApplication

if [[ -n "$LOGS_FILE" ]]; then
    LOGS_DIR=`dirname ${LOGS_FILE}`
else
    LOGS_DIR=${DEPLOY_DIR}/logs
fi
if [[ ! -d ${LOGS_DIR} ]]; then
    mkdir ${LOGS_DIR}
fi
STDOUT_FILE=${LOGS_DIR}/stdout.log

LIB_DIR=${DEPLOY_DIR}/lib
LIB_JARS=`ls ${LIB_DIR}|grep .jar|awk '{print "'${LIB_DIR}'/"$0}'|tr "\n" ":"`

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_DEBUG_OPTS=""
if [[ "$1" = "debug" ]]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n "
fi
JAVA_JMX_OPTS=""
if [[ $1 = "jmx" ]]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
JAVA_MEM_OPTS=""
BITS=`java -version 2>&1 | grep -i 64-bit`
if [[ -n ${BITS} ]]; then
    JAVA_MEM_OPTS=" -server -Xmx1024m -Xms1024m -Xmn512m  -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
else
    JAVA_MEM_OPTS=" -server -Xms512m -Xmx512m  -XX:SurvivorRatio=2 -XX:+UseParallelGC "
fi

echo -e "Starting the ${SERVER_NAME} ...\c"
nohup java ${JAVA_OPTS} ${JAVA_MEM_OPTS} ${JAVA_DEBUG_OPTS} ${JAVA_JMX_OPTS} -classpath ${CONF_DIR}:${LIB_JARS} ${MAIN_CLASS} > ${STDOUT_FILE} 2>&1 &

COUNT=0
NUM=0
TOTAL=36
while [[ ${COUNT} -lt 1 ]]; do
    echo -e ".\c"
    sleep 1 
    if [[ -n "$SERVER_PORT" ]]; then

        COUNT=`netstat -an | grep ${SERVER_PORT} | wc -l`
    else
    	COUNT=`ps -ef | grep java | grep ${DEPLOY_DIR} | awk '{print $2}' | wc -l`
    fi
    NUM=`expr ${NUM} + 1`
    if [[ ${NUM} -eq ${TOTAL} ]]; then
        printf "\n"
        echo "[ERROR]: 应用[${SERVER_NAME}]在${TOTAL}s内未能成功启动，请手工检查！！"
        break
    fi
    if [[ ${COUNT} -gt 0 ]]; then
        echo "OK!"
        PIDS=`ps -ef | grep java | grep ${DEPLOY_DIR} | awk '{print $2}'`
        echo "PIDS:$PIDS"
        break
    fi
done

echo "STDOUT: ${STDOUT_FILE}"
