public interface Translator {
    String initProgram();
    String endProgram();

    String declareVar(String type, String name);
    String readVar(String prompt, String varName);
    String writeExpr(String expr);
    String assign(String varName, String expr);

    String ifStart(String condition);
    String elseClause();
    String ifEnd();

    String whileStart(String condition);
    String whileEnd();

    String funcStart(String name, String returnType, String params);
    String funcEnd();
    String returnStmt(String expr);
    String funcCall(String name, String args);

    String indent();
    void increaseIndent();
    void decreaseIndent();
}
