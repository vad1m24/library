package by.ruva.lib.api.exceptions;

public class UserIsAlreadyExistsException extends Exception {

    private static final long serialVersionUID = 5024315615518026922L;

    public UserIsAlreadyExistsException() {
        super("Sorry, user with this email is already exists");
    }
}
