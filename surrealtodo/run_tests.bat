@echo off
set SELENIUM = C:/Tools/FuncTesting/selenium-2.7.0/selenium-server-standalone-2.7.0.jar
java -classpath ./bin;%SELENIUM% -DconfigFile=application.properties org.testng.TestNG %*