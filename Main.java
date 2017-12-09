// Main.java
// Rye Programming Language
// Jacob Paisley
public class Main {
    protected static int lineNumber = 1;
    protected static void runFile(String filename) {
        Parser p = new Parser(filename);
        try {
          Lexeme lexTree = p.parseRecursive();
          new Evaluator(lexTree);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void printFile(String filename) {
      new FileScanner(filename);
    }

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
