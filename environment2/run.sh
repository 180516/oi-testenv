#!/bin/bash

DIR=$(pwd)
cd $1
java -jar Solution.jar
OUT=$(readlink -e output.png)
cd $DIR
cd run
java -jar QualityEvaluation.jar $OUT reference.png
