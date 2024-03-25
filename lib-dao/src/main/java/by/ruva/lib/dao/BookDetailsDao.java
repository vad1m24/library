package by.ruva.lib.dao;

import by.ruva.lib.api.dao.IBookDetailsDao;
import by.ruva.lib.entities.BookDetails;

import org.springframework.stereotype.Repository;

@Repository
public class BookDetailsDao extends AGenericDao<BookDetails> implements IBookDetailsDao {

    public BookDetailsDao() {
        super(BookDetails.class);
    }
}