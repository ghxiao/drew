#!/bin/bash

if [ "$DREWROOT" = "" ]
then
  echo "export DREWROOT"
  exit 1
fi

CP="$($DREWROOT/mkcp $DREWROOT)"

exec java $JVM_ARGS -cp "$CP" -DentityExpansionLimit=128000 org.semanticweb.drew.el.cli.DReWELCLI "$@"

