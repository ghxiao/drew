#!/bin/sh

mvn clean install assembly:single -Dmaven.test.skip=true
unzip -o target/drew-*-bin.zip -d target/
