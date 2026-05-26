#!/bin/sh
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
if [ $? -ne 0 ]; then
  echo "Compilation failed."
  exit 1
fi
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main
