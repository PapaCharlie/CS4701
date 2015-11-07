JARPATH := target/scala-2.11/tetris-assembly-0.1.jar
PACKAGEPATH := target/scala-2.11/tetris_2.11-0.1.jar
JAR := tetris.jar

SPARK := spark-1.5.1
# SPARK_URL := http://mirrors.gigenet.com/apache/spark/$(SPARK)/$(SPARK).tgz
SPARK_URL := http://mirrors.sonic.net/apache/spark/$(SPARK)/$(SPARK)-bin-hadoop2.6.tgz
SPARK_TAR := $(SPARK)-bin-hadoop2.6.tgz
SPARK_DIR := $(SPARK)-bin-hadoop2.6

all: compile

compile:
	sbt compile

clean:
	sbt clean

assembly:
	sbt assembly
	-rm $(JAR)
	cp $(JARPATH) $(JAR)

package:
	sbt package

spark:
	test -s $(SPARK_TAR) || wget "$(SPARK_URL)"
	test -d $(SPARK_DIR) || tar xzf $(SPARK_TAR)

run:
	sbt run

run/showColors:
	sbt "run showColors"

# run/search:
# 	sbt "run search"

run/contour:
	sbt "run contour"

run/computeRank:
	sbt "run computeRank"