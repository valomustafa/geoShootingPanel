#!/bin/bash
sleep 10

today=$(date '+%Y_%m_%d__%H_%M_%S')
cd /home/pi/Developer

if test -f ./app.properties;
then
  . app.properties
else
  echo "$today props" >> /home/pi/Documents/failures.txt
  cp /home/pi/Documents/app.properties /home/pi/Developer/app.properties
  . app.properties
fi

application=ShootingPanel-"$currentVersion"-jar-with-dependencies.jar

if test -f "$application";
then
  sudo chmod 777 "$application"
  sudo java -jar "$application"
else
  echo "$today jar" >> /home/pi/Documents/failures.txt
  cp /home/pi/Documents/"$application" /home/pi/Developer/"$application"
  sudo chmod 777 "$application"
  sudo java -jar "$application"
fi