package by.ruva.lib.api.exceptions;

public class IsAlreadyProlongedException extends Exception {

    private static final long serialVersionUID = -3616793790062667768L;

    public IsAlreadyProlongedException() {
        super("Sorry, this book is already prolonged");
    }
}
