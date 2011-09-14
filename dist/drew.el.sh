#!/bin/sh



if [ -n "${JAVA_HOME}" -a -x "${JAVA_HOME}/bin/java" ]; then
  java="${JAVA_HOME}/bin/java"
else
  java=java
fi

if [ -z "${pellet_java_args}" ]; then
  pellet_java_args="-Xmx512m"
fi

#dir=`dirname $0`            # The directory where the script is 
#pushd "$dir" > /dev/null    # Go there
#BASEDIR=$PWD             # Record the absolute path

BASEDIR=/Users/xiao/Dropbox/krrepos/xiao/imps/drew-el/trunk/drew-el/dist

#BASEDIR=/home/xiao/krrepos-xiao/imps/drew-el/trunk/drew-el/dist	

#exec ${java} ${pellet_java_args} -jar lib/pellet-cli.jar "$@"
exec ${java} -cp ${BASEDIR}/drew.el-0.0.1-SNAPSHOT.jar:${BASEDIR}/junit-4.8.2.jar:${BASEDIR}/owlapi-all-3.2.3.jar:${BASEDIR}/DLVwrapper-4.1.jar \
 -DentityExpansionLimit=128000 org.semanticweb.drew.el.cli.DReWELCLI  "$@"
#popd > /dev/null            # Return to previous dir
