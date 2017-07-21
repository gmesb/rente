package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Versemtype;
import fr.stac.rentes.service.VersemtypeService;
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
public class VersemtypeController {
    private VersemtypeService versemtypeService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire

    @Autowired
    public void setVersemtypeService(VersemtypeService versemtypeService) {
        this.versemtypeService = versemtypeService;
    }


    @GetMapping("/versemtype/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes messageRetour){

        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();
        Versemtype versemtype = versemtypeService.getById(ID);

        model.addAttribute("VERSEMTYPE", versemtype);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("VERSEMTYPES", allVersemtypes);     // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("DISABLEBTN", false);             // active le bouton   Ajout/modif

        log.info("Modifie la versemtype : "
                +versemtype.getLibel()+" "+ versemtype.getId()
       );

        String mess = "Vous modifiez les données de : "+versemtype.getLibel();
        messageRetour.addFlashAttribute("message",mess);

        return "user/versemtype";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/versemtype/list")
    public String list(Model model){

        List<Versemtype> allVersemtypes = versemtypeService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Versemtype versemtype = new Versemtype();
        versemtype.setId(0);

        model.addAttribute("VERSEMTYPES", allVersemtypes);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("VERSEMTYPE", new Versemtype());
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif
        return "user/versemtype";                               // c'est la page versemtype.HTML
    }


    //methode avec controle de saisie et mappage de Objet saisi "RevalorisationForm" vers Objet definitif "Versemtype"


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/versemtype/add")
    public String add( Model model, @ModelAttribute("VERSEMTYPE") @Valid Versemtype versemtype,
                      BindingResult bindingResult, RedirectAttributes message){

        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();

        model.addAttribute("VERSEMTYPES", allVersemtypes);
        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);


        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie : "+ versemtype.getLibel()+"  "+versemtype.getId() );
            return "user/versemtype";

        } else {

            // on map du formulaire de saisie vers le versemtype definitif a enregistrer

            log.info("insert Reval "+ versemtype.getId()+" "+ versemtype.getLibel()   );

            //appel le LocationService
            versemtypeService.save(versemtype);
            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+versemtype.getLibel()+" effectuée");

            log.info("Ajout de versemtype "+versemtype.getLibel());
            return "redirect:/versemtype/list";
        }
    }

    @GetMapping("/versemtype/sup/{id}")
    public String sup(@PathVariable("id") Long supID,
                            final RedirectAttributes messageRetour){

        Versemtype versemtype = versemtypeService.getById(supID);


        if(versemtype.getRentes().size()>0) {

            log.info("Supression impossible   ---->   nbre de users concernées : " + versemtype.getRentes().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a " + versemtype.getRentes().size() + " rentier(s) attaché(s)");
            return "redirect:/versemtype/list";  // c'est l'url du controleur
        }else{
            versemtypeService.delete(versemtype);
            log.info("Supprime la versemtype"+versemtype.getLibel());

            messageRetour.addFlashAttribute("MESSAGE","Suppresion de "+versemtype.getLibel()+" effectuée");

            return "redirect:/versemtype/list";
        }

    }
}
