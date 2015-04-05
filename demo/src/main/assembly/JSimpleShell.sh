#!/bin/bash
#
# This script starts the JSimpleShell-Demo

####
# VARIABLES
####

STARTER_CLASS="de.raysha.lib.jsimpleshell.example.Starter"

SCRIPT_NAME=`basename $0`
UNIX_STYLE_HOME=$(cd `dirname "${BASH_SOURCE[0]}"` && pwd)/`basename "${BASH_SOURCE[0]}"`
UNIX_STYLE_HOME=`echo $UNIX_STYLE_HOME | sed "s/$SCRIPT_NAME//g"`

####
# MAIN
####

CP=""

# Then add all application library jars to the classpath.
for a in "$UNIX_STYLE_HOME"/lib/*; do
	CP="$CP":"$a"
done


java -cp $CP $STARTER_CLASS "$@"
