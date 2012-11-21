#!/bin/sh

mvn clean
mvn assembly:assembly -Dmaven.test.skip=true 
unzip -o target/drew-*-bin.zip -d target/

