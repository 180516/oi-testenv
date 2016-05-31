#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mvn install -DskipTests -f $DIR/oi-solver/pom.xml
cp $DIR/oi-solver/target/solver*.jar $DIR/environment/katalog/solver
