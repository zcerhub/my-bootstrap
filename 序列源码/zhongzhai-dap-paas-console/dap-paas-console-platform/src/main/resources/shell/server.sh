#!/bin/bash
cd `dirname $0`

# 使用自己的conf文件传递到spring启动脚本中
source ./conf/shell/run.conf

DEPLOY_DIR=`pwd`
CONF_DIR=${DEPLOY_DIR}/conf
SERVER_NAME=dap-console-platform
MAIN_CLASS=com.dap.paas.console.PlatformBootstrap
CLASSPATH="$CONF_DIR:$DEPLOY_DIR/lib/*:$DEPLOY_DIR/classes"
SERVER_PORT=${SERVER_PORT:=9080}
SERVER_URL="http://localhost:$SERVER_PORT"

LOGS_DIR=~/app/logs/${SERVER_NAME}/
if [ ! -d ${LOGS_DIR} ]; then
    mkdir -p ${LOGS_DIR}
fi

PID_DIR=~/app/data/run/
if [ ! -d ${PID_DIR} ]; then
    mkdir -p ${PID_DIR}
fi

export JAVA_OPTS=$JAVA_OPTS" -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -XX:HeapDumpPath=$LOGS_DIR/HeapDumpOnOutOfMemoryError/"
export APP_NAME=$SERVICE_NAME

s_pid=0
checkPid() {
   java_ps=`jps -l | grep $MAIN_CLASS`
   if [ -n "$java_ps" ]; then
      s_pid=`echo $java_ps | awk '{print $1}'`
   else
      s_pid=0
   fi
}

if [ "$(uname)" == "Darwin" ]; then
    windows="0"
elif [ "$(expr substr $(uname -s) 1 5)" == "Linux" ]; then
    windows="0"
elif [ "$(expr substr $(uname -s) 1 5)" == "MINGW" ]; then
    windows="1"
else
    windows="0"
fi

# for Windows
if [ "$windows" == "1" ] && [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]; then
    tmp_java_home=`cygpath -sw "$JAVA_HOME"`
    export JAVA_HOME=`cygpath -u $tmp_java_home`
    echo "Windows new JAVA_HOME is: $JAVA_HOME"
fi

# Find Java
if [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]]; then
    javaexe="$JAVA_HOME/bin/java"
elif type -p java > /dev/null 2>&1; then
    javaexe=$(type -p java)
elif [[ -x "/usr/bin/java" ]];  then
    javaexe="/usr/bin/java"
else
    echo "Unable to find Java"
    exit 1
fi

do_dump(){

    PIDS=`ps -ef | grep java | grep "$DEPLOY_DIR" |awk '{print $2}'`
    if [ -z "$PIDS" ]; then
        echo "ERROR: The $SERVER_NAME does not started!"
        exit 1
    fi

    DUMP_DIR=${LOGS_DIR}/dump
    if [ ! -d ${DUMP_DIR} ]; then
        mkdir -p ${DUMP_DIR}
    fi
    DUMP_DATE=`date +%Y%m%d%H%M%S`
    DATE_DIR=${DUMP_DIR}/${DUMP_DATE}
    if [ ! -d ${DATE_DIR} ]; then
        mkdir -p ${DATE_DIR}
    fi

    echo -e "Dumping the $SERVER_NAME ...\c"
    for PID in $PIDS ; do
        jstack $PID > ${DATE_DIR}/jstack-$PID.dump 2>&1
        echo -e ".\c"
        jinfo $PID > ${DATE_DIR}/jinfo-$PID.dump 2>&1
        echo -e ".\c"
        jstat -gcutil $PID > ${DATE_DIR}/jstat-gcutil-$PID.dump 2>&1
        echo -e ".\c"
        jstat -gccapacity $PID > ${DATE_DIR}/jstat-gccapacity-$PID.dump 2>&1
        echo -e ".\c"
        jmap $PID > ${DATE_DIR}/jmap-$PID.dump 2>&1
        echo -e ".\c"
        jmap -heap $PID > ${DATE_DIR}/jmap-heap-$PID.dump 2>&1
        echo -e ".\c"
        jmap -histo $PID > ${DATE_DIR}/jmap-histo-$PID.dump 2>&1
        echo -e ".\c"
        if [ -r /usr/sbin/lsof ]; then
        /usr/sbin/lsof -p $PID > ${DATE_DIR}/lsof-$PID.dump
        echo -e ".\c"
        fi
    done

    if [ -r /bin/netstat ]; then
    /bin/netstat -an > /netstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/iostat ]; then
    /usr/bin/iostat > ${DATE_DIR}/iostat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/mpstat ]; then
    /usr/bin/mpstat > ${DATE_DIR}/mpstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/vmstat ]; then
    /usr/bin/vmstat > ${DATE_DIR}/vmstat.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/free ]; then
    /usr/bin/free -t > ${DATE_DIR}/free.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/sar ]; then
    /usr/bin/sar > ${DATE_DIR}/sar.dump 2>&1
    echo -e ".\c"
    fi
    if [ -r /usr/bin/uptime ]; then
    /usr/bin/uptime > ${DATE_DIR}/uptime.dump 2>&1
    echo -e ".\c"
    fi

    echo "OK!"
    echo "DUMP: ${DATE_DIR}"

}


