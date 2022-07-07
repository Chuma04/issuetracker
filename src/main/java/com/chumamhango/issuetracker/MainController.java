package com.chumamhango.issuetracker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index(){
        return "index";
    }

// handler for about page
    @GetMapping("/about")
    public String about(){
        return "about";
    }
}
