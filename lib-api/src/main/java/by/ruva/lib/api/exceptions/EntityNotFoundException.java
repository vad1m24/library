package by.ruva.lib.api.exceptions;

public class EntityNotFoundException extends Exception {

    private static final long serialVersionUID = 5024315615518026922L;

    public EntityNotFoundException(String entity) {
        super("Sorry, we do not have a " + entity + " with such id");
    }
}
