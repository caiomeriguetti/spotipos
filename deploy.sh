#!/bin/bash

function appdir {
  pwd
}

tomcatConfigPath="/etc/tomcat7"
appPath=$(appdir)
tomcatAppsDir="/var/lib/tomcat7"

if [ ! -f $tomcatConfigPath/server.xml.backup ]; then
  sudo cp $tomcatConfigPath/server.xml $tomcatConfigPath/server.xml.backup
fi

sudo cp $appPath/server.xml.template $tomcatConfigPath/server.xml

echo "================ BUILDING ==============="
cd $appPath/backend
sudo mvn clean package
echo "================ DEPLOYING INTO TOMCAT ==============="
sudo mkdir -p $tomcatAppsDir/rest-api/
sudo rm -rf $tomcatAppsDir/rest-api/ROOT.war
sudo rm -rf $tomcatAppsDir/rest-api/ROOT
sudo cp -a target/spotipos-rest-api.war $tomcatAppsDir/rest-api/ROOT.war
sudo service tomcat7 restart
