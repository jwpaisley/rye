// Recognizer.java
// Rye Programming Language
// Jacob Paisley
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Recognizer {
    public Lexer lexer;
    public Lexeme currentLexeme;
    Lexeme nextLexeme;
    boolean validFile = true;

    public Recognizer(String fileName) {
        lexer = new Lexer(getFileContents(fileName));
    }

    public void parse() throws Exception {
        currentLexeme = lexer.lex();
        while (currentLexeme.type != "EOF") {
            if (check("DEF")) {
                functionDef();
            } else if (statementPending()) {
                statement();
            } else if (ifExpressionPending()) {
                ifExpression();
            }
            if (validFile) {
                System.out.println("Legal");
            } else {
                System.out.println("Illegal");
            }
        }
    }

    public String getFileContents (String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String contents = "";
            String line;
            while ((line = br.readLine()) != null) {
                contents += line;
            }
            return contents;
        } catch (IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    public void advance() {
        currentLexeme = lexer.lex();
        while (currentLexeme.type == "SPACE" || currentLexeme.type == "COMMENT") {
            currentLexeme = lexer.lex();
        }
    }

    public void match(String type) throws Exception {
        matchNoAdvance(type);
        advance();
    }

    public void matchNoAdvance(String type) throws Exception {
        System.out.println("type " + type +" currentLexeme type: " + currentLexeme.type);
        if (!check(type)) {
            throw new Exception("Syntax error");
        }
    }

    public boolean check(String type) {
        return currentLexeme.type == type;
    }

    public void primary() throws Exception {
        if (varExpressionPending()) {
            System.out.println("variable pending in primary fn");
            varExpression();
        } else if (literalPending()) {
            System.out.println("literal pending in primary fn");
            literal();
        } else if (lambdaCallPending()) {
            System.out.println("lambda call pending in primary fn");
            lambdaCall();
        } else if (varExpressionPending()) {
            varExpression();
        }
    }

    public void lambdaCall() throws Exception {
        match("LAMBDA");
        match("OPAREN");
        paramList();
        match("CPAREN");
    }

    public boolean binaryOperatorPending() {
        return check("PLUS") || check("MINUS") || check("MULT") || check("MULTMULT") || check("COMPARATOR");
    }

    public void binaryOperator() throws Exception {
        if (check("PLUS")) {
            match("PLUS");
        } else if (check("MINUS")) {
            match("MINUS");
        } else if (check("MULT")) {
            match("MULTMULT");
        } else if (check("COMPARATOR")) {
            match("COMPARATOR");
        } else {
            throw new Exception("Syntax Error: expected a binary operator");
        }
    }

    public boolean unaryOperatorPending() {
        return check("UNIOPERATOR");
    }

    public void unaryOperator() throws Exception {
        if (check("PLUSPLUS")) {
            match("PLUSPLUS");
        } else if (check("MINUSMINUS")) {
            match("MINUSMINUS");
        } else {
            throw new Exception("Syntax Error: expected a unary operator");
        }
    }

    public boolean returnPending() {
        return check("RETURN");
    }

    public void returnVal() throws Exception {
        match("RETURN");
        primary();
    }

    public void expression() throws Exception {
        if (primaryPending()) {
            primary();
            if (check("UNIOPERATOR")) {
                match("UNIOPERATOR");
            } else if (check("BINOPERATOR")) {
                match("BINOPERATOR");
                primary();
            }
        } else if (varDefPending()) {
            variableDef();
        } else if (check("ARR")) {
            System.out.println("arr");
            arrayDeclaration();
        }
        else {
            new Exception("Syntax Error: invalid expression");
        }
    }

    public void literal() {
        try {
            if (check("INTEGER")) {
                match("INTEGER");
            } else if (check("DOUBLE")) {
                match("DOUBLE");
            } else if (check("BOOLEAN")) {
                match("BOOLEAN");
            } else if (check("STRING")) {
                match("STRING");
            }
        } catch (Exception e) {
            System.out.println("caught exception in literal fn");
            validFile = false;
        }
    }

    public void body() throws Exception {
        match("OBRACKET");
        statementList();
        match("CBRACKET");
    }

    public void statementList() throws Exception{
        //System.out.println("statement " + currentLexeme.type);
        if (statementPending()) {
            System.out.println("statement pending");
            statement();
            statementList();
        }
    }

    public void lambda() throws Exception {
        match("LAMBDA");
        paramList();
        body();
    }

    public void optExpressionList() throws Exception{
        if (expressionPending()) {
            System.out.println("expression pending");
            expression();
            optExpressionList();
        }
    }

    public void paramList() throws Exception {
        if (primaryPending()) {
            System.out.println("primary pending");
            primary();
            paramList();
        }
    }

    public void variable() throws Exception {
        if (varExpressionPending()) {
            varExpressionPending();
        } else if (expressionPending()) {
            expression();
        } else if (check("VAR")) {
            match("VAR");
        } else {
            throw new Exception("not a valid variable type");
        }
    }

    public boolean varExpressionPending() {
        return check("VAR");
    }

    public void varExpression() throws Exception {
        match("VAR");
        if (check("OPAREN")) {
            match("OPAREN");
            paramList();
            match("CPAREN");
        } else if (check("EQUAL")) {
            match("EQUAL");
            primary();
        } else if (check("OSQUARE")) {
            arrayIndex();
        }
    }

    public boolean literalPending() {
        return (check("INTEGER") || check("STRING") || check("BOOLEAN") || check("DOUBLE"));
    }

    public boolean lambdaPending() {
        return check("LAMBDA");
    }

    public boolean lambdaCallPending() {
        return check("LAMBDA");
    }

    public boolean primaryPending() {
        return literalPending() || varExpressionPending();
    }

    public boolean expressionPending() {
        return primaryPending() || check("VARDEF") || check("RETURN") || check("ARR");
    }

    public boolean variablePending() {
        return check("VAR");
    }

    public void variableDef() throws Exception {
        match("VARDEF");
        match("VAR");
        match("EQUAL");
        expression();
    }

    public boolean paramDecPending() {
        System.out.println("type " + currentLexeme.type);
        return check("VARDEF");
    }

    public void paramDec() throws Exception {
        match("VARDEF");
        System.out.println("CURRENT TYPE " + currentLexeme.type);
        match("VAR");
    }

    public boolean statementPending() {
        return expressionPending() || check("FOR") || check("WHILE") || ifExpressionPending() || printPending();
    }

    public void statement() throws Exception {
        System.out.println("in statement");
        if (check("RETURN")) {
            System.out.println("return");
            match("RETURN");
            System.out.println("return");
            primary();
            System.out.println("return");
            match("SEMI");
        } else if (expressionPending()) {
            System.out.println("stateme nt " + currentLexeme.type);
            expression();
            System.out.println("current lex " + currentLexeme.type);
            match("SEMI");
        } else if (ifExpressionPending()) {
            ifExpression();
        } else if (whilePending()) {
            whileLoop();
        } else if (forPending()) {
            forExpression();
        } else if (printPending()) {
            printCall();
            match("SEMI");
        }

    }

    public void paramDecList() throws Exception {
        if (paramDecPending()) {
            paramDec();
            paramDecList();
        }
    }

    public void functionDef() throws Exception {
        System.out.println("before def");
        match("DEF");
        System.out.println("before var");
        match("VAR");
        System.out.println("before oparen");
        match("OPAREN");
        System.out.println("before paramDecList");
        paramDecList();
        System.out.println("before CPAREN");
        match("CPAREN");
        System.out.println("before body");
        body();
        System.out.println("after body");
    }

    public boolean varDefPending() {
        return check("VARDEF");
    }

    public boolean notPending() {
        return check("NOT");
    }

    public void conditional() throws Exception {
        if (primaryPending()) {
            primary();
            match("COMPARATOR");
            primary();
        } else if (check("NOT")) {
            match("NOT");
            match("OPAREN");
            primary();
            match("COMPARATOR");
            primary();
            match("CPAREN");
        }
    }

    public void ifExpression() throws Exception {
        match("IF");
        match("OPAREN");
        conditionalList();
        match("CPAREN");
        body();
    }

    public boolean ifExpressionPending() {
        return check("IF");
    }

    public boolean elifExpressionPending() {
        return check("ELIF");
    }

    public void elifExpression() throws Exception {
        match("ELIF");
        match("OPAREN");
        conditionalList();
        match("CPAREN");
    }

    public void ifChain() throws Exception {
        ifExpression();
        elifChain();
    }

    public boolean elseExpressionPending() {
        return check("ELSE");
    }

    public void elseExpression() throws Exception {
        match("ELSE");
        body();
    }

    public void elifChain() throws Exception {
        if (elseExpressionPending()) {
            elseExpression();
        } else if (elifExpressionPending()) {
            elifExpression();
            elifChain();
        }
    }

    public boolean conditionalPending() {
        return primaryPending() || check("NOT");
    }

    public boolean whilePending() {
        return check("WHILE");
    }

    public boolean forPending() {
        return check("FOR");
    }

    public void whileLoop() throws Exception {
        match("WHILE");
        match("OPAREN");
        conditional();
        match("CPAREN");
        body();
    }

    public void forExpression() throws Exception {
        System.out.println("in for expression");
        match("FOR");
        match("OPAREN");
        variableDef();
        match("COMMA");
        conditional();
        match("COMMA");
        expression();
        match("CPAREN");
        body();
    }

    public boolean printPending() {
        return check("PRINT");
    }

    public void printCall() throws Exception {
        match("PRINT");
        match("OPAREN");
        primary();
        match("CPAREN");
    }

    public void conditionalList() throws Exception {
        conditional();
        if (check("AND")) {
            match("AND");
            conditional();
            conditionalList();
        } else if (check("OR")) {
            match("OR");
            conditional();
            conditionalList();
        }
    }

    public boolean arrayDeclarationPending() {
        return check("ARR");
    }

    public void arrayIndex() throws Exception {
        match("VAR");
        match("OSQUARE");
        match("INTEGER");
        match("CSQUARE");
    }

    public void arrayDeclaration() throws Exception {
        match("ARR");
        match("OSQUARE");
        match("CSQUARE");
        match("VAR");
        match("EQUAL");
        match("OSQUARE");
        if (primaryPending()) {
            primaryList();
        } else {
            match("INTEGER");
        }
        match("CSQUARE");
    }

    public void primaryList() throws Exception {
        primary();
        if (check("COMMA")) {
            match("COMMA");
            primaryList();
        }
    }

    public static void main(String[] args) throws Exception {
        Recognizer rec = new Recognizer("parseIn.txt");
        rec.parse();
    }
}
