// Lexeme.java
// Rye Programming Language
// Jacob Paisley
import java.util.ArrayList;

public class Lexeme {
    protected String type;
    protected String strValue;
    protected int iValue;
    protected double dValue;
    protected boolean bValue;
    protected ArrayList aValue;
    protected Lexeme right;
    protected Lexeme left;
    protected int lineNumber;

    public Lexeme(String type, String data) {
        this.type = type;
        this.strValue = data;
        this.left = null;
        this.right = null;
        this.lineNumber = Main.lineNumber;
    }

    public Lexeme(String type, int iData) {
        this.type = type;
        this.iValue = iData;
        this.left = null;
        this.right = null;
        this.lineNumber = Main.lineNumber;
    }

    public Lexeme(String type, double dData) {
        this.type = type;
        this.dValue = dData;
        this.left = null;
        this.right = null;
        this.lineNumber = Main.lineNumber;
    }

    public Lexeme(String type, boolean bData) {
        this.type = type;
        this.bValue = bData;
        this.left = null;
        this.right = null;
        this.lineNumber = Main.lineNumber;
    }

    public Lexeme(String type) {
        this.type = type;
        this.left = null;
        this.right = null;
        this.lineNumber = Main.lineNumber;
    }

    public Lexeme(String type, ArrayList array) {
        this.type = type;
        this.aValue = array;
        this.lineNumber = Main.lineNumber;
    }
}
