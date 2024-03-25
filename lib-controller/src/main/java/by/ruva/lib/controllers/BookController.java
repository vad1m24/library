package by.ruva.lib.controllers;

import by.ruva.lib.api.dto.BookDetailsDto;
import by.ruva.lib.api.dto.BookDto;
import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.NoSuchBookException;
import by.ruva.lib.api.service.IBookService;
import by.ruva.lib.api.service.IDepartmentService;
import by.ruva.lib.api.service.IFeedbackService;
import by.ruva.lib.utils.ImgFileUploader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
@RequestMapping("/books/")
public class BookController {

    private static final String ERRORS = "errors/errors";
    private static final String MESSAGE = "message";
    private static final String BOOK = "book";
    private static final String ONEBOOK = "oneBook";

    @Autowired
    IBookService bookService;

    @Autowired
    IFeedbackService feedbackService;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    ImgFileUploader imgFileUploader;

    @GetMapping
    public ModelAndView getAllBooks() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allbooks");
        modelAndView.addObject("allDepartments", departmentService.getAllDepartments());
        modelAndView.addObject("bookList", bookService.getAllBooks());
        return modelAndView;
    }

    @GetMapping("/byDepartment/{id}")
    public ModelAndView getAllBooksByDepartment(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allDepartments", departmentService.getAllDepartments());
        modelAndView.setViewName("allbooks");
        try {
            modelAndView.addObject("bookList", bookService.getBooksByDepartmentId(id));
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView getBookById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject(BOOK, bookService.getBookById(id));
            modelAndView.addObject("listFeedbacks", feedbackService.getAllFeedbacksByBookId(id));
            modelAndView.setViewName(ONEBOOK);
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam String isbn) {
        ModelAndView modelAndView = new ModelAndView("search");
        try {
            modelAndView.addObject(BOOK, bookService.getBookByIsbn(isbn));
            modelAndView.setViewName(ONEBOOK);
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping(value = "addbook")
    public ModelAndView addBook() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentsList", departmentService.getAllDepartments());
        modelAndView.addObject("departmentdto", new DepartmentDto());
        modelAndView.setViewName("addbook");
        return modelAndView.addObject("bookdto", new BookDto());
    }

    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping(value = "addbook")
    public ModelAndView addBookSubmit(BookDto bookDto, DepartmentDto departmentDto) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ONEBOOK);
        try {
            modelAndView.addObject("book", bookService.createBook(bookDto, departmentDto));
        } catch (EntityNotFoundException | NoSuchBookException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @GetMapping("edit/{id}")
    public ModelAndView getBookEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("updatebook");
            modelAndView.addObject(BOOK, bookService.getBookById(id));
            modelAndView.addObject("detailsdto", new BookDetailsDto());
            modelAndView.addObject("dto", new BookDto());
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @Secured({ "ROLE_ADMIN", "ROLE_USER" })
    @PostMapping("edit/{id}")
    public ModelAndView saveBookChanges(BookDto bookDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            imgFileUploader.createOrUpdateBookCover(bookDto, file);
            modelAndView.addObject("book", bookService.updateBook(bookDto, file));
            modelAndView.setViewName("general/changesSaved");
        } catch (IOException | EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    private void returnViewNameWithError(ModelAndView modelAndView, EntityNotFoundException e) {
        modelAndView.setViewName(ERRORS);
        modelAndView.addObject(MESSAGE, e.getMessage());
    }
}