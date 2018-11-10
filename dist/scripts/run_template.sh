#!/usr/bin/env bash

# Runs @APP_NAME@ by passing up to three arguments.

# Use -DpropsFileName property to accurately point to location where properties reside.

# Default properties file will be created under current directory with name 'server.properties'
# if -DpropsFileName property isn't specified or doesn't point to existing regular file.

java -DpropsFileName=./server.properties -jar libs/@APP_DELIVERY@.jar $1 $2 $3