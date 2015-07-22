#!/bin/bash
# Muss im root-Verzeichnis des Projekts ausgeführt werden

LIB=website-services-1.1.1
REPO=/home/thomas/Entwicklung/git/dickerpulli/maven-repo/

# Main Install
mvn install

# Main Sources
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dfile=target/$LIB.jar -DpomFile=pom.xml -Dsources=target/$LIB-sources.jar -Djavadoc=target/$LIB-javadoc.jar

# Test Classes
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dfile=target/$LIB-tests.jar -DpomFile=pom.xml -Dpackaging=test-jar

# git
cd $REPO
git add .
git commit -m "new release $LIB"
git push