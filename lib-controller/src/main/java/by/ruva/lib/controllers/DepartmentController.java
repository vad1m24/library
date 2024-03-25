package by.ruva.lib.controllers;

import by.ruva.lib.api.dto.DepartmentDto;
import by.ruva.lib.api.exceptions.DepartmentIsNotEmptyException;
import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.service.IDepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Secured("ROLE_ADMIN")
@RestController
@RequestMapping("/departments/")
public class DepartmentController {

    private static final String ERRORS = "errors/errors";
    private static final String MESSAGE = "message";
    private static final String DEPARTMENT = "department";

    @Autowired
    IDepartmentService departmentService;

    @GetMapping
    public ModelAndView getAllDepartments() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("alldepartments");
        modelAndView.addObject("departmentList", departmentService.getAllDepartments());
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView getDepartmentById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("onedepartment");
            modelAndView.addObject(DEPARTMENT, departmentService.getDepartmentById(id));
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @GetMapping(value = "adddepartment")
    public ModelAndView addDepartment() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("departmentsList", departmentService.getAllDepartments());
        modelAndView.setViewName("adddepartment");
        return modelAndView.addObject("departmentdto", new DepartmentDto());
    }

    @PostMapping(value = "adddepartment")
    public ModelAndView addDepartmentSubmit(DepartmentDto departmentDto) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("onedepartment");
        return modelAndView.addObject(DEPARTMENT, departmentService.createDepartment(departmentDto));
    }

    @GetMapping("edit/{id}")
    public ModelAndView getDepartmentEditForm(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("updatedepartment");
            modelAndView.addObject(DEPARTMENT, departmentService.getDepartmentById(id));
            modelAndView.addObject("dto", new DepartmentDto());
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @PostMapping("edit/{id}")
    public ModelAndView saveDepartmentChanges(@PathVariable Long id, DepartmentDto departmentDto) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject(DEPARTMENT, departmentService.updateDepartment(id, departmentDto));
            modelAndView.setViewName("general/changesSaved");
        } catch (EntityNotFoundException e) {
            returnViewNameWithError(modelAndView, e);
        }
        return modelAndView;
    }

    @PostMapping("delete/{id}")
    public ModelAndView deletebookSubmit(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            departmentService.deleteDepartmentById(id);
            modelAndView.setViewName("general/changesSaved");
        } catch (EntityNotFoundException | DepartmentIsNotEmptyException e) {
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