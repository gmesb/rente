package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Preuve;
import fr.stac.rentes.service.PreuveService;
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
 * Créé le  07/04/2017.
 *
 * @author Antonio
 */


@Controller
public class PreuveController {
    private PreuveService preuveService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire

    @Autowired
    public void setPreuveService(PreuveService preuveService) {
        this.preuveService = preuveService;
    }

    @GetMapping("/preuve/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes messageRetour){

        Collection<Preuve> allPreuves = preuveService.getAll();
        Preuve preuve = preuveService.getById(ID);

        model.addAttribute("PREUVE", preuve);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("PREUVES", allPreuves);    // le model est virtuel et est coxstruit a chaque appel

        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        log.info("Modifie la preuve : "
                +preuve.getLibel()+" "+ preuve.getId()
        );

        String mess = "Vous modifiez les données de : "+preuve.getLibel();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "user/preuve";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/preuve/list")
    public String list(Model model){

        List<Preuve> allPreuves = preuveService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Preuve preuve = new Preuve();
        preuve.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("PREUVES", allPreuves);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("PREUVE", preuve);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/preuve";
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/preuve/add")
    public String add(Model model, @ModelAttribute("PREUVE") @Valid Preuve preuve,
                      BindingResult bindingResult, RedirectAttributes message){

        Collection<Preuve> allPreuves = preuveService.getAll();

        model.addAttribute("PREUVES", allPreuves);

        // Attention j'ai rajouté
        // model.addAttribute("bindingResult",bindingResult);


        if(bindingResult.hasErrors()) {

            log.info("Erreur de saisie : "+ preuve.getLibel()+"  "+preuve.getId() );
            return "user/preuve";

        } else {

            // on map du formulaire de saisie vers le preuve definitif a enregistrer

            log.info("insert Reval "+ preuve.getId()+" "+ preuve.getLibel()   );

            preuveService.save(preuve);

            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+preuve.getLibel()+" effectuée");
            //log.info("Ajout de preuve "+preuve.getLibel());
            return "redirect:/preuve/list";
        }
    }


    @GetMapping("/preuve/sup/{id}")
    public String sup(@PathVariable("id") Long supID,
                            final RedirectAttributes messageRetour){

        Preuve preuve = preuveService.getById(supID);
        if(preuve.getPreuvevies().size()>0){

            log.info("Supression impossible   ---->   nbre de preuves concernées : "+preuve.getPreuvevies().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+preuve.getPreuvevies().size()+" preuves attachée(s)");
            return "redirect:/preuve/list";   // c'est l'url du controleur

        }else {
            preuveService.delete(preuve);
            log.info("Supprime la preuve" + preuve.getLibel());

            messageRetour.addFlashAttribute("MESSAGE", "Vous avez supprimé : " + preuve.getLibel());
            return "redirect:/preuve/list";
        }
    }
}
