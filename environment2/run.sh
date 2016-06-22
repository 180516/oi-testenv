#!/bin/bash

SOLUTION_DIR=$1
DATA_TYPE=$2
RUN_DIR=$(pwd)
cd $SOLUTION_DIR
#java -jar Solution.jar
RES_DIR=$(readlink -e results)
cd $RUN_DIR
cd run
REF_DIR=$(readlink -e test_data/references)
java -jar QualityEvaluation.jar $DATA_TYPE $RES_DIR $REF_DIR
