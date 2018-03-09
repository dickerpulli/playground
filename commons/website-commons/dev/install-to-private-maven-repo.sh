#!/bin/bash -e
# Muss im root-Verzeichnis des Projekts ausgeführt werden

LIB=website-commons-2.0.0
REPO=/Users/thomas/Entwicklung/git/dickerpulli/maven-repo/

# Main Install
mvn clean install

# Main Sources
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dfile=target/$LIB.jar -DpomFile=pom.xml -Dsources=target/$LIB-sources.jar -Djavadoc=target/$LIB-javadoc.jar

# Test Classes
mvn install:install-file -DlocalRepositoryPath=$REPO -DcreateChecksum=true -Dpackaging=test-jar -Dfile=target/$LIB-tests.jar -DpomFile=pom.xml -Dsources=target/$LIB-test-sources.jar -Djavadoc=target/$LIB-test-javadoc.jar

# git
cd $REPO
git add .
git commit -m "new release $LIB"
git push
