#!/bin/bash

SOLUTION_DIR=$1
DATA_TYPE=$2
RUN_DIR=$(pwd)
if [ "$DATA_TYPE" == "ls" ]; then
	DATA_DIR=$(readlink -e run/data/ls/test)
else
	DATA_DIR=$(readlink -e run/data/sm/test)
fi
cd $SOLUTION_DIR
java -jar Solution.jar $DATA_DIR $DATA_TYPE
RES_DIR=$(readlink -e results)
cd $RUN_DIR
cd run
REF_DIR=$(readlink -e $DATA_DIR/references)
java -jar QualityEvaluation.jar $DATA_TYPE $RES_DIR $REF_DIR
