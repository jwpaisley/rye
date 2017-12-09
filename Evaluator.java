// Evaluator.java
// Rye Programming Language
// Jacob Paisley
import java.util.ArrayList;

public class Evaluator {
    Lexeme e;
    Environment global;

    protected Evaluator(Lexeme tree) {
        Environment e = new Environment();
        this.e = e.create();
        global = e;
        runEvaluator(tree, this.e);
    }

    protected void runEvaluator(Lexeme tree, Lexeme env) {
        eval(tree, env);
    }

    private Lexeme eval(Lexeme tree, Lexeme env) {
        String tree_type = tree.type;
        switch (tree_type) {
            case "functionDef":
                evalFuncDef(tree, env);
                break;
            case "function_call":
                return evalFunctionCall(tree, env);
            case "block":
                return evalBlock(tree, env);
            case "binoperation":
                return evalBinaryOp(tree, env);
            case "unioperation":
                return evalUniOperation(tree, env);
            case "assignment":
                return evalAssingment(tree, env);
            case "variable_definition":
                return evalVariableDef(tree, env);
            case "statementList":
                return evalStatementList(tree, env);
            case "statement":
                return evalStatement(tree, env);
            case "RETURN":
                return evalReturn(tree, env);
            case "PRINT":
                evalPrint(tree, env);
                break;
            case "INTEGER":
                return new Lexeme("INTEGER", tree.iValue);
            case "DOUBLE":
                return new Lexeme("DOUBLE", tree.dValue);
            case "STRING":
                return new Lexeme("STRING", tree.strValue);
            case "BOOLEAN":
                return new Lexeme("BOOLEAN", tree.bValue);
            case "primary":
                return evalPrimary(tree, env);
            case "expression":
                return evalExpression(tree, env);
            case "just_var":
                return evalVar(tree, env);
            case "conditional":
                return  evalConditional(tree, env);
            case "IF":
                return evalIfChain(tree, env);
            case "WHILE":
                return evalWhileLoop(tree, env);
            case "arrayDec":
                return evalArrayDec(tree, env);
            case "arrIndexAssignment":
                return evalArrayIndexAssignment(tree, env);
            case "arrIndex":
                return evalArrayIndex(tree, env);
        }
        return null;
    }

    private void evalFuncDef(Lexeme tree, Lexeme env) {
        Lexeme closure = new Lexeme("closure");
        closure.left = new Lexeme("join");
        closure.left.left = env;
        closure.left.right = tree;
        String fnName = tree.left.strValue;
        global.insert(env, new Lexeme("function_name", fnName), closure);
    }

    private Lexeme evalVar(Lexeme tree, Lexeme env) {
        Lexeme get = global.get(tree.left.strValue, env);
        if (get== null) {
            System.out.println("Error: " + tree.left.strValue + " is undefined.");
            System.exit(0);
        }
        Lexeme value = eval(get, env);
        return value;
    }

    private Lexeme evalFunctionCall(Lexeme tree, Lexeme env) {
        Lexeme closure = global.get(tree.left.strValue, env);
        Lexeme args = getArgList(tree);
        Lexeme params = getParamList(closure.left.right);
        Lexeme body = getClosureBody(closure.left.right);
        Lexeme senv = getStaticEnvironment(closure);
        Lexeme eval_args = evalArgList(args, env);
        Lexeme extended_env = global.extend(params, eval_args, senv);

        return evalBlock(body, extended_env);
    }

    private Lexeme getArgList(Lexeme tree) {
        Lexeme current_lexeme = tree.left.left;
        if(current_lexeme.left != null) {
            Lexeme args = current_lexeme.left;
            current_lexeme = current_lexeme.right;
            while (current_lexeme.left != null) {
                Lexeme temp = args;
                args = current_lexeme.left;
                args.right = temp;
                current_lexeme = current_lexeme.right;
            }
            return args;
        }
        return null;
    }

    private Lexeme getParamList(Lexeme tree) {
        Lexeme current_lexeme = tree.left.left;
        if(current_lexeme.left != null) {
            Lexeme params = current_lexeme.left.left;
            current_lexeme = current_lexeme.right;
            while (current_lexeme.left != null) {
                params.left = current_lexeme.left.left;
                current_lexeme = current_lexeme.right;
            }
            return params;
        }
        return null;
    }

    private Lexeme evalArgList(Lexeme tree, Lexeme env) {
        Lexeme arg = tree;
        Lexeme vals = null;
        while (arg != null) {
            Lexeme temp = vals;
            vals = eval(arg, env);
            vals.right = temp;
            arg = arg.right;
        }
        return vals;
    }

    private Lexeme getClosureBody(Lexeme tree) {
        return tree.left.right.left;
    }

    private Lexeme getStaticEnvironment(Lexeme closure) {
        return closure.left.left;
    }

    private Lexeme evalBlock(Lexeme tree, Lexeme env) {
        Lexeme statement_list = tree.left;
        Lexeme result = evalStatementList(statement_list, env);
        return result;
    }

