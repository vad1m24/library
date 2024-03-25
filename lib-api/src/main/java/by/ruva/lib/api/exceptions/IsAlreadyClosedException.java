package by.ruva.lib.api.exceptions;

public class IsAlreadyClosedException extends Exception {

    private static final long serialVersionUID = 8774948403114615216L;

    public IsAlreadyClosedException() {
        super("Sorry, this order is already сlosed");
    }
}
