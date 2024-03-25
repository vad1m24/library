package by.ruva.lib.api.exceptions;

public class NoSuchBookException extends Exception {

    private static final long serialVersionUID = -3801207018103995688L;

    public NoSuchBookException() {
        super("No such book, please get in touch with admin");
    }
}
