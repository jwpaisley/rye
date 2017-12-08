// Main.java
// Rye Programming Language
// Jacob Paisley
public class Main {
    protected static void runFile(String filename) {
        // Create a new parser for the file.
        Parser p = new Parser(filename);

        // Attempt recursive parsing and evaluation.
        try {
          Lexeme lexTree = p.parseRecursive();
          new Evaluator(lexTree);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Print the file to the console.
    private static void printFile(String filename) {
      new FileScanner(filename);
    }

    // The main method.
    public static void main(String[] args) {
        String filename = args[0];
        String flag = "-x";

        if(args.length == 2){
          flag = args[1];
        }

        if(flag.contains("r")){
          printFile(filename);
        }

        //
        if(flag.contains("x")){
          runFile(filename);
        }
    }
}
