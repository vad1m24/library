package by.ruva.lib.api.exceptions;

public class DepartmentIsNotEmptyException extends Exception {

    private static final long serialVersionUID = 5024315615518026922L;

    public DepartmentIsNotEmptyException() {
        super("Department is not empty! Delete books from it. And try it again");
    }
}
