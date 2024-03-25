package by.ruva.lib.api.dao;

import java.util.List;

import by.ruva.lib.entities.Book;

public interface IBookDao extends IAGenericDao<Book> {

    Book getByIsbn(String isbn);

    List<Book> getBooksByDepartmentId(Long id);
}
