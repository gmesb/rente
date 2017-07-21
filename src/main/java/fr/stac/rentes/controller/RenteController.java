package fr.stac.rentes.controller;

import fr.stac.rentes.domain.*;
import fr.stac.rentes.service.*;
import org.dozer.Mapper;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC (Direction Générale de l'Aviation Civile)
 * Créé le  18/04/2017.
 *
 * @author Antonio
 */

@Controller
public class RenteController {

    private RenteService renteService;
    private RentierService rentierService;
    private VersemtypeService versemtypeService;
    private RenterevaloriseeService renterevaloriseeService;
    private VersementService versementService;


    // Display a date in day, month, year format
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;


    @Autowired
    public void setRentierService(RentierService rentierService) {
        this.rentierService = rentierService;
    }
    @Autowired
    public void setRenteService(RenteService renteService) {  this.renteService = renteService;  }

    @Autowired
    public void setVersemtypeService(VersemtypeService versemtypeService) { this.versemtypeService = versemtypeService;   }
    @Autowired
    public void setRenterevaloriseeService(RenterevaloriseeService renterevaloriseeService) { this.renterevaloriseeService = renterevaloriseeService;}
    @Autowired
    public void setRevalorisationService(RevalorisationService revalorisationService) { }
    @Autowired
    public void setVersementService(VersementService versementService) { this.versementService = versementService;   }

    public static int getLastDayInMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int maxDay = calendar.getActualMaximum(Calendar.DATE);
        return maxDay;
    }

    // ajouter ou retirer nombre de jours a une date ( negatif pour retirer )
    public static Date ajouterJour(Date maDate, int nbJour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(maDate);
        cal.add(Calendar.DATE, nbJour);
        return cal.getTime();
    }

    // retourne une date à partir du JJ MM AAAA passes en parametre
    public static Date getDateAAAAMMJJ(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }


    @GetMapping("/rente/listRentes/{idRentier}/{choix}")
    public String ListRentesByRentier(Model model,
                                      @PathVariable ("idRentier") Long idRentier,
                                      @PathVariable ("choix") String choix){

        Rentier rentier = rentierService.getById(idRentier);

        List<Rente> allRentes = renteService.findAllByRentierOrderByDateDesc(rentier);

        List<Rentier> allRentiers = rentierService.getAll();
        versementService.getAll();
        List<Versemtype> allVersemtypes =versemtypeService.getAll();

        Rente rente = new Rente();
        rente.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        rente.setRentier(rentier);

        String ident = rentier.getNomfamille()+" " +rentier.getPrenom()+ " né(e) le "+ formatter.format(rentier.getDatenaissance());

        model.addAttribute("RENTIERS", allRentiers);

        Versement versement = new Versement();
        versement.setId(0L);
        versement.setDateversem(new Date());
        model.addAttribute("VERSEMENT", versement);


        model.addAttribute("VERSEMTYPES", allVersemtypes);
        model.addAttribute("RENTE", rente);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif
        model.addAttribute("IDENTRENTIER",ident);

        //TODO je dois verifier pourqoui > 10 fonctione et pas >0 ??????

        if (allRentes.toString().length() >5 ){
            log.info("des rentes pour ce rentier");

            model.addAttribute("RENTES", allRentes);
        }

        log.info("CHOIX : "+choix);

        switch (choix) {
        case "addreval":
            log.info("Je passe dans REVALO 'addrevalorisation'");
            model.addAttribute("HiddenSaisie", true);      // true = desactive le formulaire
            return "rentier/newrenterevalorisee";                 // c'est la page newversement.html
                                                               // break;   en comment car Il y a déjà un RETURN

        case "addversem":
            log.info("Je passe dans RENTE 'addversement'");

            model.addAttribute("HiddenSaisie",true);   // on cache la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenReval",true);    // on cache la fenetre des revalorisations
            model.addAttribute("HiddenRente",false);    // on active la fenetre Des rentes

            return "rentier/newversement";      // c'est la page newversement.html
            // break;   en comment car Il y a déjà un RETURN

        case "addrente":
            log.info("Je passe dans RENTE 'addversement'");
            model.addAttribute("HiddenSaisie", false);      // false = active le formulaire
            model.addAttribute("HiddenRente", false);      // false = active le formulaire

            return "rentier/newrente";   // c'est la page newrente.html
            // break;   en comment car Il y a déjà un RETURN

        default:
            log.info("Je passe dans RENTE 'defaut'");
            model.addAttribute("HiddenSaisie", true);      // true = desactive le formulaire

            return "rentier/newversement";      // c'est la page newversement.html
            // break;
        }
    }


    @GetMapping("/rente/getnewrente/{id}")
    public String getnewrente(Model model,  @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){

        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();
        Collection<Rentier> allRentiers = rentierService.getAll();

        Rente rente = renteService.getById(ID);
        Rentier rentier = rentierService.getById(rente.getRentier().getId());

        Collection<Rente> allRentes = renteService.findAllByRentierOrderByDateDesc(rentier);

        String ident = rentier.getNomfamille()+" " +rentier.getPrenom()+ " né(e) le "+ formatter.format(rentier.getDatenaissance());

        model.addAttribute("RENTE", rente);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("VERSEMTYPES", allVersemtypes);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("DISABLEBTN", false);            // active le bouton   Ajout/modif

        model.addAttribute("IDENTRENTIER",ident);

        String mess = "Vous modifiez les données de : "+rente.getLibel();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "rentier/newrente";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/rente/list/{idRentier}")
    public String rentesByRentier(Model model, @PathVariable ("idRentier") Long idRentier){

        Rentier rentier = rentierService.getById(idRentier);

        List<Rente> allRentes = renteService.findAllByRentierOrderByDateDesc(rentier);
        List<Rentier> allRentiers = rentierService.getAll();
        List<Versemtype> allVersemtypes =versemtypeService.getAll();

        log.info("Rentes trouvées : "+allRentes);
        log.info("Charger le rentier ------------: " + rentier.getId()
                +" "+ rentier.getNomfamille()
                +" "+ rentier.getUser().getNom()

        );

        Rente rente = new Rente();
        rente.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        //rente.setEtatrente(new Date());           // date du jour  PAS BON SI EN CREATION DE RENTE doit rester a NULL
        rente.setRentier(rentier);

        String ident = rentier.getNomfamille()+" " +rentier.getPrenom()+ " né(e) le "+ formatter.format(rentier.getDatenaissance());

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);

        model.addAttribute("RENTE", rente);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        model.addAttribute("HiddenSaisieRente", false);      // Active le formulaire

        model.addAttribute("IDENTRENTIER",ident);

        //TODO je dois verifier pourqoui > 10 fonctione et pas >0 ??????

        if (allRentes.toString().length() >5 ){
            log.info("des rentes pour ce rentier OK "+allRentes);
            model.addAttribute("RENTES", allRentes);

        }

        return "rentier/newrente";   // c'est la page newrente.html
     }


    // ANCIENNE VERSION POUR TOUTES LES RENTES   LA BONNE  C'est  getnewrente
    @GetMapping("/rente/get/{id}")
    public String get(Model model,  @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){

        Collection<Rente> allRentes = renteService.getAll();
        Collection<Versemtype> allVersemtypes = versemtypeService.getAll();
        Collection<Rentier> allRentiers = rentierService.getAll();

        Rente rente = renteService.getById(ID);

        rentierService.getById(rente.getRentier().getId());

        model.addAttribute("RENTE", rente);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("VERSEMTYPES", allVersemtypes);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("DISABLEBTN", false);            // active le bouton   Ajout/modif

        log.info("Charger le rente : " + rente.getId()
                        +" "+ rente.getLibel()
                        +" "+ rente.getDateaccident()
                        +" "+ rente.getTxippaycause()
                        +" "+ rente.getTxippaydroit()
                        +" "+ rente.getRentier().getNomfamille()
        );

        String mess = "Vous modifiez les données de : "+rente.getLibel();
        messageRetour.addFlashAttribute("message",mess);

        return "rentier/rente";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/rente/list")
    public String list(Model model){
        List<Rente> allRentes = renteService.getAll();
        List<Rentier> allRentiers = rentierService.getAll();
        List<Versemtype> allVersemtypes =versemtypeService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Rente rente = new Rente();
        rente.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("RENTE", rente);

        model.addAttribute("DISABLEBTN", true);         // desactive le bouton   Ajout/modif

        return "rentier/rente";  // c'est la page rente.html
    }


    //methode avec controle de saisie et mappage de Objet saisi "RentierForm" vers Objet definitif "Rente"
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/rente/add")
    public String add(@ModelAttribute("RENTE") @Valid final Rente rente,
                      final BindingResult bindingResult, RedirectAttributes messages, final Model model ){

        log.error(mapper.toString());

        List<Rente> allRentes = renteService.findAllByRentierOrderByDateDesc(rente.getRentier());
        List<Versemtype> allVersemtypes = versemtypeService.getAll();
        List<Rentier> allRentiers = rentierService.getAll();

        model.addAttribute("RENTES", allRentes);                    // seulement celles du rentier
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("VERSEMTYPES", allVersemtypes);


        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        // on cache la fenetre d saisie de nouvelle rente
        model.addAttribute("HiddenSaisie",true);

        log.info(" --------------------------    ------  Form saisie Rente   Erreur : "+bindingResult
                +" "+rente.getId()
                +" "+rente.getLibel()
                +" "+rente.getDateaccident()
                +" "+rente.getTxippaycause()
                +" "+rente.getTxippaydroit()
                +" "+rente.getRentier().getNomfamille()
         );

        //Attention j'ai rajouté pour renvoyer sur le model HTML
        //model.addAttribute("bindingResult",bindingResult);

        // on renvoi seulement les rentes du rentier traité
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()
                       +" né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);           // paramètre pour affichier l'identité du rentier dans le FORM


        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie "+ rente.getLibel());
            //return "redirect:/rentier/newrente";      // c'est la page newrente.html

            // on laisse la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie",false);

            return "/rentier/newrente";      // c'est la page newrente.html

        } else {

            log.info("Form saisie :Rente:: " + rente.getId()
                    +" "+ rente.getLibel()
                    +" "+ rente.getDateaccident()
                    +" "+ rente.getTxippaycause()
                    +" "+ rente.getTxippaydroit()
                    +" "+ rente.getRentier().getNomfamille()
            );
            //String today = formatter.format(rente.getDateaccident());

            int insertRevalorisation = (int) rente.getId();

            renteService.save(rente);

            messages.addFlashAttribute("MESSAGE","Insertion ou MAJ "+rente.getLibel()+" effectuée");

            log.info("Rente : ID : "+ rente.getId()+"  INTREVALORISATION : "+insertRevalorisation+"  rente : " + rente.toString());

            // on insere une revalorisation pour permettre de faire les calculs de revalorisation dans une boucle
            // sans avoir à traiter le cas de 1ere fois


            if ( insertRevalorisation == 0 ){  // on utilise une variable insertRevalorisation car le ID de rente evolue apres le Insert

                Renterevalorisee renterevalorisee = new Renterevalorisee();
                Revalorisation revalorisation= new Revalorisation();

                revalorisation.setId(1l);
                renterevalorisee.setId(0L);

                log.info("Revalorisation NEW 2 : " + revalorisation.toString()
                           +" Renterevalorisee : " + renterevalorisee.toString())
                ;

                renterevalorisee.setDatelancement(new Date());
                renterevalorisee.setMontantrevalorise(rente.getMntrenteinitial());
                renterevalorisee.setRente(rente);
                renterevalorisee.setRevalorisation(revalorisation);
                renterevalorisee.setDatedeb(rente.getDateconsolidation());

                // on positionne le 31/12/3000 en date de fin
                renterevalorisee.setDatefin(getDateAAAAMMJJ(3000,12,31));

                log.info("Renterevalorisee NEW : "+renterevalorisee.toString());

                renterevaloriseeService.save(renterevalorisee);
            }

            // on laisse la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie",true);

            return "redirect:/rentier/listnom/*";  // c'est l'url du controleur
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rente/delete/{id}")
    public String sup(@PathVariable("id") Long supID,final RedirectAttributes messageRetour){

        /* @ModelAttribute("RENTE") @Valid Rente rente, BindingResult bindingResult, */

        Rente rente = renteService.getById(supID);

        // on verifie l'existance de revalorisations associée à la rente
        int nbRentes  = renterevaloriseeService.countRentes(rente);

        log.info("Nombre de revalorisations : "+nbRentes+"   Rente ----------------- : "+rente.toString());

        if(nbRentes > 0 ) {
            //envoi un message vers la page HTML car suppression impossible
            log.info("Supression impossible   ------->    nbre de revalorisations : "+nbRentes);
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+nbRentes+" revalorisatios attachée(s) : " + rente.getLibel());

            //return "rentier/newrente";            // on repart sur la page HTML qui sera rafraichie

            //return "redirect:/rentier/listnom/*";                     // c'est l'url du controleur
            return "redirect:/rente/list/"+rente.getRentier().getId();           // c'est l'url du controleur

        }else {

            renteService.delete(rente);

            log.info("Supprime la rente " + rente.getLibel());

            //envoi un message vers la page HTML apres suppression
            //messageRetour.addFlashAttribute("SUPid",supID);
            messageRetour.addFlashAttribute("MESSAGE", "Vous avez supprimé : " + rente.getLibel());

            return "redirect:/rentier/listnom/*";       // c'est l'url du controleur
        }
        //return "redirect:/rente/list";
    }
}
