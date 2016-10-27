#!/bin/bash
# Muss im root-Verzeichnis des Projekts ausgeführt werden

LIB=website-parent-1.1.0
REPO=/home/thomas/Entwicklung/git/dickerpulli/maven-repo/

# Main Install
mvn clean install

# Main Sources
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dfile=pom.xml -DpomFile=pom.xml

# git
cd $REPO
git add .
git commit -m "new release $LIB"
git push