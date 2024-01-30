package lox;

class Interpreter implements Expr.Visitor<Object> {
    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            default:
                return null;
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                checkNumberOrStringOperands(expr.operator, left, right);
                if (areDoubles(left, right)) return (double)left > (double)right;
                if (areStrings(left, right)) return ((String)left).compareTo((String)right) > 0;
            case GREATER_EQUAL:
                checkNumberOrStringOperands(expr.operator, left, right);
                if (areDoubles(left, right)) return (double)left >= (double)right;
                if (areStrings(left, right)) return ((String)left).compareTo((String)right) >= 0;
            case LESS:
                checkNumberOrStringOperands(expr.operator, left, right);
                if (areDoubles(left, right)) return (double)left < (double)right;
                if (areStrings(left, right)) return ((String)left).compareTo((String)right) < 0;
            case LESS_EQUAL:
                checkNumberOrStringOperands(expr.operator, left, right);
                if (areDoubles(left, right)) return (double)left <= (double)right;
                if (areStrings(left, right)) return ((String)left).compareTo((String)right) <= 0;
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                checkNumberOrStringOperands(expr.operator, left, right);
                if (areDoubles(left, right)) return (double)left + (double)right;
                if (areStrings(left, right)) return (String)left + (String)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            default:
                return null;
        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }

    private boolean areDoubles(Object a, Object b) {
        return a instanceof Double && b instanceof Double;
    }

    private boolean areStrings(Object a, Object b) {
        return a instanceof String && b instanceof String;
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private void checkNumberOrStringOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        if (left instanceof String && right instanceof String) return;
        throw new RuntimeError(operator, "Operands must be two numbers or two strings.");
    }
}
