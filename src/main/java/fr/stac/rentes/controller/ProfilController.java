package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Profil;
import fr.stac.rentes.service.ProfilService;
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
 * Créé le 06/04/2017.
 *
 * @author Antonio
 */


@Controller
public class ProfilController {
    private ProfilService profilService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setProfilService(ProfilService profilService) {
        this.profilService = profilService;
    }

    @GetMapping("/profil/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes messageRetour){

        Collection<Profil> allProfils = profilService.getAll();
        Profil profil = profilService.getById(ID);

        model.addAttribute("PROFIL", profil);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("PROFILS", allProfils);
        model.addAttribute("DISABLEBTN", false);    // active le bouton   Ajout/modif


        log.info("Modifie profil : "
                +profil.getNom()+" "+ profil.getId()
                +" "+ profil.getRole()
        );

        String mess = "Vous modifiez les données de : "+profil.getNom();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "user/profil";            // on repart sur la page HTML qui sera rafraichie

    }


    @GetMapping("/profil/list")
    public String list(Model model){

        List<Profil> allProfils = profilService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Profil profil = new Profil();
        profil.setId(0L);             // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("PROFILS", allProfils);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("PROFIL", profil);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/profil";
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @RequestMapping(value = "/profil/add")
    public String add( Model model, @ModelAttribute("PROFIL") @Valid Profil profil,
                      BindingResult bindingResult, RedirectAttributes message){

        Collection<Profil> allProfils = profilService.getAll();
        model.addAttribute("PROFILS", allProfils);


        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);


        if(bindingResult.hasErrors()) {
            //log.info("Erreur de saisie "+ profilForm.getNom());
            return "user/profil";

        } else {

            // on map du formulaire de saisie vers le profil definitif a enregistrer

            log.info("insert saisie "+ profil.getId()+" "+ profil.getNom()   );

            //appel le ProfilService
            profilService.save(profil);
            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+profil.getNom()+" effectuée");

            //log.info("Ajout de profil "+profil.getNom());
            return "redirect:/profil/list";
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/profil/sup/{id}")
    public String sup(@PathVariable("id") Long supID,final RedirectAttributes messageRetour){

        Profil profil = profilService.getById(supID);


        if(profil.getUsers().size()>0){
            log.info("Supression impossible   ---->   nbre de users concernées : "+profil.getUsers().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+profil.getUsers().size()+" users attachée(s)");
            return "redirect:/profil/list";   // c'est l'url du controleur
        }else{

            profilService.delete(profil);
            log.info("Supprime l'utilisateur"+profil.getNom());
            messageRetour.addFlashAttribute("MESSAGE","Vous supprimez "+profil.getNom());
            return "redirect:/profil/list";
        }
    }
}
