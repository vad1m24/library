package by.ruva.lib.controllers;

import by.ruva.lib.api.exceptions.EntityNotFoundException;
import by.ruva.lib.api.exceptions.IsAlreadyClosedException;
import by.ruva.lib.api.exceptions.IsAlreadyProlongedException;
import by.ruva.lib.api.exceptions.NoBooksAvailableException;
import by.ruva.lib.api.service.IOrderService;
import by.ruva.lib.api.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("/orders/")
@Secured({ "ROLE_ADMIN", "ROLE_USER" })
public class OrderController {

    private static final String ERRORS = "errors/errors";
    private static final String MESSAGE = "message";
    private static final String ORDER = "order";

    @Autowired
    IOrderService orderService;

    @Autowired
    IUserService userService;

    @GetMapping("my")
    public ModelAndView getMyOrders(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        final String currentUser = principal.getName();
        try {
            long principalId = userService.getUserByEmail(currentUser).getId();
            modelAndView.setViewName("allorders");
            modelAndView.addObject("orderList", orderService.getAllOrdersByUserId(principalId));
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView getOrderById(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("oneorder");
            modelAndView.addObject(ORDER, orderService.getOrderById(id));
        } catch (EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping(value = "addorder/{id}")
    public ModelAndView addOrder(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addorder");
        return modelAndView;
    }

    @PostMapping(value = "addorder/{id}")
    public ModelAndView addOrderSubmit(@PathVariable Long id, Principal principal) {
        final String userName = principal.getName();
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("thanksfororder");
            modelAndView.addObject(ORDER, orderService.createOrder(id, userName));
        } catch (NoBooksAvailableException | EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("prolong/{id}")
    public ModelAndView prolongOrder(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("orderProlong");
            modelAndView.addObject(ORDER, orderService.prolongOrder(id));
        } catch (IsAlreadyProlongedException | IsAlreadyClosedException | EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }

    @GetMapping("return/{id}")
    public ModelAndView closeOrder(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.setViewName("orderClose");
            modelAndView.addObject(ORDER, orderService.closeOrder(id));
        } catch (IsAlreadyClosedException | EntityNotFoundException e) {
            modelAndView.setViewName(ERRORS);
            modelAndView.addObject(MESSAGE, e.getMessage());
        }
        return modelAndView;
    }
}