    private Lexeme evalStatement(Lexeme tree, Lexeme env) {
        return eval(tree.left, env);
    }

    private Lexeme evalStatementList(Lexeme tree, Lexeme env) {
        Lexeme statement_list = tree;
        Lexeme current_statement = tree.left;
        Lexeme result = null;
        while (current_statement != null) {
            result = eval(current_statement, env);
            statement_list = statement_list.right;
            current_statement = statement_list.left;
        }
        return result;
    }

    private Lexeme evalExpression(Lexeme tree, Lexeme env) {
        return eval(tree.left, env);
    }

    private Lexeme evalBinaryOp(Lexeme tree, Lexeme env) {
        String op = tree.left.right.strValue;
        if (op.contentEquals("+")) {
            return evalPlus(tree, env);
        } else if (op.contentEquals("*")) {
            return evalMult(tree, env);
        } else if (op.contentEquals("/")) {
            return evalDivision(tree, env);
        } else if (op.contentEquals("%")) {
            return evalMod(tree, env);
        } else if (op.contentEquals("^")) {
            return evalExponentiation(tree, env);
        } else if (op.contentEquals("-")) {
            return evalMinus(tree, env);
        }
        return null;
    }

    private Lexeme evalPlus(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("INTEGER", left.iValue + right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.dValue + right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", left.iValue + right.dValue);
        } else if (left.type == "STRING" && right.type == "INTEGER") {
            return new Lexeme("STRING", left.strValue + right.iValue);
        } else if (left.type == "INTEGER" && right.type == "STRING") {
            return new Lexeme("STRING", left.iValue + right.strValue);
        } else {
            return new Lexeme("DOUBLE", left.dValue + right.dValue);
        }
    }

    private Lexeme evalMinus(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("INTEGER", left.iValue - right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.dValue - right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", left.iValue - right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE"){
            return new Lexeme("DOUBLE", left.dValue - right.dValue);
        } else {
            System.out.println("Error: cannot subtract types " + left.type + " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalMult(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("INTEGER", left.iValue * right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.dValue * right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", left.iValue * right.dValue);
        } else {
            return new Lexeme("DOUBLE", left.dValue * right.dValue);
        }
    }

