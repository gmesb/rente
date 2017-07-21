package fr.stac.rentes.controller;

import fr.stac.rentes.domain.Grade;
import fr.stac.rentes.service.GradeService;
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
 * Pour STAC (Direction Générale de l'Aviation Civile )
 *
 * Créé le  02/04/2017.
 *
 * @author Antonio RODRIGUES
 */


@Controller
public class GradeController {

    private GradeService gradeService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }


    /**
     * Methode controller : l'URL de chargement de la page HTML permettant de gérer la liste des Grades
     * Suppression, modification, Insertion sont les actions possibles  sur cette entité
     * @param model On fait référence au modèle de données du MVC
     * @param ID Le paramètre passé, selectionné dans un élement de la page HTML et récupéré pour traitement
     * @param redirectAttributes La variable contenant les message de retour vers la page HTML
     * @return Redirection vers la page HTML Grade
     */
    @GetMapping("/grade/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,
                            final RedirectAttributes redirectAttributes){

        Collection<Grade> allGrades = gradeService.getAll();
        Grade grade = gradeService.getById(ID);

        /**
         * Ici on envoie les données vers la feuille HTML
         * en liste : Tout le contenu de la table est présent
         * En formulaire de saisie/modification avec l'élement selectionné préalablement
         * DISABLEBTN est positionnée à false ce qui le rend actif
         */
        model.addAttribute("GRADE", grade);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("DISABLEBTN", false);   // active le bouton   Ajout/modif

        log.info("Modifie la grade : "
                +grade.getLibel()+" "+ grade.getId()
        );


        String mess = "Vous modifiez les données de : "+grade.getLibel();
        redirectAttributes.addFlashAttribute("MESSAGE",mess);

        return "user/grade";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/grade/list")
    public String list(Model model) {

        List<Grade> allGrades = gradeService.getAll();

        log.info("Liste Grades "+ allGrades );

        /**
         * on initialize pour ne pas avoir  la valeur 'null' dans l'ID lorsqu'il y aura persistance dans la base de données,
         * il y aura une nouvelle insertion dans la table
         */
        Grade grade = new Grade();
        grade.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        log.info("New Grade "+ grade.getId()+" "+ grade.getLibel()   );

        /**
         * Dans le template, la liste va être renseignée par le contenu total de "allGrades".
         * Le formulaire de saisie sera renseigné au minima avec le contenu de l'objet "grade" initializé par son ID =0
         * au cas où il y aurait une nouvelle insertion à faire.
         * "DISABLEBTN" recevra la valeur true pour rester désactif tant qu'une saisie dans le formulaire n'est pas effectuée
         */
        model.addAttribute("GRADES", allGrades);            // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("GRADE", grade);
        model.addAttribute("DISABLEBTN", true);         // desactive le bouton   Ajout/modif

        return "user/grade";                                // c'est la page HTML'
    }

    /**
     * Methode controller : Permetant la mise à jour ou l'insertion dans la base de données
     * La persistance est selectionnée  soit en mode insertion soit en Update par l'ID qui est ou non renseigné
     * ID = 0 il y a insertion    ID renseigné, il y aure Mise à jour dans la table.
     * @param model On fait référence au modèle de données du MVC pour le traitement de données
     * @param grade Est le paramètre Objet Grade échangé avec la Vue (Page HTML)
     * @param bindingResult Paramètre contenant l'erreur, si elle existe devra être traitée
     * @param redirectAttributes La variable contenant les messages de retour vers la page HTML
     * @return renvoi soit vers la page HTML user/grade  soit vers L'URL grade/list
     */
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/grade/add")
    public String add(Model model, @ModelAttribute("GRADE") @Valid Grade grade,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes){

        Collection<Grade> allGrades = gradeService.getAll();

        model.addAttribute("GRADES", allGrades);

       /* // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);
        */

        /**
         * Traitement des erruers de retour
         *
         */
        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie : "+ grade.getLibel()+"  "+grade.getId() );
             return "user/grade";

        } else {

            // on map du formulaire de saisie vers le grade definitif a enregistrer
            log.info("insert Reval "+ grade.getId()+" "+ grade.getLibel()   );

            gradeService.save(grade);

            redirectAttributes.addFlashAttribute("MESSAGE","Insertion ou MAJ de < "+grade.getLibel()+" > réussie");

            log.info("Ajout de grade "+grade.getLibel());
            return "redirect:/grade/list";
        }
    }


    /**
     * Methode de suppression des données de l'entité Grade
     *
     * @param supID L'id de l'élement à supprimer
     * @param redirectAttributes La variable contenant les messages de retour vers la page HTML
     * @return Retour à l'URL  /grade/list
     */
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @GetMapping("/grade/sup/{id}")
    public String sup(@PathVariable("id") Long supID,
                            final RedirectAttributes redirectAttributes){

        Grade grade = gradeService.getById(supID);

        /**
         * Test s'il y a des enfants, la suppression en cascade n'est pas autorisée
         */
        if(grade.getRentiers().size()>0){

            log.info("Supression impossible   ---->   nbre de users concernées : "+grade.getRentiers().size());
            redirectAttributes.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+grade.getRentiers().size()+" users attachée(s)");
            return "redirect:/grade/list";   // c'est l'url du controleur

        }else {

            gradeService.delete(grade);
            log.info("Supprime la grade" + grade.getLibel());
            redirectAttributes.addFlashAttribute("MESSAGE", "Suppression de < " + grade.getLibel() + " > effectuée");
            return "redirect:/grade/list";
        }
    }
}
