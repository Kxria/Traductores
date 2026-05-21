import java.util.*;

public class PythonTranslator implements Translator {

    private int indentLevel = 0;
    private static final String INDENT_UNIT = "    ";

    @Override
    public String initProgram() {
        return "#!/usr/bin/env python3\n# -*- coding: utf-8 -*-\n\ndef main():\n";
    }

    @Override
    public String endProgram() {
        return "\nif __name__ == \"__main__\":\n    main()\n";
    }

    @Override
    public String declareVar(String type, String name) {
        String initVal;
        switch (type.toLowerCase()) {
            case "entero":
                initVal = "0";
                break;
            case "real":
                initVal = "0.0";
                break;
            case "cadena":
                initVal = "\"\"";
                break;
            case "booleano":
                initVal = "False";
                break;
            default:
                initVal = "None";
                break;
        }
        return indent() + name + " = " + initVal + "\n";
    }

    @Override
    public String readVar(String prompt, String varName) {
        String displayPrompt = prompt.isEmpty() ? varName : prompt;

        return indent() + varName + " = input(\"" + displayPrompt + ": \")\n";
    }

    @Override
    public String writeExpr(String expr) {
        return indent() + "print(" + expr + ")\n";
    }

    @Override
    public String assign(String varName, String expr) {
        return indent() + varName + " = " + expr + "\n";
    }

    @Override
    public String ifStart(String condition) {
        String line = indent() + "if " + condition + ":\n";
        increaseIndent();
        return line;
    }

    @Override
    public String elseClause() {
        decreaseIndent();
        String line = indent() + "else:\n";
        increaseIndent();
        return line;
    }

    @Override
    public String ifEnd() {
        decreaseIndent();
        return "";
    }

    @Override
    public String whileStart(String condition) {
        String line = indent() + "while " + condition + ":\n";
        increaseIndent();
        return line;
    }

    @Override
    public String whileEnd() {
        decreaseIndent();
        return "";
    }

    @Override
    public String funcStart(String name, String returnType, String params) {
        String line = indent() + "def " + name + "(" + params + "):\n";
        increaseIndent();
        return line;
    }

    @Override
    public String funcEnd() {
        decreaseIndent();
        return "\n";
    }

    @Override
    public String returnStmt(String expr) {
        return indent() + "return " + expr + "\n";
    }

    @Override
    public String funcCall(String name, String args) {
        return indent() + name + "(" + args + ")\n";
    }

    @Override
    public String indent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++)
            sb.append(INDENT_UNIT);
        return sb.toString();
    }

    @Override
    public void increaseIndent() {
        indentLevel++;
    }

    @Override
    public void decreaseIndent() {
        if (indentLevel > 0)
            indentLevel--;
    }
}
