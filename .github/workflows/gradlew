#!/usr/bin/env sh

# Resolviendo enlaces simbolicos de ejecucion
PRG="$0"
while [ -h "$PRG" ] ; do
    ls=`ls -ld "$PRG"`
    link=`expr "$ls" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`/"$link"
    fi
done
SAVED="`pwd`"
CDPATH=""
APP_DIR=`dirname "$PRG"`
APP_DIR=`cd "$APP_DIR" && pwd`
cd "$SAVED"

APP_BASE_NAME=`basename "$0"`
CLASSPATH=$APP_DIR/gradle/wrapper/gradle-wrapper.jar

# Ejecucion del wrapper de Gradle
exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
