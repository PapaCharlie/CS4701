JARPATH := "target/scala-2.11/tetris-assembly-0.1.jar"
JAR := "tetris.jar"

all: compile

compile:
	sbt compile

clean:
	sbt clean

assembly:
	sbt assembly
	-rm $(JAR)
	cp $(JARPATH) $(JAR)

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
	sbt "run spark"

play/computeContour:
	sbt "run computeContour"