#!/bin/bash

# Navigate to the root directory where src is located
cd "$(dirname "$0")" 

# Compile all Java files in the src/bankapp directory
javac src/bankapp/*.java

# Run the Menu class from the bankapp package
java -cp src bankapp.Menu

