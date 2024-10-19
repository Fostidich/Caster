package it.fostidich.caster;

public enum Errors {

    JsonParserFailure("Unable to produce object from json file"),
    ResourceNotFound("Unable to access resource"),
    NonNegativeValue("Value cannot be negative"),
    UndefinedVariable("Variable has not been initialized");

    private final String message;

    Errors(String message) {
        this.message = message;
    }

    public void abort() {
        System.err.println(message);
        System.exit(1);
    }

    public void abort(String detail) {
        System.err.println(message + ": " + detail);
        System.exit(1);
    }

    public void abort(boolean condition) {
        if (condition) {
            System.err.println(message);
            System.exit(1);
        }
    }

    public void abort(boolean condition, String detail) {
        if (condition) {
            System.err.println(message + ": " + detail);
            System.exit(1);
        }
    }

}