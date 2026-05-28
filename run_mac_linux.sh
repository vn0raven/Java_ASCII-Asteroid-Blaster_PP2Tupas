#!/bin/sh

cd "$(dirname "$0")"

echo "Checking project structure..."

if [ ! -f "src/asteroidgame/app/Main.java" ]; then
  echo ""
  echo "ERROR: Main.java was not found."
  echo "Expected location:"
  echo "src/asteroidgame/app/Main.java"
  echo ""
  echo "Make sure this script is inside the main project folder,"
  echo "the same folder where the src folder is located."
  exit 1
fi

if ! command -v javac >/dev/null 2>&1; then
  echo ""
  echo "ERROR: javac was not found."
  echo "Please install Java JDK first."
  exit 1
fi

echo "Compiling ASCII Asteroid Blaster..."

rm -f sources.txt
rm -rf out
mkdir -p out

find src -name "*.java" > sources.txt

javac -encoding UTF-8 -d out @sources.txt

if [ $? -ne 0 ]; then
  echo ""
  echo "Compilation failed. Please check the Java error messages above."
  exit 1
fi

echo ""
echo "Starting game..."
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main