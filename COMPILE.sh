#! /bin/sh
mkdir ./bin
javac -d ./bin/ $(find . -name "*.java")
