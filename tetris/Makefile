JARPATH := target/scala-2.11/tetris-assembly-0.1.jar
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

echo:
	echo $(SPARK_URL)
	echo $(SPARK_TAR)

spark:
	test -s $(SPARK_TAR) || wget "$(SPARK_URL)"
	test -d $(SPARK_DIR) || tar xzf $(SPARK_TAR)

jar:
	test -s $(JAR) || make assembly
	java -jar $(JAR)

play:
	sbt run

play/rank:
	sbt "run rank"

play/search:
	sbt "run search"

play/contour:
	sbt "run contour"

play/spark:
	$(SPARK_DIR)/bin/spark-submit tetris.jar spark

play/computeContour:
	sbt "run computeContour"