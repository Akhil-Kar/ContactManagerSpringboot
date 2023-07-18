package com.smart.controller;

import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Smart Contact Manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "SignUp - Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    //handler for registering new User:--
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
//        System.out.println("Agreement " + agreement);
//        System.out.println("User" + user);

        try {
            if (!agreement) {
                System.out.println("You have not agreed condition");
                throw new Exception("You have not agreed condition");
            }
            if (bindingResult.hasErrors()) {
                System.out.println("Error: + " + bindingResult.toString());
                model.addAttribute("user", user);
                return "signup";
            }

            User result = userService.saveUser(user);
//            System.out.println(result);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered!", "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong!" + e.getMessage(), "alert-danger"));
            return "signup";
        }

        return "signup";
    }

    public void removeVerificationMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("message");
        } catch (RuntimeException ex) {

        }
    }

    // handler for login page
//    @GetMapping("/signin")
//    public String customLogin(Model model) {
//        model.addAttribute("title", "SignIn - Smart Contact Manager");
//        return "login";
//    }


    @GetMapping("/login")
    String login(Model model) {
        model.addAttribute("title", "SignIn - Smart Contact Manager");
        return "login";
    }
}
