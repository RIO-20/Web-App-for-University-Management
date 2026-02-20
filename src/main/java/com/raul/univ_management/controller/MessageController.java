/** Clasa controller pentru gestionarea mesajelor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.controller;

import com.raul.univ_management.dao.AdminDao;
import com.raul.univ_management.dao.MessageDao;
import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

    private final MessageDao messageDao;
    private final AdminDao adminDao;

    public MessageController(MessageDao messageDao, AdminDao adminDao) {
        this.messageDao = messageDao;
        this.adminDao = adminDao;
    }

    @GetMapping("/messages")
    public String showMessages(jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/login";
        }

        
        var inbox = messageDao.findByReceiverEmail(admin.getEmail());
        model.addAttribute("inbox", inbox);

        var contacts = adminDao.findEmailsForContact(admin.getId());
        model.addAttribute("contacts", contacts);
        model.addAttribute("admin", admin);

        return "messages";
    }

    @PostMapping("/messages/send")
    public String sendMessage(Message message, jakarta.servlet.http.HttpSession session) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/login";
        }

        message.setSenderId(admin.getId());
        message.setSenderName(admin.getNume() + " " + admin.getPrenume());
        
        messageDao.insert(message);

        return "redirect:/messages?success";
    }
}
