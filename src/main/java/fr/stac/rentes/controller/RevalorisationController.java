package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Revalorisation;
import fr.stac.rentes.service.RevalorisationService;
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
public class RevalorisationController {

    private RevalorisationService revalorisationService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setRevalorisationService(RevalorisationService revalorisationService) {
        this.revalorisationService = revalorisationService;
    }

    @GetMapping("/revalorisation/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes messageRetour){

        Collection<Revalorisation> allRevalorisations = revalorisationService.getAll();
        Revalorisation revalorisation = revalorisationService.getById(ID);

        model.addAttribute("REVALORISATION", revalorisation);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("REVALORISATIONS", allRevalorisations);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        log.info("Modifie la revalorisation : "
                +revalorisation.getLibel()+" "+ revalorisation.getId()

        );

        String mess = "Vous modifiez les données de : "+revalorisation.getLibel();
        messageRetour.addFlashAttribute("message",mess);

        return "user/revalorisation";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/revalorisation/list")
    public String list(Model model){

        List<Revalorisation> revalorisations = revalorisationService.getAll();

        log.info("Je suis dans /revalorisation/list");

        // on initialize pour ne pas avoir  null dans l'ID
        Revalorisation revalorisation = new Revalorisation();
        revalorisation.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        
        model.addAttribute("REVALORISATIONS", revalorisations);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("REVALORISATION", revalorisation);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/revalorisation";
    }

    //methode avec controle de saisie et mappage de Objet saisi "RevalorisationForm" vers Objet definitif "Revalorisation"

    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/revalorisation/add")
    public String add(Model model, @ModelAttribute("REVALORISATION") @Valid Revalorisation revalorisation,
                      BindingResult bindingResult, RedirectAttributes message){

        Collection<Revalorisation> allRevalorisations = revalorisationService.getAll();

        model.addAttribute("REVALORISATIONS", allRevalorisations);


        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);


        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie : "+ revalorisation.getLibel()
                    +"  " +revalorisation.getId()+" "+revalorisation.getCoeff() );
            return "user/revalorisation";

        } else {

            // on map du formulaire de saisie vers le revalorisation definitif a enregistrer

            log.info("insert Reval "+ revalorisation.getId()+" "+ revalorisation.getLibel()   );

            //appel le LocationService
            revalorisationService.save(revalorisation);
            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+revalorisation.getLibel()+" effectuée");
            log.info("Ajout de revalorisation "+revalorisation.getLibel());
            return "redirect:/revalorisation/list";
        }
    }

    @GetMapping("/revalorisation/sup/{id}")
    public String sup(@PathVariable("id") Long supID,
                            final RedirectAttributes messageRetour){

        Revalorisation revalorisation = revalorisationService.getById(supID);

        // on verifie l'existance de versements associée à la revalorisation

        log.info("Nombre de versements : "+revalorisation.toString());

        if( revalorisation.getRenterevalorisees().size() > 0 ) {

            //envoi un message vers la page HTML car suppression impossible
            log.info("Supression impossible   ---->   nbre de rentes concernées : "+revalorisation.getRenterevalorisees().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+revalorisation.getRenterevalorisees().size()+" revalorisatios attachée(s) : " + revalorisation.getLibel());

            //return "rentier/newrente";            // on repart sur la page HTML qui sera rafraichie

            //return "redirect:/rentier/listnom/*";                          // c'est l'url du controleur
            return "redirect:/revalorisation/get/"+revalorisation.getId();   // c'est l'url du controleur


        }else {
            revalorisationService.delete(revalorisation);

            log.info("Supprime la revalorisation" + revalorisation.getLibel());

            //envoi un message vers la page HTML apres suppression
            //messageRetour.addFlashAttribute("SUPid",supID);
            messageRetour.addFlashAttribute("MESSAGE", "Suppression de " + revalorisation.getLibel() + " effectuée");

            return "redirect:/revalorisation/list";
        }
    }
}
