JARPATH := "target/scala-2.11/tetris-assembly-0.1.jar"
JAR := "tetris.jar"

all: compile

compile:
	sbt compile

clean:
	sbt clean

assembly:
	sbt assembly
	ln $(JARPATH) $(JAR)

jar:
	test -s $(JAR) || make assembly
	java -jar $(JAR)

play:
	screen sbt run

play/rank:
	screen sbt "run rank"

play/search:
	screen sbt "run search"

play/contour:
	screen sbt "run contour"
