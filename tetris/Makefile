JAR := "target/scala-2.11/tetris-assembly-0.1.jar"

all:
	sbt compile

run:
	sbt run

clean:
	sbt clean

# assembly takes forever, don't recompile if jar already exists.
jar:
	test -s $(JAR) || sbt assembly

play/rank: jar
	java -jar $(JAR) rank

play/search: jar
	java -jar $(JAR) search