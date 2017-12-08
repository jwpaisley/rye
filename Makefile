make: Environment.class Evaluator.class Lexeme.class Lexer.class Main.class Parser.class Recognizer.class FileScanner.class

Environment.class: Environment.java
	javac -d . -classpath . Environment.java

Evaluator.class: Evaluator.java
	javac -d . -classpath . Evaluator.java

Lexeme.class: Lexeme.java
	javac -d . -classpath . Lexeme.java

Lexer.class: Lexer.java
	javac -d . -classpath . Lexer.java

Main.class: Main.java
	javac -d . -classpath . Main.java

Recognizer.class: Recognizer.java
	javac -d . -classpath . Recognizer.java

Parser.class: Parser.java
	javac -d . -classpath . Parser.java

FileScanner.class: FileScanner.java
	javac -d . -classpath . FileScanner.java

clean:
	rm -f *.class

error1:
	java Main examples/error1.rye -r

error1x:
	java Main examples/error1.rye -x

error2:
	java Main examples/error2.rye -r

error2x:
	java Main examples/error2.rye -x

error3:
	java Main examples/error3.rye -r

error3x:
	java Main examples/error3.rye -x

error4:
	java Main examples/error4.rye -r

error4x:
	java Main examples/error4.rye -x

error5:
	java Main examples/error5.rye -r

error5x:
	java Main examples/error5.rye -x

arrays:
	java Main examples/arrays.rye -r

arraysx:
	java Main examples/arrays.rye -x

conditionals:
	java Main examples/conditionals.rye -r

conditionalsx:
	java Main examples/conditionals.rye -x

recursion:
	java Main examples/recursive.rye -r

recursionx:
	java Main examples/recursive.rye -x

iteration:
	java Main examples/iteration.rye -r

iterationx:
	java Main examples/iteration.rye -x

functions:
	java Main examples/functions.rye -r

functionsx:
	java Main examples/functions.rye -x

lambda:
	java Main examples/lambda.rye -r

lambdax:
	java Main examples/lambda.rye -x

dictionary:
	java Main examples/dictionary.rye -r

dictionaryx:
	java Main examples/dictionary.rye -x

problem:
	java Main examples/problem.rye -r

problemx:
	java Main examples/problem.rye -x
