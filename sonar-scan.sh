#!/bin/bash
export PATH=/opt/apache-maven-3.9.6/bin:$PATH
export JAVA_HOME=/opt/jdk-17.0.2
mvn clean install
mvn clean package sonar:sonar -Dsonar.host.url="http://13.201.33.163:9000" -Dsonar.projectKey=AEM-WEEKEND-7 -Dsonar.projectName='AEM-WEEKEND-7' -Dsonar.token=sqa_a51390374c149203754221835b8232248ed0d2cc
