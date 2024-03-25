package by.ruva.lib.controllers;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.dto.UserDto;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.UserIsAlreadyExistsException;
import by.ruva.lib.api.service.IDepartmentService;
import by.ruva.lib.api.service.IUserService;
import by.ruva.lib.utils.ImgFileUploader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class MainController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    IUserService userService;

    @Autowired
    ImgFileUploader imgFileUploader;

    @Autowired
    IDepartmentService departmentService;

    @GetMapping("/users/adduser")
    public ModelAndView addUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentsList", departmentService.getAllDepartments());
        modelAndView.setViewName("adduser");
        modelAndView.addObject("departmentdto", new DepartmentDto());
        return modelAndView.addObject("dto", new UserDto());
    }

    @PostMapping("/users/adduser")
    public ModelAndView addUserSubmit(UserDto userDto, DepartmentDto departmentDto,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            imgFileUploader.createOrUpdateUserAvatar(userService.createUser(userDto, departmentDto), file);
            modelAndView.setViewName("general/home");
            setAuth(userDto.getEmail(), userDto.getPassword());
        } catch (IOException | EntityNotFoundException | UserIsAlreadyExistsException e) {
            modelAndView.setViewName("errors/errors");
            modelAndView.addObject("message", e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("/thankU")
    public ModelAndView mailToAdminFeedback(@RequestParam String email, @RequestParam String message) {
        ModelAndView modelAndView = new ModelAndView();
        userService.sendEmailToAdmin(email, message);
        modelAndView.setViewName("general/thankU");
        return modelAndView;
    }

    @GetMapping("/newPasswordSent")
    public ModelAndView mailToUserNewPassword(@RequestParam String email) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            userService.sendEmailWithNewPassword(email);
            modelAndView.setViewName("general/thankU");
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName("errors/errors");
            modelAndView.addObject("message", e.getMessage());
        }
        return modelAndView;
    }

    private void setAuth(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(authentication);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
    }
}