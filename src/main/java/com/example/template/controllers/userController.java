package com.example.template.controllers;

import com.example.template.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class userController {

    private final static String ERROR_PATH = "/error";

    @RequestMapping(value = "/hello")
    public String getHelloWorld(Model model, @RequestParam("name") String name)
    {
        model.addAttribute("name",name);

        return "helloworld";
    }
    @CrossOrigin
    @RequestMapping(value = "/user")
    public String getUser(Model model, @RequestBody User user)
    {
        model.addAttribute("user",user);
        return "helloworld";
    }
}
