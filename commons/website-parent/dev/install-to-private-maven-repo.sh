#!/bin/bash -e
# Muss im root-Verzeichnis des Projekts ausgeführt werden

REPO=/Users/thomas/Entwicklung/git/dickerpulli/maven-repo/

# Version ermitteln
VERSION=`mvn exec:exec -Dexec.executable='echo' -Dexec.args='${project.version}' -q`
ARTIFACTID=`mvn exec:exec -Dexec.executable='echo' -Dexec.args='${project.artifactId}' -q`
LIB=$ARTIFACTID-$VERSION

# Main Install
mvn clean install

# Main Sources
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dfile=pom.xml -DpomFile=pom.xml

# git
cd $REPO
git add .
git commit -m "new release $LIB"
git push
