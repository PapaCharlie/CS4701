JARPATH := target/scala-2.11/tetris-assembly-0.1.jar
PACKAGEPATH := target/scala-2.11/tetris_2.11-0.1.jar
JAR := tetris.jar

SBT_URL := https://dl.bintray.com/sbt/native-packages/sbt/0.13.9/sbt-0.13.9.tgz
SBT_TAR := sbt-0.13.9.tgz
SBT := $(shell which sbt 2> /dev/null || echo sbt/bin/sbt)

all: compile

compile:
	$(SBT) compile

clean:
	$(SBT) clean

assembly:
	$(SBT) assembly
	-rm $(JAR)
	cp $(JARPATH) $(JAR)

package:
	$(SBT) package

run:
	$(SBT) run

run/contour:
	$(SBT) "run contour"

run/serialCompute:
	$(SBT) "run serialCompute"

run/runIterations:
	$(SBT) "run runIterations"

run/colors:
	$(SBT) "run colors"

run/loadMap:
	$(SBT) "run loadMap"

install:
	test -e $(SBT_TAR) || wget $(SBT_URL)
	tar xzf $(SBT_TAR)

tar:
	test -e tetris.tar && rm tetris.tar
	tar cf tetris.tar *
