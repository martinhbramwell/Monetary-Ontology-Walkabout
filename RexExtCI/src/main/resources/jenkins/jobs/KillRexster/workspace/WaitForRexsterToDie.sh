#!/bin/bash

REXPID=$(ps aux | grep rexster | grep -v grep | awk '{print $2}')
while [ "$REXPID" != ""  ]
do
    echo Still there.
    sleep 5 
    REXPID=$(ps aux | grep rexster | grep -v grep | awk '{print $2}')
done

echo Done

