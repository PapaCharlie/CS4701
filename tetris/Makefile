JARPATH := target/scala-2.11/tetris-assembly-0.1.jar
PACKAGEPATH := target/scala-2.11/tetris_2.11-0.1.jar
JAR := tetris.jar

SBT_URL := https://dl.bintray.com/sbt/native-packages/sbt/0.13.9/sbt-0.13.9.tgz
SBT_TAR := sbt-0.13.9.tgz
SBT := $(shell which sbt 2> /dev/null || echo sbt/bin/sbt)

# TARGS = $(shell cat src/main/scala/tetris/Main.scala | grep -o 'case ".*"' | sed 's,",,g' | grep -oP '\w*$$')

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

computeMap:
	$(SBT) "run computeMap"

showRowClear:
	$(SBT) "run showRowClear"

loadRanks:
	$(SBT) "run loadRanks"

testContour:
	$(SBT) "run testContour"

playRanked:
	$(SBT) "run playRanked"

rankedStatsHeight:
	$(SBT) "run rankedStatsHeight"

rankedStatsLookahead:
	$(SBT) "run rankedStatsLookahead"

tetrominoes:
	$(SBT) "run tetrominoes"

install:
	test -e $(SBT_TAR) || wget $(SBT_URL)
	tar xzf $(SBT_TAR)

publish:
	-rm -r maven
	sbt publish

local:
	sbt publishLocal

update:
	ssh oureatech.com 'sudo mkdir -p /root/maven/'
	-rsync -vrc --progress maven/ oureatech.com:~/maven/
	ssh oureatech.com 'sudo rsync -vrc ~/maven/ /root/maven/'

upgrade: publish update

tar:
	-test -e tetris.tar && rm tetris.tar
	tar cf tetris.tar build.sbt Makefile project/Common.scala project/plugins.sbt src
	scp tetris.tar datafarm:~/tetris/
