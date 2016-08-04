#!/bin/bash

appPath=$(pwd)

tomcatConfigPath="/etc/tomcat7"

sudo apt-get install -y software-properties-common python-software-properties
sudo add-apt-repository ppa:webupd8team/java
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv EA312927
sudo apt-get update

#jdk
sudo apt-get install -y oracle-java8-installer
sudo apt-get install -y oracle-java8-set-default

#tomcat7
sudo apt-get install -y tomcat7

#maven
sudo apt-get -y install maven

#redis
sudo apt-get install -y redis-server

#pip
wget https://bootstrap.pypa.io/get-pip.py
sudo python get-pip.py
sudo pip install redis

sudo apt-key update
sudo apt-get update

