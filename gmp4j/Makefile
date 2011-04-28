#JAVA_HOME=/usr/lib/jvm/java-6-sun
JAVA_HOME=/opt/soft/jdk1.6.0_17
INCLUDES=-I $(JAVA_HOME)/include/ -I $(JAVA_HOME)/include/linux/
#CFLAGS=-Wall -shared -static -Wl,-rpath,`pwd`  -g
CFLAGS=-fPIC -Wall -shared -Wl,-rpath,`pwd` -g


all: bin/gmp4j.so bin/gmp4j.jar


bin/us/altio/BigInteger.class: src/us/altio/gmp4j/BigInteger.java
	javac -d bin src/us/altio/gmp4j/*.java

bin/us_altio_gmp4j_BigInteger.h: bin/us/altio/BigInteger.class
	javah -d bin -classpath bin us.altio.gmp4j.BigInteger	

bin/gmp4j.so: bin/us_altio_gmp4j_BigInteger.h
	cc -lgmp src/common.c src/us_altio_gmp4j_BigInteger.c -o bin/libgmp4j.so $(INCLUDES) $(CFLAGS)

bin/gmp4j.jar:
	jar cvf bin/gmp4j.jar -C bin us/altio/gmp4j
	
install:
	sudo cp bin/libgmp4j.so /lib

check:
	java -Djava.library.path=`pwd`/bin

clean:
	-rm -rv bin/*
