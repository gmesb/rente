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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC
 * Créé le  18/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class RenterevaloriseeController {
    private RenterevaloriseeService renterevaloriseeService;
    private RenteService renteService;
    private RevalorisationService revalorisationService;
    private RentierService rentierService;
    // Display a date in day, month, year format
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    // Display a date in Année, mois, jour format(AAAA-MM-JJ)
    private DateFormat formatBDD = new SimpleDateFormat("yyyy-MM-dd");

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;

    @Autowired
    public void setRenterevaloriseeService(RenterevaloriseeService renterevaloriseeService) {
        this.renterevaloriseeService = renterevaloriseeService;
    }

    @Autowired
    public void setRenteService(RenteService renteService) {
        this.renteService = renteService;
    }
    public RenteService getRenteService() {      return renteService;    }

    @Autowired
    public void setRevalorisationService(RevalorisationService revalorisationService) {
        this.revalorisationService = revalorisationService;
    }
    @Autowired
    public void setRentierService(RentierService rentierService) {
        this.rentierService = rentierService;
    }
    @Autowired
    public void setVersementService(VersementService versementService) {
    }

    // ajouter nombre de jours a une date
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


    @GetMapping("/renterevalorisee/boucle")
    public String boucleRentes(Model model) {

        Versemtype versemtype = new Versemtype();
        versemtype.setId(3L);


        List<Rente> rentesValides = renteService.allRentesPourRevalorisation(versemtype);  //  PAs de Forfait versemtype <>3

        //List<Rente> rentesValides = renteService.findAllByEtatrentePresent(1L);      // Une rente  28

        log.info("Rentes concernées : " + rentesValides.toString());

        //  on commence la boucle
        model.addAttribute("HiddenAffiche", true);      // Desactive le formulaire


        for (Rente uneRente : rentesValides) {

            String datFin = "";

            if(uneRente.getDatefinrente() == null ){
                uneRente.setDatefinrente(getDateAAAAMMJJ(3000,12,31));

                log.info("Date EtatRente NULL  remis : "+uneRente.getId()+"   "+uneRente.getEtatrente()+" "+uneRente.getDatefinrente()
                + " CONSOLIDATION : " + uneRente.getDateconsolidation());

                datFin = "3000-12-31";
            }else{
                datFin = formatBDD.format(uneRente.getDatefinrente());
            }

            Date dateFinRente = new Date();

            // on transforme la chaine DatFin en type  Date
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateFinRente = dt.parse(datFin);
            } catch (ParseException e) {   e.printStackTrace();      }

            String datDebut = formatBDD.format(uneRente.getDateconsolidation());

            log.info("ID  +  Dates : "+uneRente.getId()+"    "+uneRente.getDateconsolidation()+"    "+dateFinRente +"   "+datDebut);



            // toutes les revalorisations non inclues dans cette rente et comprises dans les dates
           /* List<Revalorisation> revalorisations = revalorisationService.getAllRevalorisationNotIn(uneRente.getDateconsolidation(),uneRente.getDatefinrente());
*/

            List<Revalorisation> revalorisations = revalorisationService.getRevalorisationNonValidee(uneRente.getId(),datDebut,datFin);


            log.info("Revalorisations a appliquer : " + revalorisations.toString());


            // nouvelle rente crée
            Renterevalorisee NEWrenterevalorisee = new Renterevalorisee();


            for (Revalorisation uneRevalorisation : revalorisations) {

                String dateBDD = formatBDD.format(uneRevalorisation.getDaterevalorisation());
                log.info("Date " + dateBDD);
                log.info("Revalorisation EN COURS : " + uneRevalorisation);
                log.info("Rente EN COURS : " + uneRente);


                // on charge la derniere revalorisation positionnée par défaut la 1 a un  coeff à 1.00 neutre
                Renterevalorisee MAXrenterevalorisee = renterevaloriseeService.findPlusRecente(uneRente);


                log.info("MAX rente :" + MAXrenterevalorisee.getDatedeb()
                        + " " + MAXrenterevalorisee.getDatefin()
                        + " " + MAXrenterevalorisee.getMontantrevalorise()
                );


                NEWrenterevalorisee.setDatelancement(new Date());       // date du jour

                NEWrenterevalorisee.setRevalorisation(uneRevalorisation);
                NEWrenterevalorisee.setRente(uneRente);

                NEWrenterevalorisee.setMontantrevalorise(MAXrenterevalorisee.getMontantrevalorise());
                NEWrenterevalorisee.setId(0L);

                NEWrenterevalorisee.setDatedeb(uneRevalorisation.getDaterevalorisation());

                NEWrenterevalorisee.setDatefin(uneRente.getDatefinrente());


                log.info("Coeff av Modif : " + NEWrenterevalorisee.getMontantrevalorise()
                        + " " + NEWrenterevalorisee.getDatelancement()
                        + " " + NEWrenterevalorisee.getId()
                );

                NEWrenterevalorisee.setMontantrevalorise(MAXrenterevalorisee.getMontantrevalorise() * uneRevalorisation.getCoeff());

                // on persiste dans la BDD
                renterevaloriseeService.save(NEWrenterevalorisee);


                MAXrenterevalorisee.setDatefin(ajouterJour(NEWrenterevalorisee.getDatedeb(), -1));

                // la date de fin < date debut si on a une revalorisation au même jour que debut de Rente
                // On ne peut pas aller chercher jour - 1

                if (MAXrenterevalorisee.getDatefin().compareTo(MAXrenterevalorisee.getDatedeb()) < 0) {
                    // on met date debut = date de fin cela fera 0 JOurs à facturer et on applique la revalorisation suivante
                    MAXrenterevalorisee.setDatefin(MAXrenterevalorisee.getDatedeb());
                }


                /**
                 * on modifie la revalorisation précédente qui était à 3000-MM-AA
                 * on met à J - 1
                 * puis on persite dans la BDD
                 */
                renterevaloriseeService.save(MAXrenterevalorisee);      // on persiste
            }
        }
        model.addAttribute("HiddenAffiche", true);      // Desactive le formulaire

        return "menu";                // on repart sur la page HTML de la nav pricipale qui sera rafraichie
    }


    @GetMapping("/renterevalorisee/get/{id}")
    public String get(Model model, @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){


        Renterevalorisee renterevalorisee = renterevaloriseeService.getById(ID);

        log.info("SELECTIONNEE "+renterevalorisee.toString());


        Rente rente = renteService.getById(renterevalorisee.getRente().getId());
        Rentier rentier = rentierService.getById(rente.getRentier().getId());

        List<Renterevalorisee> allRenterevalorisee = renterevaloriseeService.getAllByRente(rente);

        List<Rente> allRente = renteService.getAllRentesByRentier(rentier);
        List<Revalorisation> allRevalorisations = revalorisationService.getAll();
        List<Rentier> allRentiers = rentierService.getAll();

        model.addAttribute("RENTIERS",allRentiers);
        model.addAttribute("RENTEREVALORISEE", renterevalorisee);
        model.addAttribute("RENTEREVALORISEES", allRenterevalorisee);
        model.addAttribute("RENTES", allRente);
        model.addAttribute("REVALORISATIONS", allRevalorisations);

        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);


        String mess = "Vous modifiez les données de : "+renterevalorisee.getDatelancement();
        messageRetour.addFlashAttribute("MESSAGE", mess);

        return "rentier/newrenterevalorisee";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/renterevalorisee/list")
    public String list(Model model){

        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAll();
        List<Rente> allRentes = renteService.getAll();
        List<Revalorisation> allRevalorisations = revalorisationService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Renterevalorisee renterevalorisee = new Renterevalorisee();
        renterevalorisee.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("REVALORISATIONS", allRevalorisations);
        model.addAttribute("RENTEREVALORISEE", renterevalorisee);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/renterevalorisee";       // c'est la page renterevalorisee.html qui est lancée
    }


    @GetMapping("/renterevalorisee/versemRenteReval/{idRente}/{choix}")
    public String versemRenteRevalByRente(Model model,
                                          @PathVariable ("idRente") Long idRente,
                                          @PathVariable ("choix") String choix) {

        Rente rente = renteService.getById(idRente);

        List<Rentier> allRentiers = rentierService.getAll();
        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAllByRente(rente);
        List<Rente> allRentes = renteService.getAllRentesForRentier(rente.getRentier());
        revalorisationService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Versement versement = new Versement();
        versement.setId(0L);           // 0 pour la valeur et L pour indiquer que c'est un type Long
        versement.setDateversem(new Date());
        versement.setMontant(0.0F);
        //versement.setRente(rente);


        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);

        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);

        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("RENTES", allRentes);
        /*model.addAttribute("REVALORISATIONS", allRevalorisations);*/
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        //model.addAttribute("HiddenSaisie", false);          // Active le formulaire

        switch (choix) {
        case "addreval":
            log.info("Je passe dans REVALO 'addrevalorisation'");
            model.addAttribute("HiddenSaisie", true);      // true = desactive le formulaire
            return "rentier/newrenterevalorisee";                 // c'est la page newversement.html
        // break;   en comment car Il y a déjà un RETURN

        case "addversem":
            log.info("Je passe dans RENTE 'addversement'");

            model.addAttribute("HiddenSaisie", true);      // true = desactive le formulaire
            return "rentier/newversement";      // c'est la page newversement.html
        // break;   en comment car Il y a déjà un RETURN

        case "addrente":
            log.info("Je passe dans RENTE 'addversement'");
            model.addAttribute("HiddenSaisie", false);      // false = active le formulaire
            return "rentier/newrente";   // c'est la page newrente.html
        // break;   en comment car Il y a déjà un RETURN

        default:
            log.info("Je passe dans RENTE 'defaut'");
            model.addAttribute("HiddenSaisie", true);      // true = desactive le formulaire
            return "rentier/newversement";      // c'est la page newversement.html
        // break;
        }

        //return "rentier/newversement";       // c'est la page renterevalorisee.html qui est lancée
    }


    @GetMapping("/renterevalorisee/listRenteReval/{idRente}")
    public String listRenteRevalByRente(Model model,@PathVariable ("idRente") Long idRente) {

        Rente rente = renteService.getById(idRente);

        List<Rentier> allRentiers = rentierService.getAll();
        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAllByRente(rente);
        List<Rente> allRentes = renteService.getAllRentesForRentier(rente.getRentier());
        List<Revalorisation> allRevalorisations = revalorisationService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Renterevalorisee renterevalorisee = new Renterevalorisee();
        renterevalorisee.setId(0L);           // 0 pour la valeur et L pour indiquer que c'est un type Long
        renterevalorisee.setDatelancement(new Date());
        renterevalorisee.setMontantrevalorise(0.0F);
        renterevalorisee.setRente(rente);


        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);


        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("REVALORISATIONS", allRevalorisations);
        model.addAttribute("RENTEREVALORISEE", renterevalorisee);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        model.addAttribute("HiddenSaisie", false);          // Active le formulaire


        /*if (allRenterevalorisees.toString().length() >10 ){
            log.info("des revalorisations pour cette rente");
            model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);
        }*/

        return "rentier/newrenterevalorisee";       // c'est la page renterevalorisee.html qui est lancée
    }



    //methode avec controle de saisie et mappage de Objet saisi "RentierForm" vers Objet definitif "Rente"
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/renterevalorisee/add")
    public String add(Model model, @ModelAttribute("RENTEREVALORISEE") @Valid Renterevalorisee renterevalorisee,
                      BindingResult bindingResult, RedirectAttributes messages){

        log.error(mapper.toString());

        log.info("INSERT ZZZ "+ renterevalorisee.toString());

        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAll();
        List<Revalorisation> allRevalorisations = revalorisationService.getAll();
        List<Rente> allRentes = renteService.getAllRentesForRentier(renterevalorisee.getRente().getRentier());
        List<Rentier> allRentiers = rentierService.getAll();


        String rent = renterevalorisee.getRente().getLibel()+ " --> début : " +formatter.format(renterevalorisee.getRente().getDateconsolidation());
        String ident = renterevalorisee.getRente().getRentier().getNomfamille()+" " +renterevalorisee.getRente().getRentier().getPrenom()
                + " né(e) le "+ formatter.format(renterevalorisee.getRente().getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);
        model.addAttribute("REVALORISATIONS", allRevalorisations);


        // renterevalorisee.setId(renterevalorisee.getRevalorisation().getId()-1);

        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);

        if(bindingResult.hasErrors()) {
            log.info("Erreur de saisie "+ renterevalorisee.getDatelancement());

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

            messages.addFlashAttribute("MESSAGE","Erreur d'insertion ou MAJ "+renterevalorisee.getRente().getLibel());

            // on laisse la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie",true);

            return "rentier/newrenterevalorisee";                //  page HTML

        } else {

            log.info("INSERT "+ renterevalorisee.toString());

            renterevaloriseeService.save(renterevalorisee);

            messages.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+renterevalorisee.getRente().getLibel()+" effectuée");

            // on cache la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie",true);

            return "rentier/newrenterevalorisee";                //  page HTML
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/renterevalorisee/sup/{id}")
    public String sup(Model model,@PathVariable("id") Long supID, final RedirectAttributes messageRetour){

        Renterevalorisee renterevalorisee = renterevaloriseeService.getById(supID);
        Rente rente = renteService.getById(renterevalorisee.getRente().getId());
        Rentier rentier = rentierService.getById(rente.getRentier().getId());

        List<Renterevalorisee> allRenterevalorisee = renterevaloriseeService.getAllByRente(rente);
        List<Rente> allRente = renteService.getAllRentesByRentier(rentier);
        List<Rentier> allRentiers = rentierService.getAll();

        model.addAttribute("RENTIERS",allRentiers);
        model.addAttribute("RENTEREVALORISEE", renterevalorisee);
        model.addAttribute("RENTEREVALORISEES", allRenterevalorisee);
        model.addAttribute("RENTES", allRente);

        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);


        // on verifie l'existance de versements associée à la revalorisation
       // int nbRentes  = versementService.countRentes(rente);
       // log.info("Nombre de versements : "+nbRentes+"   Rente ----------------- : "+rente.toString());



        if(renterevalorisee.getVersements().size() > 0 ) {
            //envoi un message vers la page HTML car suppression impossible
            log.info("Supression impossible   ------->    nbre de revalorisations : "+renterevalorisee.getVersements().size());
            messageRetour.addFlashAttribute("MESSAGE", "Suppression impossible, il y a "+renterevalorisee.getVersements().size()+" revalorisatios attachée(s) : " + rente.getLibel());

            //return "rentier/newrente";            // on repart sur la page HTML qui sera rafraichie

            //return "redirect:/rentier/listnom/*";                     // c'est l'url du controleur
            return "redirect:/rente/list/"+rente.getRentier().getId();           // c'est l'url du controleur

        }else {
            // on persiste dans la base
            renterevaloriseeService.delete(renterevalorisee);

            messageRetour.addFlashAttribute("MESSAGE", "Vous avez supprimé : " + renterevalorisee.getRente().getLibel());

            // on cache la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie", true);

            //return "redirect:/renterevalorisee/list";
            return "rentier/newrenterevalorisee";                //  page HTML

        }
    }
}
