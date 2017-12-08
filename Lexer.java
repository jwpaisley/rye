// Lexer.java
// Rye Programming Language
// Jacob Paisley
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.function.Supplier;

public class Lexer {
    Hashtable keywords;
    int currentIndex = 0;
    int offset = 0;
    char[] characters;

    public Lexer(String fileContents) {
        characters = fileContents.toCharArray();
        initiateKeyWords();
    }

    public void initiateKeyWords() {
        keywords = new Hashtable();
        keywords.put("var", "VARDEF");
        keywords.put("for" , "FOR");
        keywords.put("if", "IF");
        keywords.put("elif", "ELIF");
        keywords.put("else", "ELSE");
        keywords.put("while", "WHILE");
        keywords.put("def", "DEF");
        keywords.put("return", "RETURN");
        keywords.put("or", "OR");
        keywords.put("and", "AND");
        keywords.put("print", "PRINT");
        keywords.put("lambda", "LAMBDA");
        keywords.put("arr", "ARR");
        keywords.put("true", "BOOLEAN");
        keywords.put("false", "BOOLEAN");
        keywords.put("null", "NULL");
    }

    public Lexeme lex() {
        char ch = characters[currentIndex];
        if (ch == '\"') {
            return lexString(characters, currentIndex + 1);
        } else if (isNumeric(ch)) {
            return lexNumber(ch, characters, currentIndex + 1, false);
        } else if (ch == '-') {
            if (isNumeric(characters[currentIndex + 1])) {
                currentIndex += 1;
                ch = characters[currentIndex];
                return lexNumber(ch, characters, currentIndex + 1, true);
            } else {
                currentIndex += 1;
                return new Lexeme("BINOPERATOR", "" + ch);
            }
        } else if (ch == ';') {
            currentIndex += 1;
            return new Lexeme("SEMI");
        } else if (ch == ',') {
            currentIndex += 1;
            return new Lexeme("COMMA");
        } else if (ch == '(') {
            currentIndex += 1;
            return new Lexeme("OPAREN");
        } else if (ch == ')') {
            currentIndex += 1;
            return new Lexeme("CPAREN");
        } else if (ch == '{') {
            currentIndex += 1;
            return new Lexeme("OBRACKET");
        } else if (ch == '}') {
            currentIndex += 1;
            return new Lexeme("CBRACKET");
        } else if (ch == '[') {
            currentIndex += 1;
            return new Lexeme("OSQUARE");
        } else if (ch == ']') {
            currentIndex += 1;
            return new Lexeme("CSQUARE");
        } else if (ch == 'e' && characters[currentIndex + 1] == 'n' && characters[currentIndex + 2] == 'd') {
            return new Lexeme("EOF");
        } else if (ch == '/') {
            if (characters[currentIndex + 1] == '=') {
                currentIndex += 1;
                return getComment();
            }
        } else if (ch == '%' || ch == '/') {
            currentIndex += 1;
            return new Lexeme("BINOPERATOR", "" + ch);
        } else if (ch == '!') {
            if (characters[currentIndex + 1] == '=') {
                currentIndex += 2;
                return new Lexeme("COMPARATOR","!=");
            }
        }
        else if (ch == '<') {
            if (characters[currentIndex+1] == '=') {
                currentIndex += 2;
                return new Lexeme("COMPARATOR","<=");
            } else {
                currentIndex += 1;
                return new Lexeme("COMPARATOR", "<");
            }
        } else if (ch == '>') {
            currentIndex += 1;
            return new Lexeme("COMPARATOR", ">");
        } else if (ch == '^') {
            currentIndex += 1;
            return new Lexeme("BINOPERATOR", ch);
        } else if (ch == '=') {
            if (characters[currentIndex + 1] == '>' || characters[currentIndex + 1] == '=') {
                char value = characters[currentIndex + 1];
                currentIndex = currentIndex + 2;
                return new Lexeme("COMPARATOR", "=" + value);
            } else {
                currentIndex += 1;
                return new Lexeme("EQUAL");
            }
        }  else if (ch == '*' || ch == '+') {
            return checkChar(characters, currentIndex + 1, ch);
        } else if (ch == ' ') {
            currentIndex += 1;
            return new Lexeme("SPACE");
        } else if (isAlpha(ch)) {
            return lexAlpha(characters, currentIndex);
        }

        return null;
    }

