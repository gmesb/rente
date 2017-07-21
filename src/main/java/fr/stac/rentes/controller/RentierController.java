package fr.stac.rentes.controller;

import fr.stac.rentes.domain.*;
import fr.stac.rentes.service.*;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.*;
import static java.util.Objects.isNull;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC
 * Créé le  13/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class RentierController {

    private UserService userService;
    private GradeService gradeService;
    private TitulaireService titulaireService;
    private RentierService rentierService;
    private VersemtypeService  versemtypeService;
    private PreuveService preuveService;
    private RenteService renteService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setGradeService(GradeService gradeService) {
        this.gradeService = gradeService;
    }
    @Autowired
    public void setTitulaireService(TitulaireService titulaireService) {
        this.titulaireService = titulaireService;
    }
    @Autowired
    public void setVersemtypeService(VersemtypeService versemtypeService) {  this.versemtypeService = versemtypeService; }
    @Autowired
    public void setRentierService(RentierService rentierService) {
        this.rentierService = rentierService;
    }
    @Autowired
    public void setPreuveService(PreuveService preuveService) {
        this.preuveService = preuveService;
    }
    @Autowired
    public void setRenteService(RenteService renteService) {  this.renteService = renteService;   }


    @GetMapping("/rentier/get/{id}")
    public String get(Model model,  @PathVariable("id") Long ID ,final RedirectAttributes messageRetour){

        Collection<Rentier> allRentiers = rentierService.getAll();
        Rentier rentier = rentierService.getById(ID);

        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        Collection<Grade> allGrades = gradeService.getAll();

        model.addAttribute("TITULAIRES", allTitulaires);
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("RENTIER", rentier);
        model.addAttribute("RENTIERS", allRentiers);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("DISABLEBTN", false);     // active le bouton   Ajout/modif

        log.info("Charge le rentier : " + rentier.getId()
                        +" "+ rentier.getNomfamille()+" "+ rentier.getPrenom()
                        +" "+ rentier.getDatenaissance()+" "+ rentier.getFindroit()
                        +" "+ rentier.getGrade().getLibel()+" "+ rentier.getTitulaire().getLibel()
                        +" "+ rentier.getBanque()
                       //// +" "+ rentier.getUser().getNom()
                       ////   +" "+ rentier.getUser().getId()
                );

        //envoi un message vers la page HTML apres suppression
        //messageRetour.addFlashAttribute("SUPid",supID);


        String mess = "Vous modifiez les données de : "+rentier.getNomfamille();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "rentier/rentier";            // on repart sur la page HTML qui sera rafraichie
    }


    // au premier coup, on arrive du menu
    @GetMapping("/rentier/listnom/{nomrentier}")
    public String listByName(Model model, @PathVariable("nomrentier") String nomrentier){

        Collection<Rentier> allRentiers;

        // si on a passe * em parametre, on recupere tout le monde sinon par le mom saisi partiellement
        if(nomrentier.equals("*")) {
            allRentiers = rentierService.getAll();}
        else{
            allRentiers = rentierService.getNomComme(nomrentier); }


        Collection<User> allUsers = userService.getAll();
        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        Collection<Grade> allGrades = gradeService.getAll();
        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getByIdentif(username);

        log.info("------------------------------------------------------------------------------------------");
        log.info("USER QQ : "+user.getIdentif() +" "+user.getId());
        log.info("------------------------------------------------------------------------------------------");

        log.info("RENTES TROUVEES ");

        Rente rente = new Rente();
        rente.setEtatrente(new Date());

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("USERS", allUsers);
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("RENTE",rente);
        model.addAttribute("VERSEMTYPES",allVersemtypes);

        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        // on cache la fenetre d saisie de nouvelle rente
        model.addAttribute("HiddenSaisie",true);

        return "rentier/newrente";  // c'est la page newrente.html
    }


    // au premier coup, on arrive du menu
    @GetMapping("/rentier/listnomForRevalorisation/{nomrentier}")
    public String listByNameForRevalorisation(Model model, @PathVariable("nomrentier") String nomrentier) {

        Collection<Rentier> allRentiers;

        switch (nomrentier) {
            case "*":                    //   ICI on gére les versements Nouvelles ou modifiées
                allRentiers = rentierService.getAll();
                break;

            case "px3":                    //   ICI on gére les versements Nouveaux ou modifiées patients presents
                allRentiers = rentierService.getPresents();
                break;

            default:
                log.info("Je passe dans 'defaut'");
                allRentiers = rentierService.getNomComme(nomrentier);
                break;
        }

        userService.getAll();
        titulaireService.getAll();
        gradeService.getAll();
        versemtypeService.getAll();

        //List<Rente> allRentes = renteService.getAll();


        Renterevalorisee renterevalorisee = new Renterevalorisee();
        renterevalorisee.setId(0L);
        renterevalorisee.setDatelancement(new Date());
        model.addAttribute("RENTEREVALORISEE", renterevalorisee);


        /*
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("USERS", allUsers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);*/

        //model.addAttribute("RENTES", allRentes);

        model.addAttribute("RENTIERS", allRentiers);

        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        // on cache la fenetre de saisie du versement
        model.addAttribute("HiddenSaisie", true);           // true = Saisie cachee

        return "rentier/newrenterevalorisee";  // c'est la page newversement.html qui est lancee
    }



    // au premier coup, on arrive du menu
    @GetMapping("/rentier/listnomForVersement/{nomrentier}")
    public String listByNameForVersement(Model model, @PathVariable("nomrentier") String nomrentier) {

        Collection<Rentier> allRentiers;

        switch (nomrentier) {
            case "*":                    //   ICI on gére les versements Nouvelles ou modifiées
                allRentiers = rentierService.getAll();
                break;

            case "px3":                    //   ICI on gére les versements Nouveaux ou modifiées patients presents
                allRentiers = rentierService.getPresents();
                break;

            default:
                log.info("Je passe dans 'defaut'");
                allRentiers = rentierService.getNomComme(nomrentier);
                break;
        }

        userService.getAll();
        titulaireService.getAll();
        gradeService.getAll();
        versemtypeService.getAll();

        // List<Rente> allRentes = renteService.getAll();


        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getByIdentif(username);*/

        Versement versement = new Versement();
        versement.setId(0L);
        versement.setDateversem(new Date());
        model.addAttribute("VERSEMENT", versement);

        /*
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("USERS", allUsers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);*/

        //model.addAttribute("RENTES", allRentes);

        model.addAttribute("RENTIERS", allRentiers);


        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        model.addAttribute("HiddenSaisie",true);   // on cache la fenetre d saisie de nouvelle rente
        model.addAttribute("HiddenReval",true);    // on cache la fenetre des revalorisations
        model.addAttribute("HiddenRente",true);    // on cache la fenetre Des rentes


        return "rentier/newversement";  // c'est la page newversement.html qui est lancee
    }




    // au premier coup, on arrive du menu
    @GetMapping("/rentier/listnomForRente/{nomrentier}")
    public String listByNameForRente(Model model, @PathVariable("nomrentier") String nomrentier) {

        Collection<Rentier> allRentiers;

        switch (nomrentier) {
        case "*":                    //   ICI on gére les rentes Nouvelles ou modifiées
            log.info("Je passe dans RENTIER '*'");
            allRentiers = rentierService.getAll();
            break;

        case "px3":                    //   ICI on gére les rentes Nouvelles ou modifiées patients presents
            log.info("Je passe dans RENTIER 'PRESENTS'");

            model.addAttribute("HiddenRente", true);        // on cache la fenetre de saisie de nouvelle rente
            allRentiers = rentierService.getPresents();
            break;

        default:
            log.info("Je passe dans 'RENTIER defaut'");
            allRentiers = rentierService.getNomComme(nomrentier);
            break;
        }

        Collection<User> allUsers = userService.getAll();
        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        Collection<Grade> allGrades = gradeService.getAll();
        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getByIdentif(username);

        log.info("------------------------------------------------------------------------------------------");
        log.info("USER : " + user.getIdentif() + " " + user.getId());
        log.info("------------------------------------------------------------------------------------------");

        Rente rente = new Rente();
        rente.setId(0L);
        rente.setEtatrente(new Date());

        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("USERS", allUsers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);

        model.addAttribute("RENTE", rente);
        model.addAttribute("RENTIERS", allRentiers);

        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        // on cache la fenetre de saisie de nouvelle rente
        model.addAttribute("HiddenSaisie", true);

        return "rentier/newrente";  // c'est la page newrente.html qui est lancee
    }



    // au premier coup, on arrive du menu
    @GetMapping("/rentier/listnomForPreuvevie/{nomrentier}")
    public String listByNameForPreuvevie(Model model, @PathVariable("nomrentier") String nomrentier) {

        Collection<Rentier> allRentiers;

        switch (nomrentier) {
            case "*":                    //   ICI on gére les rentes Nouvelles ou modifiées
                log.info("Je passe dans '*'");
                allRentiers = rentierService.getAll();
                break;

            case "px3":                    //   ICI on gére les rentes Nouvelles ou modifiées patients presents
                log.info("Je passe dans 'PRESENTS'");
                allRentiers = rentierService.getPresents();
                break;

            default:
                log.info("Je passe dans 'defaut'");
                allRentiers = rentierService.getNomComme(nomrentier);

                break;
        }

        Collection<User> allUsers = userService.getAll();
        Collection<Titulaire> allTitulaires = titulaireService.getAll();
        Collection<Grade> allGrades = gradeService.getAll();
        Collection<Preuve> allPreuves = preuveService.getAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.getByIdentif(username);

        Preuvevie preuvevie = new Preuvevie();
        preuvevie.setId(0L);
        preuvevie.setDatedemande(new Date());           // on met la date du jour

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("PREUVES", allPreuves);
        model.addAttribute("USERS", allUsers);

        model.addAttribute("PREUVEVIE", preuvevie);

        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        // on cache la fenetre d saisie de nouvelle rente
        model.addAttribute("HiddenSaisie", true);

        return "user/preuvevie";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/rentier/list")
    public String list(Model model){

        List<Rentier> allRentiers = rentierService.getAll();
        List<User> allUsers = userService.getAll();
        List<Titulaire> allTitulaires = titulaireService.getAll();
        List<Grade> allGrades = gradeService.getAll();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getByIdentif(username);

        log.info("------------------------------------------------------------------------------------------");
        log.info("USER : "+user.getIdentif() +" "+user.getId());
        log.info("------------------------------------------------------------------------------------------");

        Rentier rentier = new Rentier();
        rentier.setUser(user);
        rentier.setId(0L);          // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("USERS", allUsers);
        model.addAttribute("GRADES", allGrades);
        model.addAttribute("TITULAIRES", allTitulaires);    // le model est virtuel et est coxstruit a chaque appel
        model.addAttribute("RENTIER", rentier);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif



        return "rentier/rentier";  // c'est la page rentier.html
    }



    //methode avec controle de saisie et mappage de Objet saisi "RentierForm" vers Objet definitif "Rentier"
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/rentier/add")
    public String add(Model model ,@ModelAttribute("RENTIER") @Valid Rentier rentier,
                      BindingResult bindingResult, RedirectAttributes messages){

        log.error(mapper.toString());

        List<Rentier> allRentiers = rentierService.getAll();
        List<Titulaire> allTitulaires = titulaireService.getAll();
        List<Grade> allGrades = gradeService.getAll();

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("TITULAIRES", allTitulaires);
        model.addAttribute("GRADES", allGrades);

        log.info("Form saisie xxx " + rentier.getId()
                +" | "+ rentier.getNomfamille()+" | "+ rentier.getPrenom()
                +" | "+ rentier.getDatenaissance()+" | "+ rentier.getFindroit()
                +" | "+ rentier.getGrade().getLibel()+" | "+ rentier.getTitulaire().getLibel()
                +" | "+ rentier.getBanque()
                +" | "+ rentier.getUser().getNom()+"|"
        );

        // Attention j'ai rajouté
        //model.addAttribute("bindingResult",bindingResult);

        if(bindingResult.hasErrors()) {

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

            log.info("Erreur de saisie "+ rentier.getNomfamille());
            return "rentier/rentier";    // c'est la page rentier.html

        } else {

            log.info("Form saisie ::: " + rentier.getId()
                    +" | "+ rentier.getNomfamille()+" | "+ rentier.getPrenom()
                    +" | "+ rentier.getDatenaissance()+" | "+ rentier.getFindroit()
                    +" | "+ rentier.getGrade().getLibel()+" | "+ rentier.getTitulaire().getLibel()
                    +" | "+ rentier.getBanque()
                    +" | "+ rentier.getUser().getNom()+"|"
            );


            if( isNull(rentier.getUser()) ){

                // on recupère l'utilisateur loggé
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userService.getByIdentif(username);
                log.info("USER : "+user.getIdentif() +" "+user.getId());

                rentier.setUser(user);

            }

            rentierService.save(rentier);


            messages.addFlashAttribute("MESSAGE","Insertion ou MAJ "+rentier.getNomfamille()+" effectuée");
            return "redirect:/rentier/list";               //  url dans le controleur
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/rentier/delete/{id}")
    public String sup(@PathVariable("id") Long supID, final RedirectAttributes messageRetour){

        Rentier rentier = rentierService.getById(supID);

        log.info("Form DELETE ::: " + rentier.getId()
                +" | "+ rentier.getNomfamille()+" | "+ rentier.getPrenom()
                +" | "+ rentier.getDatenaissance()+" | "+ rentier.getFindroit()
                +" | "+ rentier.getGrade().getLibel()+" | "+ rentier.getTitulaire().getLibel()
                +" | "+ rentier.getBanque()
                +" | "+ rentier.getUser().getNom()
                +" | "+ rentier.getGrade().getLibel()
                +" | "+ rentier.getTitulaire().getLibel()
        );

        //int nbRentiers = renteService.countRentiers(rentier);

        if(renteService.countRentiers(rentier) > 0 ) {
            //envoi un message vers la page HTML car suppression impossible

            log.info("Erreur, il y a  : "+renteService.countRentiers(rentier));

            messageRetour.addFlashAttribute("MESSAGE","Suppression impossible : "+rentier.getNomfamille());

        }else{

            rentierService.delete(rentier);

            log.info("Supprime l'utilisateur"+rentier.getNomfamille());

            //envoi un message vers la page HTML apres suppression
            messageRetour.addFlashAttribute("MESSAGE","Vous avez supprimé : "+rentier.getNomfamille()+"  "+rentier.getPrenom());

        }

        return "redirect:/rentier/list";
   }
}
