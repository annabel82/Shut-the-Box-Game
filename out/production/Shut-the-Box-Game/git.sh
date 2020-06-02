#!/usr/bin/env bash

if [ ! -f "./commitNo" ] ; then
  # Typically by the time we've commit through this script it's commit no2. 
  var=1
else
  var=`cat ./commitNo`
fi

var=$((var+1))
#DATE=`date '+%d-%b-%Y-%H:%M'`
read -p "Enter commit message: " -e commit

git add -u && git commit -m "Commit${var} - ${commit}" && git push

echo "${var}" > ./commitNo
sleep 2
