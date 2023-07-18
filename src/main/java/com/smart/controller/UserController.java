package com.smart.controller;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUser(username);
        model.addAttribute("user", user);
    }

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "Welcome - Smart Contact Manager");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contact - Smart Contact Manager");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact";
    }

//    processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file, Principal principal, HttpSession session) {

        try {
            String username = principal.getName();
            User user = userService.getUser(username);

//            Processing Image for Profile Pic
            if (file.isEmpty()) {
                System.out.println("File is Empty");
            } else {
//                Upload file to folder
                String fileName = file.getOriginalFilename()+"_"+contact.getEmail()+"_"+user.getId();
                contact.setImage(fileName);

                File tempFile = new ClassPathResource("static/img/").getFile();
                Path path = Paths.get(tempFile.getAbsolutePath()+File.separator+fileName);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//                System.out.println("Successfully uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            userService.updateUser(user);

            session.setAttribute("message", new Message("Your Contact is Successfully added!", "alert-success"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went Wrong!", "alert-danger"));
        }

        System.out.println("data" + contact);
        return "normal/add_contact";
    }

    public void removeVerificationMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("message");
        } catch (RuntimeException ex) {

        }
    }
}
