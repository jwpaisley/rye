import java.util.ArrayList;

public class Lexeme {
    protected String type;
    protected String strValue;
    protected int iValue;
    protected double dValue;
    protected boolean bValue;
    protected Lexeme right;
    protected Lexeme left;
    protected ArrayList aValue;

    public Lexeme(String type, String data) {
        this.type = type;
        this.strValue = data;
        this.left = null;
        this.right = null;
    }

    /*
    * constructor for an integer lexeme
     */
    public Lexeme(String type, int iData) {
        this.type = type;
        this.iValue = iData;
        this.left = null;
        this.right = null;
    }

    public Lexeme(String type, double dData) {
        this.type = type;
        this.dValue = dData;
        this.left = null;
        this.right = null;
    }

    /*
    * constructor for a boolean lexeme
     */
    public Lexeme(String type, boolean bData) {
        this.type = type;
        this.bValue = bData;
        this.left = null;
        this.right = null;
    }

    /*
    * constructor for a word lexeme
     */
    public Lexeme(String type) {
        this.type = type;
        this.left = null;
        this.right = null;
    }

    /*
    * constructor for array lexeme
     */
    public Lexeme(String type, ArrayList array) {
        this.type = type;
        this.aValue = array;
    }
}
