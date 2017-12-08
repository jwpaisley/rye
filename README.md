# Rye
## Contents

## Basics
## Running a Program in Rye
To interpret a program written in Rye, use the command `rye <filename> (-p)`.
The `-p` flag is optional and will print the file contents to file prior to evaluation.

## The End
A valid Rye program's last line is the keyword `end`.

## Comments
To write a comment in a Rye file, start your comment with `/=` and end it with `=/`. An example of a valid comment in Rye would be `/= This is a comment! =/`. Nothing in a comment is evaluated, so be sure to close the comment. Line comments do not exist in Rye.

## Types
Rye is a dynamically-typed language and does not have explicit typing for variables.

The types that are available are:
- Integer
- String
- Boolean
- Double

## Arrays
Arrays in Rye have constant access time. An array is created by using the keyword `arr`, followed by the name of the array and its assignment to an empty array. An example of a valid array declaration would be `arr newArray = [];`.

Arrays can be assigned values at certain indices accessed by

## Conditionals

## Recursion

## Iteration

## Printing to the Console

## Operators

## Anonymous Functions (Lambda)
