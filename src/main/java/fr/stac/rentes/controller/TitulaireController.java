package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Titulaire;
import fr.stac.rentes.service.TitulaireService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


/**
 * Projet Gestion Rentes
 * Pour Antonio RODRIGUES
 * Direction générale de l'aviation Civile
 * Créé le  02/04/2017.
 *
 * @author Antonio
 */


@Controller
public class TitulaireController {
    private TitulaireService titulaireService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire

    @Autowired
    public void setTitulaireService(TitulaireService titulaireService) {
        this.titulaireService = titulaireService;
    }

    @GetMapping("/titulaire/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes messageRetour){

        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        Titulaire titulaire = titulaireService.getById(ID);

        model.addAttribute("TITULAIRE", titulaire);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("TITULAIRES", allTitulaires);
        model.addAttribute("DISABLEBTN", false);             // active le bouton   Ajout/modif


        log.info("Modifie la titulaire : "+titulaire.getLibel()+" "+ titulaire.getId()
        );

        String mess = "Vous modifiez les données de : "+titulaire.getLibel();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "user/titulaire";            // on repart sur la page HTML qui sera rafraichie
    }



    @GetMapping("/titulaire/list")
    public String list(Model model){
        List<Titulaire> allTitulaires = titulaireService.getAll();

        log.info("Je suis dans /titulaire/list");

       // on initialize pour ne pas avoir  null dans l'ID
        Titulaire titulaire = new Titulaire();
        titulaire.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("TITULAIRE",titulaire);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/titulaire";
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/titulaire/add")
    public String add(Model model, @ModelAttribute("TITULAIRE") @Valid Titulaire titulaire,
                      BindingResult bindingResult, RedirectAttributes message){

        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        model.addAttribute("TITULAIRES", allTitulaires);

        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);


        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie : "+ titulaire.getLibel()+"  "+titulaire.getId() );
            
            return "user/titulaire";

        } else {

            // on map du formulaire de saisie vers le titulaire definitif a enregistrer

            log.info("insert Reval "+ titulaire.getId()+" "+ titulaire.getLibel()   );

            //appel le LocationService
            titulaireService.save(titulaire);
            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+titulaire.getLibel()+" effectuée ");

            log.info("Ajout de titulaire "+titulaire.getLibel());
            return "redirect:/titulaire/list";
        }
    }

    @GetMapping("/titulaire/sup/{id}")
    public String sup(@PathVariable("id") Long supID,
                            final RedirectAttributes messageRetour){

        Titulaire titulaire = titulaireService.getById(supID);

        if(titulaire.getRentiers().size()>0){

            log.info("Supression impossible   ---->   nbre de users concernées : "+titulaire.getRentiers().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+titulaire.getRentiers().size()+" rentier(s) attaché(s)");
            return "redirect:/titulaire/list";   // c'est l'url du controleur
        }else {

            titulaireService.delete(titulaire);
            log.info("Supprime la titulaire" + titulaire.getLibel());
            messageRetour.addFlashAttribute("MESSAGE", "Vous suppruimez : " + titulaire.getLibel());
            return "redirect:/titulaire/list";
        }
    }
}
