package fr.stac.rentes.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqController {

	@GetMapping("/user/faq")
    public String main(){


        return "user/faq";
    }

	
}
