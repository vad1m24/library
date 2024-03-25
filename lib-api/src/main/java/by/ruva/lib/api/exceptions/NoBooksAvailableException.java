package by.ruva.lib.api.exceptions;

public class NoBooksAvailableException extends Exception {

    private static final long serialVersionUID = -5436303797788043366L;

    public NoBooksAvailableException() {
        super("Sorry, this book is not available now");
    }
}
