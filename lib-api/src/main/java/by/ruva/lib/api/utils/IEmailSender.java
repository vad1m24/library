package by.ruva.lib.api.utils;

import javax.mail.MessagingException;

import by.ruva.lib.entities.Book;
import by.ruva.lib.entities.Department;
import by.ruva.lib.entities.Order;

public interface IEmailSender {

    void sendEmailsFromAdminAboutNewBook(Book book, Department department) throws MessagingException;

    void sendEmailsFromAdminAboutDebts(Order order) throws MessagingException;

    void sendEmailsFromAdminDueDateTomorrow(Order order) throws MessagingException;
}