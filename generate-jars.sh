#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# oi-solver
mvn install -DskipTests -f $DIR/oi-solver/pom.xml
cp $DIR/oi-solver/target/solver*.jar $DIR/environment/katalog/solver

# quality-evaluation
#mvn install -DskipTests -f $DIR/QualityEvaluation/pom.xml
#cp $DIR/QualityEvaluation/target/quality-evaluation*.jar $DIR/environment/katalog/quality-evaluation

