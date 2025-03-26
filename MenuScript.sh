#!/bin/bash

# Navigate to the root directory where src is located
cd "$(dirname "$0")" || exit

# Compile all Java files in the bankapp package
javac src/bankapp/*.java

# Run the main BankApp class from the bankapp package
java -cp src bankapp.BankApp
