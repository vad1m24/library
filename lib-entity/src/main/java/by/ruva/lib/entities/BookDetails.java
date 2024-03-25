package by.ruva.lib.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "book_details")
public class BookDetails extends AEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "author")
    private String author;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description")
    private String description;
}

