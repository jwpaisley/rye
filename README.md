# Rye
## Running a Program in Rye
To interpret a program written in Rye, use the command 'rye <filename>' or 'dpl <filename> <-x/-r>'. The '-x' flag indicates that you wish to execute the provided Rye source code, while '-r' will simply read the file and print it back to you.

## The End of File (EOF)
A valid Rye program's last line contains the keyword `end`. This is treated as an end-of-file lexeme and is used to signify the end of the program.

## Hello World
Writing the Hello World program is simple. Start with a comment saying what the program will do, such as `/= Hello world! =/`. Next, write the statements that you want your program to accomplish. In the 'Hello World' program, the only statement would be `print("Hello world!");`. Then, to end the program, write the end-of-file keyword, `end`. Putting these three steps together yields the "Hello World" program in Rye, as seen below.

```
/= Hello world! =/
  print("Hello world!");
end
```

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

Arrays can be assigned values at certain indices accessed by assigning a literal to them. An example of this would be `newArray[0] = "test";`.

Values stored in arrays can be accessed using the same indices that were used to store them, which can then be passed to functions or used in assignment. For example, to print the string we stored above, a valid statement would be `print(newArray[0]);`.

## Conditionals
Conditionals on Rye allow the use of `if`, `elif`, `else`, and `while`. Each expect a conditional expression surrounded by parentheses following their keyword and a body for the conditional surrounded by curly brackets.

An example of a valid combination of `if`, `elif`, and `else` statements would be
```
if(x){
  functionX();
}elif(y){
  functionY();
}else{
  functionZ();
}
```

An example of a valid `while` loop would be
```
while(x > 0){
  x = x - 1;
}
```

## Printing to the Console
Printing to the console can be done using the built-in `print()` method. Arguments to the print method can be a literal of any type or the concatenation of two literals. For example, `print("x = " + x);` is a valid print statement, as are `print(true);` and `print("hello world!");`.

## Operators
The operators accepted in Rye include `+`, `-`, `/`, `*`, `==`, `>`, `<`, `>=`, `<=`, `++`, and `--`.

## Anonymous Functions (Lambda)
Lambdas are not yet fully implemented in Rye.