    public Lexeme lexAlpha(char[] chars, int index) {
        int i = index;
        char ch = chars[index];
        String buffer = "";
        while (ch != ' ' && i <= chars.length && (isNumeric(ch) || isAlpha(ch))) {
            buffer += ch;
            if (keywords.containsKey(buffer)) {
                offset = buffer.length() - 1;
                currentIndex += offset + 1;
                if (keywords.get(buffer).toString() == "VARDEF") {
                    return new Lexeme(keywords.get(buffer).toString(), buffer);
                }
                return new Lexeme(keywords.get(buffer).toString());
            }
            i = i + 1;
            ch = chars[i];
        }
        if (characters[currentIndex] == '.') {
            currentIndex += 1;
            String newBuff = "";
            ch = characters[currentIndex];
            while (ch != ' ' && i <= chars.length && (isNumeric(ch) || isAlpha(ch))) {
                newBuff += ch;
                currentIndex += 1;
            }
            if (newBuff.contentEquals("add")) {
                System.out.println("arrayAdd");
                return new Lexeme("arrayAdd", buffer + newBuff);
            }
        }
        offset = buffer.length() - 1;
        currentIndex += offset + 1;
        return new Lexeme("VAR", buffer);
    }

    public Lexeme checkChar(char[] chars, int index, char ch) {
        int i = index;
        if (chars[i] == ch) {
            offset = 1;
            currentIndex += offset + 1;
            return new Lexeme("UNIOPERATOR", "" + ch + ch);
        } else if (chars[i] == ' ') {
            offset = 0;
            currentIndex += 1;
            return new Lexeme("BINOPERATOR", "" + ch);
        } else {
            return new Lexeme("BAD CHAR");
        }
    }

    public boolean isAlpha(char ch) {
        return (((int) ch >= 65 && (int) ch <=90) || ((int) ch >= 97 && (int) ch <= 122));
    }

    public Lexeme getComment() {
        String buffer = "";
        currentIndex += 1;
        while (!((characters[currentIndex] == '=') && (characters[currentIndex + 1] == '/'))) {
            buffer += characters[currentIndex];
            currentIndex = currentIndex + 1;
        }
        currentIndex += 2;

        return new Lexeme("COMMENT", buffer);
    }

    public boolean isNumeric(char ch) {
        return (int) ch >= 48 && (int) ch <= 57;
    }

    public Lexeme lexString(char[] chars, int index) {
        String buffer = "";
        int i = index;
        char ch = chars[i];
        while (ch != '\"') {
            buffer += ch;
            i = i + 1;
            ch = chars[i];
        }
        offset = buffer.length() - 1;
        currentIndex += offset + 3;
        return new Lexeme("STRING", buffer);
    }

    public Lexeme lexNumber(char ch, char[] chars, int index, boolean is_negative) {
        int i = index;
        String buffer;
        if (is_negative) {
            buffer = "-" + ch;
        } else {
            buffer = "" + ch;
        }
        ch = chars[i];
        boolean isDouble = false;

        while (ch != ' ' && i <= chars.length) {
            if (ch == '.') {
                isDouble = true;
                buffer += ch;
            } else if (isNumeric(ch)) {
                buffer += ch;
            } else {
                break;
            }
            i = i + 1;
            ch = chars[i];
        }

        if (is_negative) {
            offset = buffer.length() - 2;

        } else {
            offset = buffer.length() - 1;
        }
        currentIndex += offset + 1;
        if (isDouble) {
            return new Lexeme("DOUBLE", Double.parseDouble(buffer));
        } else {
            return new Lexeme("INTEGER", Integer.parseInt(buffer));
        }
    }

}
