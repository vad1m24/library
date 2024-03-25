package by.ruva.lib.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = false, chain = true)
@Table(name = "book")
public class Book extends AEntity {

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "quantity_available")
    private int quantityAvailable = 0;

    @Column(name = "quantity_in_library")
    private int quantityInLibrary = 0;

    @Column(name = "rating")
    private Double rating;

    @ManyToMany
    @JoinTable(name = "department_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "department_id"))
    private List<Department> departments;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Order> orders;
    //при удалении книги будет удалятся и детали
    @OneToOne(orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private BookDetails bookDetails;
}
