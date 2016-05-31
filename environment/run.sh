#!/bin/bash

RUNNING_MODE=testing

# update absolute paths in solver app
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

PROPERTIES=$DIR"/katalog/solver/application.properties"
TEACHING_PATTERNS=$DIR"/katalog/teachingPatterns"
PERFECT_MAP=$DIR"/katalog/perfectMap.png"
TESTING_MAP=$DIR"/katalog/testingMap.bsq"
OUTPUT_MAP=$DIR"/katalog/output.bmp"

java -jar $DIR/katalog/solver/solve*.jar --runningMode=$RUNNING_MODE --teachingPatternsDirPath=$TEACHING_PATTERNS --perfectMapPath=$PERFECT_MAP --testingMapPath=$TESTING_MAP --outputMapPath=$OUTPUT_MAP
