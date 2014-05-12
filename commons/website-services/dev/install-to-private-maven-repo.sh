#!/bin/bash

mvn install:install-file -DlocalRepositoryPath=/home/thomas/Entwicklung/git/maven-repo/ -DcreateChecksum=true -Dpackaging=jar -Dfile=../target/website-services-1.0.0.jar -DgroupId=de.tbosch.web -DartifactId=website-services -Dversion=1.0.0 -DpomFile=../pom.xml
