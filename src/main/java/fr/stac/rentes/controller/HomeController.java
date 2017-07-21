package fr.stac.rentes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/")
	public String helloWorld(Model model) {
        //log.info("Accès à la page d'accueil et pas de message ?");
		//return "/home";

		//  on cache
		model.addAttribute("HiddenAffiche", true);     // Desactive le formulaire
		return "/login";									// Page login.HTML
	}
}
