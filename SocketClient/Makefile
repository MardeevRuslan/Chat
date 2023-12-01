all: clean package run


package:
	mvn -f pom.xml package

run:
	java -jar target/SocketClient-1.0-SNAPSHOT.jar --server-port=8082


clean:
	mvn -f ./pom.xml clean
