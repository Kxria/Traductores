import java.util.*;

public class CTranslator implements Translator {
    private int indentLevel = 0;
    private static final String INDENT_UNIT = "    ";

    private static final Map<String, String> TYPE_MAP = new HashMap<>();
    
    static {
        TYPE_MAP.put("entero", "int");
        TYPE_MAP.put("real", "double");
        TYPE_MAP.put("cadena", "char*");
        TYPE_MAP.put("booleano", "int");
    }

    @Override
    public String initProgram() {
        return "#include <stdio.h>\n\nint main(void) {\n";
    }

    @Override
    public String endProgram() {
        return indent() + "return 0;\n}\n";
    }

    @Override
    public String declareVar(String type, String name) {
        String cType = TYPE_MAP.getOrDefault(type.toLowerCase(), type);
        return indent() + cType + " " + name + ";\n";
    }

    @Override
    public String readVar(String prompt, String varName) {
        StringBuilder sb = new StringBuilder();
        if (!prompt.isEmpty()) {
            sb.append(indent()).append("printf(\"").append(prompt).append(": \");\n");
        }

        sb.append(indent()).append("scanf(\"%s\", &").append(varName).append(");\n");
        return sb.toString();
    }

    @Override
    public String writeExpr(String expr) {
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            return indent() + "printf(" + expr + ");\n" +
                    indent() + "printf(\"\\n\");\n";
        }
        return indent() + "printf(\"%s\\n\", " + expr + ");\n";
    }

    @Override
    public String assign(String varName, String expr) {
        return indent() + varName + " = " + expr + ";\n";
    }

    @Override
    public String ifStart(String condition) {
        String line = indent() + "if (" + condition + ") {\n";
        increaseIndent();
        return line;
    }

    @Override
    public String elseClause() {
        decreaseIndent();
        String line = indent() + "} else {\n";
        increaseIndent();
        return line;
    }

    @Override
    public String ifEnd() {
        decreaseIndent();
        return indent() + "}\n";
    }

    @Override
    public String whileStart(String condition) {
        String line = indent() + "while (" + condition + ") {\n";
        increaseIndent();
        return line;
    }

    @Override
    public String whileEnd() {
        decreaseIndent();
        return indent() + "}\n";
    }

    @Override
    public String funcStart(String name, String returnType, String params) {
        String cReturn = TYPE_MAP.getOrDefault(returnType.toLowerCase(), returnType);
        String line = cReturn + " " + name + "(" + params + ") {\n";
        increaseIndent();
        return line;
    }

    @Override
    public String funcEnd() {
        decreaseIndent();
        return "}\n\n";
    }

    @Override
    public String returnStmt(String expr) {
        return indent() + "return " + expr + ";\n";
    }

    @Override
    public String funcCall(String name, String args) {
        return indent() + name + "(" + args + ");\n";
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
