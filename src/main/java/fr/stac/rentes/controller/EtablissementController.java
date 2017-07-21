package fr.stac.rentes.controller;


import fr.stac.rentes.domain.Etablissement;
import fr.stac.rentes.service.EtablissementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Projet Gestion Rentes
 * Pour Antonio RODRIGUES
 * STAC
 * Créé le  02/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class EtablissementController {
    private EtablissementService etablissementService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }


    @GetMapping("/etablissement/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                                   final RedirectAttributes message){

        List<Etablissement> allEtablissements = etablissementService.getAll();
        Etablissement etablissement = etablissementService.getById(ID);

        model.addAttribute("ETABLISSEMENTS", allEtablissements);       // le model est virtuel et est construit a chaque appel
        model.addAttribute("ETABLISSEMENT", etablissement);
        model.addAttribute("DISABLEBTN", false);                    // active le bouton   Ajout/modif

        //envoi un message vers la page HTML apres suppression
        String mess = "Vous modifiez les données de : " + etablissement.getNom();
        message.addFlashAttribute("message",mess);

        return "etablissement/etablissement";                //  c'est la page HTML
    }


    @GetMapping("/etablissement/list")
    public String list(Model model){
        List<Etablissement> allEtablissements = etablissementService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Etablissement etablissement = new Etablissement();
       etablissement.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("ETABLISSEMENTS", allEtablissements);        // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("ETABLISSEMENT", etablissement);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "etablissement/etablissement";    // HTML
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/etablissement/add")
    public String add(Model model, @ModelAttribute("ETABLISSEMENT") @Valid Etablissement etablissement,
                      BindingResult bindingResult,final RedirectAttributes message){

        Collection<Etablissement> allEtablissements = etablissementService.getAll();

        model.addAttribute("ETABLISSEMENTS", allEtablissements);

        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie "+etablissement.getNom()
                            +" ID : " +etablissement.getId()
                    );

            // des erreurs ?
            Map<String, Object> map = new HashMap<String, Object>();
            StringBuffer buffer = new StringBuffer();

            // parcours de la liste des erreurs
            for (FieldError error : bindingResult.getFieldErrors()) {
                buffer.append(String.format("[%s:%s:%s:%s:%s]", error.getField(), error.getRejectedValue(),
                        String.join(" - ", error.getCodes()), error.getCode(),error.getDefaultMessage()));
            }

            map.put("errors", buffer.toString());
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.println("LES ERREURS ::::::::::: " + map);
            System.out.println("-------------------------------------------------------------------------------------------------");

            return "etablissement/etablissement";

        } else {
            // appel le LocationService

            etablissementService.save(etablissement);
            message.addFlashAttribute("MESSAGE","Insertion ou MAJ de l'établissement "+ etablissement.getNom() + " effectuée.");

            //log.info("Ajout de etablissement "+etablissement.getNom());

            return "redirect:/etablissement/list";
        }
    }



    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("etablissement/delete/{SUPid}")
    public String sup(@PathVariable("SUPid") Long supID, final RedirectAttributes messageRetour){

        Etablissement etablissement = etablissementService.getById(supID);

        if(etablissement.getUsers().size()>0){

            log.info("Supression impossible   ---->   nbre de users concernées : "+etablissement.getUsers().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+etablissement.getUsers().size()+" users attachée(s)");
            return "redirect:/etablissement/list";   // c'est l'url du controleur
        }else{

            log.info("Supprime l'établissement "+etablissement.getNom()+ " " + etablissement.getId());
            etablissementService.delete(etablissement);

            //envoi un message vers la page HTML apres suppression
            //messageRetour.addFlashAttribute("SUPid",supID);
            messageRetour.addFlashAttribute("MESSAGE","Vous suprimmez l'établissement "+ etablissement.getNom());

            return "redirect:/etablissement/list";
        }
    }
}