    private Lexeme evalDivision(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE",(double) left.iValue / right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.dValue / right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", left.iValue / right.dValue);
        } else {
            return new Lexeme("DOUBLE", left.dValue / right.dValue);
        }
    }

    private Lexeme evalPrimary(Lexeme tree, Lexeme env) {
        return eval(tree.left, env);
    }

    private Lexeme evalExponentiation(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("INTEGER", Math.pow((double) left.iValue, (double) right.iValue));
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", Math.pow(left.dValue, (double) right.iValue));
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", Math.pow((double) left.iValue,  right.dValue));
        } else {
            return new Lexeme("DOUBLE", Math.pow(left.dValue, right.dValue));
        }
    }

    private Lexeme evalMod(Lexeme tree, Lexeme env) {
        Lexeme left = eval(tree.left, env);
        Lexeme right = eval(tree.left.right.left, env);
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.iValue % right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("DOUBLE", left.dValue % right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("DOUBLE", left.iValue % right.dValue);
        } else {
            return new Lexeme("DOUBLE", left.dValue % right.dValue);
        }
    }

    private Lexeme evalUniOperation(Lexeme tree, Lexeme env) {
        String op = tree.left.right.strValue;
        if (op == "++") {
            return evalIncrement(tree, env);
        } else if (op == "--") {
            return evalDecrement(tree, env);
        }
        return null;
    }

    private Lexeme evalIncrement(Lexeme tree, Lexeme env) {
        Lexeme operand = eval(tree.left, env);
        if (operand.type == "INTEGER") {
            return new Lexeme("INTEGER", operand.iValue++);
        } else {
            return new Lexeme("DOUBLE", operand.dValue++);
        }
    }

    private Lexeme evalDecrement(Lexeme tree, Lexeme env) {
        Lexeme operand = eval(tree.left, env);
        if (operand.type == "INTEGER") {
            return new Lexeme("INTEGER", operand.iValue--);
        } else {
            return new Lexeme("DOUBLE", operand.dValue--);
        }
    }

    private Lexeme evalReturn(Lexeme tree, Lexeme env) {
        return eval(tree.left, env);
    }

    private void evalPrint(Lexeme tree, Lexeme env) {
        Lexeme result = eval(tree.left.left, env);
        if (result.type == "STRING") {
            System.out.println(result.strValue);
        } else if (result.type == "BOOLEAN") {
            System.out.println(result.bValue);
        } else if(result.type == "DOUBLE") {
            System.out.println(result.dValue);
        } else if (result.type == "INTEGER") {
            System.out.println(result.iValue);
        } else {
            System.out.println(result.strValue);
        }
    }

    private Lexeme evalAssingment(Lexeme tree, Lexeme env) {
        String varname = tree.left.strValue;
        Lexeme value = eval(tree.left.left, env);
        return global.update(varname, value, env);
    }

    private Lexeme evalVariableDef(Lexeme tree, Lexeme env) {
        Lexeme variable = new Lexeme("VAR", tree.left.left.strValue);
        Lexeme value = eval(tree.left.left.left, env);
        Lexeme insert = global.insert(env, variable, value);
        return insert;
    }

    public void inOrderTraversal(Lexeme tree) {
        if (tree == null) {
            return;
        }
        System.out.println(tree.type);
        inOrderTraversal(tree.left);
        inOrderTraversal(tree.right);
    }

    private Lexeme evalConditional(Lexeme tree, Lexeme env) {
        if (tree.left.type.contentEquals("primary")) {
            return eval(tree.left, env);
        }
        Lexeme comparator = tree.left;
        Lexeme left = eval(tree.left.left, env);
        Lexeme right = eval(tree.left.right, env);
        if (comparator.strValue.contentEquals("<")) {
            return evalLessThan(left, right);
        } else if (comparator.strValue.contentEquals("<=")) {
            return evalLessEqual(left, right);
        } else if (comparator.strValue.contentEquals(">")) {
            return evalGreaterThan(left, right);
        } else if (comparator.strValue.contentEquals("=>")) {
            return evalGreaterEqual(left, right);
        } else if (comparator.strValue.contentEquals("==")) {
            return evalIsEqual(left, right);
        } else if (comparator.strValue.contentEquals("!=")) {
            return evalNotEqual(left, right);
        } else {
            System.out.println("Error: invalid conditional expression.");
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalLessThan(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue < right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue < right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue < right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue < right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalLessEqual(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue <= right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue <= right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue <= right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue <= right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalGreaterThan(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue > right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue > right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue > right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue > right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalGreaterEqual(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue >= right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue >= right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue >= right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue >= right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalIsEqual(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue == right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue == right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue == right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue == right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalNotEqual(Lexeme left, Lexeme right) {
        if (left.type == "INTEGER" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.iValue != right.iValue);
        } else if (left.type == "DOUBLE" && right.type == "INTEGER") {
            return new Lexeme("BOOLEAN", left.dValue != right.iValue);
        } else if (left.type == "INTEGER" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.iValue != right.dValue);
        } else if (left.type == "DOUBLE" && right.type == "DOUBLE") {
            return new Lexeme("BOOLEAN", left.dValue != right.dValue);
        } else {
            System.out.println("Error: cannot compare values of type " + left.type +  " and " + right.type);
            System.exit(0);
            return null;
        }
    }

    private Lexeme evalIfChain(Lexeme tree, Lexeme env) {
        if (eval(tree.left.left, env).bValue) {
            return evalIfExpression(tree, env);
        } else {
            return evalElifChain(tree.right, env);
        }
    }

    private Lexeme evalIfExpression(Lexeme tree, Lexeme env) {
        if (eval(tree.left.left, env).bValue) {
            return evalBlock(tree.left.right, env);
        }
        else return null;
    }

    private Lexeme evalElifChain(Lexeme tree, Lexeme env) {
        Lexeme parent = tree.left;
        if (parent != null) {
            Lexeme elif = tree.left.left;
            while (parent.left != null) {
                if (parent.type.contentEquals("ELSE")) {
                    return eval(tree.left.left, env);
                } else if (parent.left.type.contentEquals("ELSE")) {
                    return eval(parent.left.left, env);
                } else if (eval(elif.left.left, env).bValue) {
                    return eval(elif.left.right, env);
                } else {
                    parent = parent.right;
                }
            }
        }
        return null;
    }

    protected Lexeme evalWhileLoop(Lexeme tree, Lexeme env) {
        Lexeme result = null;
        while (eval(tree.left.left, env).bValue) {
            result = evalBlock(tree.left.right, env);
        }
        return result;
    }

    protected Lexeme evalArrayDec(Lexeme tree, Lexeme env) {
        Lexeme name = new Lexeme("VAR", tree.left.left.strValue);
        ArrayList<Lexeme> array = new ArrayList<>();
        Lexeme value = global.insert(env, name, new Lexeme("ARRAY", array));
        return value;
    }

    protected Lexeme evalArrayIndex(Lexeme tree, Lexeme env) {
        String array_name = tree.left.left.left.strValue;
        ArrayList<Lexeme> array = global.get(array_name, env).aValue;
        int index = eval(tree.left.right, env).iValue;
        if (index >= array.size()) {
            System.out.println("Error: attempted to access an out of range item");
            System.exit(0);
        }
        return array.get(index);
    }

    protected Lexeme evalArrayIndexAssignment(Lexeme tree, Lexeme env) {
        String array_name = tree.left.left.left.strValue;
        ArrayList<Lexeme> array = global.get(array_name, env).aValue;
        int index = eval(tree.left.right.left, env).iValue;
        if (array.size() <= index) {
            for (int i = array.size(); i <= index; i++) {
                array.add(new Lexeme("null"));
            }
        }
        return array.set(index, eval(tree.left.right.right, env));
    }
}
