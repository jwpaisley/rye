// Environment.java
// Rye Programming Language
// Jacob Paisley
public class Environment {
    protected Lexeme addLocalVariable(Lexeme env, Lexeme id, Lexeme value) {
        Lexeme tempVars = env.left;
        Lexeme tempVals = env.right;
        Lexeme nextEnv = env.right.right;
        env.left = id;
        id.left = tempVars;
        env.right = value;
        env.right.left = tempVals;
        env.right.right = nextEnv;
        return value;
    }

    protected Lexeme setCar(Lexeme envir, Lexeme value) {
        value.left = envir;
        return value;
    }

    protected Lexeme setCdr(Lexeme envir, Lexeme value) {
        envir.right = value;
        return envir;
    }

    protected Lexeme get(String id, Lexeme env) {
        if (env == null) {
            return null;
        }
        Lexeme currentVar = env.left;
        Lexeme currentVal = env.right.left;
        while(currentVar != null) {
            if (currentVar.strValue.contentEquals(id)) {
                return currentVal;
            }
            currentVal = currentVal.right;
            currentVar = currentVar.left;
        }
        return get(id, env.right.right);
    }

    protected Lexeme update(String id, Lexeme value, Lexeme env) {
        if (env == null) {
            return null;
        }
        Lexeme currentVar = env.left;
        Lexeme currentVal = env.right.left;
        if (currentVar.strValue.contentEquals(id)) {
            Lexeme temp = currentVal.left;
            env.right.left = value;
            value.right = temp;
            return value;
        }
        Lexeme nextVar = env.left.left;
        Lexeme nextVal = env.right.left.right;
        while(nextVar != null) {
            if (nextVar.strValue.contentEquals(id)) {
                value.right = nextVal.right;
                currentVal.right = value;
                return value;
            }
            currentVal = currentVal.right;
            currentVar = currentVar.left;
            nextVal = nextVal.left;
            nextVar = nextVar.left;
        }
        return update(id, value, env.right.right);
    }

    protected Lexeme create() {
        Lexeme cool_new_environment = new Lexeme("env");
        cool_new_environment.right = new Lexeme("values_join");
        return cool_new_environment;
    }

    protected Lexeme insert(Lexeme env, Lexeme variable, Lexeme value) {
        variable.left = env.left;
        value.right = env.right.left;
        env.left = variable;
        env.right.left = value;
        return value;
    }

    protected Lexeme extend(Lexeme varList, Lexeme valList, Lexeme env) {
        Lexeme newEnv = new Lexeme("env");
        newEnv.left = varList;
        newEnv.right = new Lexeme("vals");
        newEnv.right.left = valList;
        newEnv.right.right = env;
        return newEnv;
    }

    protected void displayLocal(Lexeme env) {
        Lexeme currentVar = env.left;
        Lexeme currentVal = env.right.left;
        while (currentVal != null) {
            System.out.println(currentVar.type + " " + currentVal.type);
            currentVar = currentVar.left;
            currentVal = currentVal.right;
        }
    }

    protected void displayAll(Lexeme env) {
        Lexeme currentEnv = env;
        while (env != null) {
            displayLocal(env);
            env = env.right.right;
        }
    }

}
