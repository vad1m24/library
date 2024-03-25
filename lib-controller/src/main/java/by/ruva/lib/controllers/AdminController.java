package by.ruva.lib.controllers;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.dto.FeedbackDto;
import by.ruva.lib.api.dto.RoleDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.service.IBookService;
import by.ruva.lib.api.service.IDepartmentService;
import by.ruva.lib.api.service.IFeedbackService;
import by.ruva.lib.api.service.IOrderService;
import by.ruva.lib.api.service.IRoleService;
import by.ruva.lib.api.service.IUserService;
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
import java.util.List;

@RestController
@RequestMapping
@Secured("ROLE_ADMIN")
public class AdminController {

    private static final String ERRORS = "errors/errors";
    private static final String MESSAGE = "message";
    private static final String USER = "user";
    private static final String CHANGES_SAVED = "general/changesSaved";

    @Autowired
    IFeedbackService feedbackService;

    @Autowired
    IBookService bookService;

    @Autowired
    IUserService userService;

    @Autowired
    IOrderService orderService;

    @Autowired
    ImgFileUploader imgFileUploader;

    @Autowired
    IDepartmentService departmentService;

    @Autowired
    IRoleService roleService;

    @GetMapping("/books/delete/{id}")
    public ModelAndView deleteBook(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("book", bookService.getBookById(id));
            modelAndView.setViewName("deletebook");
            modelAndView.addObject("departmentdto", new DepartmentDto());
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @PostMapping("/books/delete/{id}")
    public ModelAndView deletebookSubmit(@PathVariable Long id, DepartmentDto departmentDto) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            bookService.deleteBookById(id, departmentDto);
            modelAndView.setViewName(CHANGES_SAVED);
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @GetMapping("/users/")
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allusers");
        modelAndView.addObject("userList", userService.getAllUsers());
        return modelAndView;
    }

    @GetMapping("/users/{id}")
    public ModelAndView getUserProfile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject(USER, userService.getUserById(id));
            modelAndView.addObject("ListOrders", orderService.getAllOrdersByUserId(id));
            modelAndView.setViewName("oneuser");
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView getUserEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("departmentsList", departmentService.getAllDepartments());
            modelAndView.addObject(USER, userService.getUserById(id));
            modelAndView.addObject("departmentdto", new DepartmentDto());
            modelAndView.addObject("dto", new UserDto());
            modelAndView.setViewName("updateuser");
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @PostMapping("/users/edit/{id}")
    public ModelAndView saveUsersChanges(UserDto userDto, DepartmentDto departmentDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            imgFileUploader.createOrUpdateUserAvatar(userDto, file);
            modelAndView.addObject(USER, userService.updateUser(userDto.getId(), userDto, departmentDto));
            modelAndView.setViewName(CHANGES_SAVED);
        } catch (IOException | EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/users/setrole/{id}")
    public ModelAndView setRoleToUser(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allroles", roleService.getAllRoles());
        modelAndView.setViewName("setroles");
        return modelAndView.addObject("dto", new RoleDto());
    }

    @PostMapping("/users/setrole/{id}")
    public ModelAndView saveRolesChanges(@PathVariable Long id, RoleDto roleDto) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject(USER, userService.setRoles(id, roleDto));
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        modelAndView.setViewName(CHANGES_SAVED);
        return modelAndView;
    }

    @GetMapping("/orders/")
    public ModelAndView getAllOrders() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("allorders");
        modelAndView.addObject("orderList", orderService.getAllOrders());
        return modelAndView;
    }

    @GetMapping("/feedbacks/")
    public ModelAndView getAllFeedbacks() {
        ModelAndView modelAndView = new ModelAndView();
        List<FeedbackDto> feedbacks = feedbackService.getAllFeedbacks();
        modelAndView.setViewName("allfeedbacks");
        modelAndView.addObject("feedbackList", feedbacks);
        return modelAndView;
    }

    @PostMapping("/feedbacks/delete/{id}")
    public ModelAndView deletebookSubmit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            feedbackService.deleteFeedbackById(id);
            modelAndView.setViewName(CHANGES_SAVED);
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    private void returnViewNameWithError(ModelAndView modelAndView, EntityNotFoundException e) {
        modelAndView.setViewName(ERRORS);
        modelAndView.addObject(MESSAGE, e.getMessage());
    }
}