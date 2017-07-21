package fr.stac.rentes.controller;

import fr.stac.rentes.dao.ProfilDao;
import fr.stac.rentes.domain.Etablissement;
import fr.stac.rentes.domain.Profil;
import fr.stac.rentes.domain.User;
import fr.stac.rentes.service.EtablissementService;
import fr.stac.rentes.service.ProfilService;
import fr.stac.rentes.service.UserService;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC
 * Créé le  05/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class UserController {
    private UserService userService;
    private ProfilService profilService;
    private EtablissementService etablissementService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setProfilService(ProfilService profilService) {
        this.profilService = profilService;
    }

    @Autowired
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }
    @Autowired
    public void setProfilDao(ProfilDao profilDao) {
    }

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;}



    @GetMapping("/user/saisiemdp/{idu}")
    public String saisiemdp(Model model, @PathVariable("idu") Long ID){

        Collection<User> allUsers = userService.getAll();
        User user = userService.getById(ID);

        model.addAttribute("USERS",allUsers);
        model.addAttribute("USER",user);

        return "user/verifyuser";   // on appelle la page HTML que l'on a préparée dans /templates/user/verifyuser.HTML
    }


    @RequestMapping(value = "/user/compare")
    public String compareMdp(Model model, @ModelAttribute("USER") @Valid User user,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            log.info("Erreurs dans formulaire de changement de mot de passe ");
            return "menu";   // retour page menu.html de menu
        }else{

            User bdduser = userService.getById(user.getId());

            // création de l'objet  passwordEncodder qui contient la methode pour encoder <encode>
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // compare ancien mot de passe saisi et mot de passe ancien crypté issu de la BDD
            if (passwordEncoder.matches(user.getOldmdp(), bdduser.getMdp()) ) {

                if(user.getMdp().equals(user.getMdpconf())){

                    //création de l'objet  passwordEncodder qui contient la methode pour encoder <encode>
                    //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                    String hashedPassword = passwordEncoder.encode(user.getMdp());
                    bdduser.setMdp(hashedPassword);

                    userService.save(bdduser);
                    redirectAttributes.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+user.getNom()+" effectuée");

                    return "menu";   // retour page menu.html de menu

                }else{

                    String mess = "Vos mots de passe ne sont pas identiques, veuillez recommencer ";
                    redirectAttributes.addFlashAttribute("MESSAGE",mess);

                    return "redirect:/user/saisiemdp/"+user.getId();  // on retourne directement dans la saisie
                }
            }
            else {
                redirectAttributes.addFlashAttribute("MESSAGE","Votre ancien Mot de passe est incorrect... ");
                return "redirect:/user/saisiemdp/"+user.getId();  // on retourne directement dans la saisie
            }
        }
    }


    @GetMapping("/user/get/{iduser}")
    public String get(Model model, @PathVariable("iduser") Long ID,
                          final RedirectAttributes messageRetour){

        Collection<User> allUsers = userService.getAll();
        User user = userService.getById(ID);

        // on ne prend que les profils non enocre attribués à cet utilisateur
        //Collection<Profil> allProfils = profilService.getAllProfilsNonAtt(ID);

        Collection<Profil> allProfils = profilService.getAll();

        List<Etablissement> allEtablissements = etablissementService.getAll();

        model.addAttribute("PROFILS", allProfils);
        model.addAttribute("USER", user);           //le model est virtuel et est construit a chaque appel
        model.addAttribute("USERS", allUsers);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("ETABLISSEMENTS", allEtablissements);

        model.addAttribute("DISABLEBTN", false);             // active le bouton   Ajout/modif

        //envoi un message vers la page HTML apres suppression
        //messageRetour.addFlashAttribute("SUPid",supID);

        String mess = "Vous modifiez les données de : "+user.getNom();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "user/user";            // on repart sur la page HTML qui sera rafraichie
    }

    @GetMapping("/user/list/{choix}")
    public String list(Model model,@PathVariable("choix") String choix){

        List<User> allUsers = userService.getAll();
        List<Profil> allProfils = profilService.getAll();
        List<Etablissement> allEtablissements = etablissementService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        User user = new User();
        user.setId(0L);          // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("ETABLISSEMENTS", allEtablissements);
        model.addAttribute("USERS", allUsers);        // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("PROFILS", allProfils);
        model.addAttribute("USER", user);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        if(choix.equals("PW")){
            log.info("CHOIX 2 : "+choix);
            return "user/verifyuser";           // page HTML pour changement mot de passe

        }else{
            log.info("CHOIX 3 : "+choix);
            return "user/user";                 // page HTML pour modification ou saisie utilisateur
        }
    }


    //methode avec controle de saisie et mappage de Objet saisi "UserForm" vers Objet definitif "User"
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user/add")
    public String add(Model model, @ModelAttribute("USER") @Valid User user,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.error(mapper.toString());

        Collection<User> allUsers = userService.getAll();
        Collection<Profil> allProfils = profilService.getAll();
        Collection<Etablissement> allEtablissements = etablissementService.getAll();

        model.addAttribute("ETABLISSEMENTS", allEtablissements);
        model.addAttribute("PROFILS", allProfils);
        model.addAttribute("USERS", allUsers);
        model.addAttribute("bindingResult",bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie "+ user.getNom());
            return "user/user";
        } else {

            // on map du formulaire de saisie vers le user definitif a enregistrer
            // ici cryptage de passwd avan save dans BDD

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String monPassword = passwordEncoder.encode(user.getMdp());

            if(user.getId() <= 0 || user.getId() == null ){
                log.info("Element encode NEW : " + monPassword);
                user.setMdp(monPassword);
            }else {

                if (user.getMdp() != null && user.getMdp().length() != 0 ) {

                    log.info("Element encode MODIFIE : " + monPassword);
                    user.setMdp(monPassword);

                } else {

                    // on considere qu'il n'y a pas eu de saisie nouvelle
                    // compare mot de passe saisi et mot de passe déjà crypté
                    User uu = new User();
                    uu = userService.getById(user.getId());
                    user.setMdp(uu.getMdp());

                    log.info("Element remis : " + user.getMdp());
                }
            }

            userService.save(user);
            redirectAttributes.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+user.getNom()+" effectuée");

            return "redirect:/user/list/US";
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/delete/{iduser}")
    public String supUser(@PathVariable("iduser") Long supID,
                          final RedirectAttributes messageRetour){
        User user = userService.getById(supID);

        userService.delete(user);

        messageRetour.addFlashAttribute("MESSAGE","Vous avez supprimé : "+user.getNom());
        return "redirect:/user/list/US";
    }
}