case "$1" in
    start)
        checkPid
        if [ $s_pid -ne 0 ]; then
            echo "warn: $SERVER_NAME already started! (pid=$s_pid)"
        else
            echo -n "Starting $SERVER_NAME ..."
            nohup java -classpath $CLASSPATH $MAIN_CLASS >/dev/null 2>&1 &
            declare -i counter=0
            declare -i max_counter=12
            declare -i total_time=0
            until [[ (( counter -ge max_counter )) || "$(curl -X GET --silent --connect-timeout 1 --max-time 2 --head $SERVER_URL | grep "HTTP")" != "" ]];
            do
                printf "."
                counter+=1
                sleep 5
                checkPid
                if [ $s_pid -eq 0 ]; then
                    printf "\n$(date) Server failed to start!\n"
                    exit 1;
                fi
            done
            total_time=counter*5
            if [[ (( counter -ge max_counter )) ]];
            then
                printf "\n$(date) Server failed to start in $total_time seconds!\n"
                exit 1;
            fi
            printf "\n$(date) Server started in $total_time seconds!(pid=$s_pid)\n"
            exit 0;
        fi
    ;;
    stop)
        ps -ef|grep ${SERVER_NAME}|grep -v grep|awk -F ' ' '{print $2}'|xargs kill -9
    ;;
    restart)
        ps -ef|grep ${SERVER_NAME}|grep -v grep|awk -F ' ' '{print $2}'|xargs kill -9
        echo -n "Restarting $SERVER_NAME ..."
            nohup java -classpath $CLASSPATH $MAIN_CLASS >/dev/null 2>&1 &
            declare -i counter=0
            declare -i max_counter=12
            declare -i total_time=0
            until [[ (( counter -ge max_counter )) || "$(curl -X GET --silent --connect-timeout 1 --max-time 2 --head $SERVER_URL | grep "HTTP")" != "" ]];
            do
                printf "."
                counter+=1
                sleep 5
                checkPid
                if [ $s_pid -eq 0 ]; then
                    printf "\n$(date) Server failed to start!\n"
                    exit 1;
                fi
            done
            total_time=counter*5
            if [[ (( counter -ge max_counter )) ]];
            then
                printf "\n$(date) Server failed to start in $total_time seconds!\n"
                exit 1;
            fi
            printf "\n$(date) Server started in $total_time seconds!(pid=$s_pid)\n"
            exit 0;
    ;;
    dump)
        do_dump
    ;;
    *)
        echo "Usage ${0} <start|stop|status|restart|dump>"
        exit 1
    ;;
esac
