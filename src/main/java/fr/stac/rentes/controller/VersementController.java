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
import java.util.*;
import java.text.ParseException;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC
 * Créé le  20/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class VersementController {
    private VersementService versementService;
    private RenteService renteService;
    private RenterevaloriseeService renterevaloriseeService;
    private RevalorisationService revalorisationService;
    private RentierService rentierService;

    // Display a date in day, month, year format
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    // Display a date in YEAR, month,DAY format
    private DateFormat formatBDD = new SimpleDateFormat("yyyy-MM-dd");


    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;

    @Autowired
    public void setVersementService(VersementService versementService) {
        this.versementService = versementService;
    }
    @Autowired
    public void setRenteService(RenteService renteService) {
        this.renteService = renteService;
    }
    @Autowired
    public void setRenterevaloriseeService(RenterevaloriseeService renterevaloriseeService) {
        this.renterevaloriseeService = renterevaloriseeService;
    }
    @Autowired
    public void setRevalorisationService(RevalorisationService revalorisationService) {
        this.revalorisationService = revalorisationService;
    }
    @Autowired
    public void setRentierService(RentierService rentierService) {
        this.rentierService = rentierService;
    }


    public static int getLastDayInMonth(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month , day);
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


    // generation des versements

    public void createVersementTrim(Rente uneRente,Date dateDebut, String typeVersem){

         log.info("DATE DE DEBUT : "+dateDebut+
                " Fin de Rente : "+uneRente.getEtatrente()+
                " Fin de RenteBis : "+uneRente.getDatefinrente()
        );

        // private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        // pour recuperer le dernier jour du mois on passe d'abbord dans 1 String
        String datDeb=formatter.format(dateDebut);            // date debut

        log.info("DATE DE DEBUT (formatter) : "+datDeb);

        int jDeb = Integer.parseInt(datDeb.substring(0, 2));   // ... le jour
        int mDeb = Integer.parseInt(datDeb.substring(3, 5));   // ... le mois
        int aDeb = Integer.parseInt(datDeb.substring(6, 10));  // la variable reçoit l'année


        String datFin ="";
        int derJourMois =0;
        String periodepayee ="";
        int diviseurMoisTrimestre = 4;  //  4    par defaut

        Date dateFin = new Date();

        switch (typeVersem){
        case "trim":{
            /////   CAS PAR TRIMESTRE   ++++++++++++++++++++++++++++++++++++++
            //String datFin="2016-12-01";

            if(mDeb>0 && mDeb <4){
                datFin = aDeb  + "-" + "03" +"-" + "31";
                diviseurMoisTrimestre = 12 / (3 - mDeb + 1);
            }else{
                if(mDeb>3 && mDeb <7){
                    datFin = aDeb  + "-" + "06" +"-" + "30";
                    diviseurMoisTrimestre = 12 / (6 - mDeb + 1);
                }else{
                    if(mDeb>6 && mDeb <10){
                        datFin = aDeb  + "-" + "09" +"-" + "30";
                        diviseurMoisTrimestre = 12 / (9 - mDeb + 1);
                    }else{
                        // donc ici c'est 10,11,12
                        datFin = aDeb  + "-" + "12" +"-" + "31";
                        diviseurMoisTrimestre = 12 / (12 - mDeb + 1);
                    }
                }
            }

            // on transforme la chaine DatFin en type  Date
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateFin = dt.parse(datFin);
            } catch (ParseException e) {   e.printStackTrace();      }

            // Si on a une date de Fin de rente on la positionne pour arreter les calculs
            if( uneRente.getDatefinrente().compareTo(dateFin)< 0 && uneRente.getDatefinrente().compareTo(dateDebut)>= 0 ) {
                dateFin = uneRente.getDatefinrente();
            }

            datFin=formatBDD.format(dateFin);

            log.info("DATE DE FIN fffffffffff : "+dateFin+"   FORMATEE : "+datFin);
            log.info(" MoisDeb : "+mDeb+" Diviseur : "+diviseurMoisTrimestre);

            periodepayee = "Paiement trimestriel du " + formatter.format(dateFin);
            break;
            }


        case "mois":{

            ///  CAS PAR MOIS   +++++++++++++++++++++++++++++++++++++++++++++++++++
            // on recupere le dernier jour du mois conerne par cette date ( parametres passées : annee,mois,jour )
            derJourMois = getLastDayInMonth(aDeb,mDeb - 1 ,jDeb); //Janvier = 0 février=1,, etc...
            datFin = aDeb + "-"+ mDeb +"-" + derJourMois;

            // on transforme la chaine DatFin en type  Date
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dateFin = dt.parse(datFin);
            } catch (ParseException e) {   e.printStackTrace();      }

            datFin=formatBDD.format(dateFin);

            log.info("DATE DE FIN : "+dateFin+"   FORMATEE : "+datFin);

            diviseurMoisTrimestre = 12;
            periodepayee ="Paiement mensuel du "+ formatter.format(dateFin);
            break;
            }

        default:{
            break;
            }
        }

        log.info("J trim deb : "+jDeb+" moisDeb : "+mDeb+" annee Deb : "+aDeb
                +" ===  der J mois : "+derJourMois+"  STR datfin ======  "+datFin +"   Diviseur : "+diviseurMoisTrimestre);



        // on commence a boucler   car la periode facturables est faite   ex : 01/01/2017  au  31/03/2017

        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getRenterevalorisesByRente(uneRente);



        for (Renterevalorisee uneRenterevalorisee : allRenterevalorisees ) {

            log.info("Une Revalorisation  XXXXXX : " + uneRenterevalorisee.getDatedeb()+"  "+uneRenterevalorisee.getDatefin());
            log.info("Rente en cours : " + uneRente.toString());

            boolean sortir = false;

            // c'est une revalorisation DEBUT/FIN même Jour donc à ne pas prendre
            // on passe a la suivante

            if(uneRenterevalorisee.getDatedeb().compareTo(uneRenterevalorisee.getDatefin()) == 0 ){
                //  RIEN A FAIRE ;

                log.info("RIEN A FAIRE................................................................");

            }else{

                while( uneRente.getDatefinrente().compareTo(dateFin) >= 0 && !sortir ){

                    log.info("Periode - Deb : "+dateDebut+" Fin : " + dateFin+
                            " Etat Rente : "+uneRente.getEtatrente()+
                            " Etat RenteBis : "+uneRente.getDatefinrente()
                    );
                    log.info("Diviseur : "+diviseurMoisTrimestre);

                    if (uneRenterevalorisee.getDatefin().compareTo(dateDebut) < 0) {
                        // fin de reval < debut periode à payer donc RIEN
                        // on passe à la reval suivante

                        sortir = true;
                        break;

                    } else {

                        if (uneRenterevalorisee.getDatedeb().compareTo(dateFin) >= 0) {
                            // debut de reval > Fin periode à payer donc RIEN
                            // on passe à la reval suivante

                            sortir = true;
                            break;

                        } else {

                            Versement newVersement = new Versement();
                            newVersement.setId(0L);
                            newVersement.setDateversem(new Date());

                            newVersement.setEtatedite(false);

                            newVersement.setRenterevalorisee(uneRenterevalorisee);

                            newVersement.setMontant(uneRenterevalorisee.getMontantrevalorise() / diviseurMoisTrimestre);

                            newVersement.setDernierjourpaye(dateFin);
                            newVersement.setPeriodeversee(periodepayee);

                            log.info("Une renteRevalorisée : "+uneRenterevalorisee.getId()
                                    + " ID reval : " +uneRenterevalorisee.getRevalorisation().getId());

                            versementService.save(newVersement);

                            // on recalcule pour les periodes suivantes ///

                            Calendar monCalendar = Calendar.getInstance();
                            monCalendar.setTime(newVersement.getDernierjourpaye());
                            monCalendar.add(Calendar.DATE, 1);  // on aditionne un Jour

                            dateDebut = monCalendar.getTime();     // date de début = Dernier jour Payé + 1

                            // private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            // pour recuperer le dernier jour du mois on passe d'abbord dans 1 String

                            datDeb = formatter.format(dateDebut);            // date debut

                            log.info("22 DATE DE DEBUT (formatter) : " + datDeb);

                            int jourDeb = Integer.parseInt(datDeb.substring(0, 2));   // ... le jour
                            int moisDeb = Integer.parseInt(datDeb.substring(3, 5));   // ... le mois
                            int anneeDeb= Integer.parseInt(datDeb.substring(6, 10));  // la variable reçoit l'année

                            switch (typeVersem) {
                                case "trim": {

                                    if (moisDeb > 0 && moisDeb < 4) {
                                        datFin = anneeDeb + "-" + "03" + "-" + "31";
                                        diviseurMoisTrimestre = 12 / (3 - moisDeb + 1);
                                    } else {
                                        if (moisDeb > 3 && moisDeb < 7) {
                                            datFin = anneeDeb + "-" + "06" + "-" + "30";
                                            diviseurMoisTrimestre = 12 / (6 - moisDeb + 1);
                                        } else {
                                            if (moisDeb > 6 && moisDeb < 10) {
                                                datFin = anneeDeb + "-" + "09" + "-" + "30";
                                                diviseurMoisTrimestre = 12 / (9 - moisDeb + 1);
                                            } else {
                                                // donc ici c'est 10,11,12
                                                datFin = anneeDeb + "-" + "12" + "-" + "31";
                                                diviseurMoisTrimestre = 12 / (12 - moisDeb + 1);
                                            }
                                        }
                                    }

                                    // on transforme la chaine DatFin en type  Date
                                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        dateFin = dt.parse(datFin);
                                    } catch (ParseException e) { e.printStackTrace();  }

                                    // Si on a une date de Fin de rente on la positionne pour arreter les calculs
                                    if( uneRente.getDatefinrente().compareTo(dateFin)< 0 && uneRente.getDatefinrente().compareTo(dateDebut)>= 0 ) {
                                        dateFin = uneRente.getDatefinrente();
                                    }

                                    datFin = formatBDD.format(dateFin);

                                    periodepayee = "Paiement trimestriel du " + formatter.format(dateFin);
                                    break;
                                }

                                case "mois": {

                                    ///  CAS PAR MOIS   +++++++++++++++++++++++++++++++++++++++++++++++++++
                                    // on recupere le dernier jour du mois conerne par cette date ( parametres passées : annee,mois,jour )
                                    derJourMois = getLastDayInMonth(anneeDeb, moisDeb - 1, jourDeb); //Janvier = 0 février=1,, etc...
                                    datFin = anneeDeb + "-" + moisDeb + "-" + derJourMois;

                                    diviseurMoisTrimestre = 12;

                                    // on transforme la chaine DatFin en type  Date

                                    SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                                    try {
                                        dateFin = dt.parse(datFin);
                                    } catch (ParseException e) {   e.printStackTrace();  }

                                    datFin = formatBDD.format(dateFin);           // format  AAAA-MM-JJ
                                    periodepayee = "Paiement mensuel du " + formatter.format(dateFin);
                                    break;
                                }

                                default: {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    @GetMapping("/versement/boucle")
    public String boucleVersement(Model model)throws NullPointerException  {

        List<Rente> rentesValides = renteService.getAll();      // je prends toutes les rentes

        //List<Rente> rentesValides = renteService.findAllByEtatrentePresent(12L);      // je prends pour test


        log.info("DEMARRAGE    Rentes a payer : " + rentesValides.toString());

        //  on commence la boucle
        model.addAttribute("HiddenAffiche", false);      // false = active le formulaire


        for (Rente uneRente : rentesValides) {


            // dernier versement effectué pour connaitre le debut de paiement
            //log.info("SIZE versements dans Rente : "+ uneRente.getVersements());

            // on declare au prealable afin d'avoir à null si aucun versement de trouvé

            Versement lastVersement = versementService.getLastVersement(uneRente);

            log.info("Last Versement : "+lastVersement.getDernierjourpaye()
                    + " " +lastVersement.getPeriodeversee()
                    + " " +lastVersement.getMontant()
                    + " " +uneRente.getRenterevalorisees()
            );
            log.info(" UNE RENTE :  Type versement : "+ uneRente.getVersemtype()
                    + " " + uneRente.getId()
                    + " " + uneRente.getLibel()
                    + " " + uneRente.getRentier().getNomfamille()
                    + " " + uneRente.getRentier().getId()
                    + " " + uneRente.getDateconsolidation()
                    + " " + uneRente.getDatefinrente()

            );

            int typeV = (int)(long) uneRente.getVersemtype().getId();

            log.info("Choix  typeV : "+ typeV);

            // je ne laisse pas la FIn de rente à NULL   Indispensable pour continuer le traitement


            if(uneRente.getDatefinrente() == null ){

                //lorsque l'etat de la rente est null je positionne le dernier jour payable pour effectuer les calculs
                //qui est toujours le dernier jour du mois precedent échu par rapport au jour J

                //pour recuperer le dernier jour du mois on passe d'abbord dans 1 String

                String datDeb=formatter.format(new Date());               // date du jour

                int jourDeb  =Integer.parseInt(datDeb.substring(0, 2));   // ... le jour
                int moisDeb  =Integer.parseInt(datDeb.substring(3, 5));   // ... le mois
                int anneeDeb =Integer.parseInt(datDeb.substring(6, 10));  // la variable reçoit l'année

                // on passe au mois de décembre de l'année n -1 si on est en janvier
                if(moisDeb == 1){moisDeb = 13 ; anneeDeb = anneeDeb - 1;  };

                // on recupere le dernier jour du mois concerné par cette date ( parametres passées : annee,mois,jour )
                int derJourMois = getLastDayInMonth(anneeDeb,moisDeb -2 ,jourDeb);     //Janvier = 0 février=1, etc...
                switch (typeV){
                case 1:     // trimestre    4 trimestres

                    if (moisDeb > 0 && moisDeb < 4) {
                       uneRente.setDatefinrente(getDateAAAAMMJJ(anneeDeb -1,12,31));
                    } else {
                        if (moisDeb > 3 && moisDeb < 7) {
                            uneRente.setDatefinrente(getDateAAAAMMJJ(anneeDeb ,3,31));
                        } else {
                            if (moisDeb > 6 && moisDeb < 10) {
                                uneRente.setDatefinrente(getDateAAAAMMJJ(anneeDeb ,6,30));
                            } else {
                                // donc ici c'est 10,11,12
                                uneRente.setDatefinrente(getDateAAAAMMJJ(anneeDeb ,9,30));
                            }
                        }
                    }
                    break;

                case 2:
                    // CAS PAR MOIS   +++++++++++++++++++++++++++++++++++++++++++++++++++
                    // on recupere le dernier jour du mois conerne par cette date ( parametres passées : annee,mois,jour )

                    derJourMois = getLastDayInMonth(anneeDeb,moisDeb - 2 ,jourDeb); //Janvier = 0 février=1,, etc...
                    uneRente.setDatefinrente(getDateAAAAMMJJ(anneeDeb ,moisDeb - 1,derJourMois));

                    break;
                case 3:
                    // cas de Forfait donc on arrete la rente au moment du paiement
                    uneRente.setEtatrente(new Date());
                    uneRente.setDatefinrente(new Date());
                    break;
                }
            }


            //  DEBUT DU TRAITEMENT EN BOUCLE    on cherche la date de debut

            Date dateDebut = new Date() ;

            if(lastVersement.getDateversem() == null ){
                // aucun versement d'effectué pour l'instant
                dateDebut = uneRente.getDateconsolidation();

            }else{
                Calendar cal = Calendar.getInstance();
                cal.setTime(lastVersement.getDernierjourpaye());
                cal.add(Calendar.DATE, 1);                       // on aditionne un Jour

                dateDebut = cal.getTime();                              // date de début = Dernier jour Payé + 1
            }

            switch (typeV){
            case 1:     // trimestre    4 trimestres

                // on apelle la methode de calcul avec une date de debut positionnée et une rente a traiter
                createVersementTrim(uneRente,dateDebut,"trim" );
                break;

            case 2:             // mensuel      12 mois

               // on apelle la methode de calcul
                createVersementTrim(uneRente,dateDebut,"mois" );
                break;


            case 3:     // forfait      1 forfait   ATTENTION PAS DE REVALORISATION POUR LES FORFAITS

                // on fait si pas de versement unique deja fait

                if(lastVersement.getDateversem() == null ) {
                    // la valeur de la plus recente revalorisation de la rente à appliquer
                    Renterevalorisee lastRenterevalorisee = renterevaloriseeService.getMaxRenterevalorisee(uneRente);

                    log.info("Last  Revalorisation : " + lastRenterevalorisee);

                    Versement newVersement = new Versement();

                    newVersement.setId(0L);
                    uneRente.setEtatrente(new Date());

                    newVersement.setDateversem(new Date());
                    newVersement.setMontant(lastRenterevalorisee.getMontantrevalorise());
                    newVersement.setPeriodeversee("Paiement du Forfait  le " + formatter.format(newVersement.getDateversem()));

                    newVersement.setDernierjourpaye(uneRente.getEtatrente());

                    newVersement.setRenterevalorisee(lastRenterevalorisee);  // si  on modifie Renterevalorisee se sera persisté dans la BDD

                    versementService.save(newVersement);

                    //  on positionne la date de paiement au niveau de la revalorisation parceque l'on avait 31-12-3000
                    lastRenterevalorisee.setDatefin(newVersement.getDateversem());
                    renterevaloriseeService.save(lastRenterevalorisee);
                }

                break;
            default:
                break;
            }
        }

        model.addAttribute("HiddenAffiche", true);      // Desactive le formulaire

        return "menu";                // on repart sur la page HTML de la nav principale qui sera rafraichie
    }



    @GetMapping("/versement/getnewversem/{id}")
    public String getNewVersement(Model model, @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){

        Versement versement = versementService.getById(ID);
        Rente rente = renteService.getById(versement.getRenterevalorisee().getRente().getId());
        Rentier rentier = rentierService.getById(rente.getRentier().getId());
        Renterevalorisee renterevalorisee = renterevaloriseeService.getById(versement.getRenterevalorisee().getId());

        List<Versement> allVersements = versementService.getVersementsByRenterevalorisee(renterevalorisee);

        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAllByRente(rente);
        List<Rente> allRentes = renteService.getAllRentesForRentier(rentier);
        List<Rentier> allRentiers = rentierService.getAll();

        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("VERSEMENTS", allVersements);
        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);
        model.addAttribute("RENTES",allRentes);
        model.addAttribute("RENTIERS",allRentiers);

        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());

        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);

        String mess = "Vous modifiez les données de : "+versement.getRenterevalorisee().getRente().getRentier().getNomfamille();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "rentier/newversement";            // on repart sur la page HTML qui sera rafraichie
    }



    @GetMapping("/versement/get/{id}")
    public String get(Model model,  @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){
       
        Collection<Versement> allVersements = versementService.getAll();
        Collection<Rente> allRentes = renteService.getAll();
        Versement versement = versementService.getById(ID);

        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("VERSEMENTS", allVersements);
        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif
        model.addAttribute("RENTES",allRentes);

        String mess = "Vous modifiez les données de : "+versement.getRenterevalorisee().getRente().getRentier().getNomfamille();
        messageRetour.addFlashAttribute("MESSAGE",mess);

        return "user/versement";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/versement/list")
    public String list(Model model){
        
        List<Versement> allVersements = versementService.getAll();
        List<Rente> allRentes = renteService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Versement versement = new Versement();
        versement.setId(0L);          // 0 pour la valeur et L pour indiquer que c'est un type Long

        model.addAttribute("RENTES",allRentes);
        model.addAttribute("VERSEMENTS", allVersements);
        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/versement";  // c'est la page versement.html
    }



    @GetMapping("/versement/listVersem/{idRevalo}")
    public String ListVersemByRente(Model model,@PathVariable ("idRevalo") Long idRevalo){

        Renterevalorisee renterevalorisee = renterevaloriseeService.getById(idRevalo);
        Rente rente = renteService.getById(renterevalorisee.getRente().getId());
        revalorisationService.getById(renterevalorisee.getRevalorisation().getId());

        List<Rentier> allRentiers = rentierService.getAll();
        List<Rente> allRentes = renteService.getAllRentesForRentier(rente.getRentier());
        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAllByRente(rente);

        List<Versement> allVersements = versementService.getVersementsByRenterevalorisee(renterevalorisee);

        Versement versement = new Versement();
        versement.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        versement.setDateversem(new Date());
        versement.setRenterevalorisee(renterevalorisee);

        versement.setMontant(0.00F);

        log.info("Charger le Versement ----------: " + versement.toString()     );

        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());

        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);

        model.addAttribute("RENTIERS",allRentiers);
        model.addAttribute("RENTES",allRentes);
        model.addAttribute("RENTEREVALORISEES",allRenterevalorisees);
        model.addAttribute("VERSEMENTS", allVersements);

        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        model.addAttribute("HiddenSaisie", false);          // On  Active le formulaire

        //TODO je dois verifier pourqoui > 10 fonctione et pas >0 ??????

        if (allVersements.toString().length() >10 ){
            log.info("des versements pour cette rente");

            model.addAttribute("VERSEMENTS", allVersements);
        }
        return "rentier/newversement";   // c'est la page newversement.html
    }



    @GetMapping("/versement/listVersements/{idRente}")
    public String ListVersementsByRente(Model model, @PathVariable ("idRente") Long idRente){

        Rente rente = renteService.getById(idRente);

        Renterevalorisee renterevalorisee = new Renterevalorisee();  // A voir

        List<Rentier> allRentiers = rentierService.getAll();
        List<Rente> allRentes = renteService.getAllRentesForRentier(rente.getRentier());

        List<Versement> allVersements = versementService.getVersementsByRenterevalorisee(renterevalorisee);


        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getAllByRente(rente);

        Versement versement = new Versement();
        versement.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        versement.setDateversem(new Date());
        versement.setRenterevalorisee(renterevalorisee);

        versement.setRenterevalorisee(renterevalorisee);
        versement.setMontant(0.00F);

        log.info("Charger le Versement ----------: " + versement.toString()     );

        String rent = rente.getLibel()+ " --> début : " +formatter.format(rente.getDateconsolidation());
        String ident = rente.getRentier().getNomfamille()+" " +rente.getRentier().getPrenom()+ " né(e) le "+ formatter.format(rente.getRentier().getDatenaissance());

        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);

        model.addAttribute("RENTIERS",allRentiers);
        model.addAttribute("RENTES",allRentes);
        model.addAttribute("RENTEREVALORISEE",allRenterevalorisees);
        model.addAttribute("VERSEMENT", versement);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif

        model.addAttribute("HiddenSaisie", true);          // DesActive le formulaire


        //TODO je dois verifier pourqoui > 10 fonctione et pas >0 ??????

        if (allVersements.toString().length() >10 ){
            log.info("des versements pour cette rente");

            model.addAttribute("VERSEMENTS", allVersements);
        }
        return "rentier/newversement";   // c'est la page newversement.html
    }



    //methode avec controle de saisie et mappage de Objet saisi "RentierForm" vers Objet definitif "Versement"
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/versement/add")
    public String add(@ModelAttribute("VERSEMENT") @Valid final Versement versement,
                      final BindingResult bindingResult,  RedirectAttributes messages,
                      final Model model ){

        log.error(mapper.toString());

        List<Rentier> allRentiers = rentierService.getAll();
        List<Rente> allRentes = renteService.getAllRentesForRentier(versement.getRenterevalorisee().getRente().getRentier());
        List<Renterevalorisee> allRenterevalorisees = renterevaloriseeService.getRenterevalorisesByRente(versement.getRenterevalorisee().getRente());

        List<Versement> allVersements = versementService.getVersementsByRenterevalorisee(versement.getRenterevalorisee());

        String rent = versement.getRenterevalorisee().getRente().getLibel()+ " --> début : " +formatter.format(versement.getRenterevalorisee().getRente().getDateconsolidation());
        String ident = versement.getRenterevalorisee().getRente().getRentier().getNomfamille()+" " +versement.getRenterevalorisee().getRente().getRentier().getPrenom()
                + " né(e) le "+ formatter.format(versement.getRenterevalorisee().getRente().getRentier().getDatenaissance());
        model.addAttribute("IDENTRENTIER",ident);
        model.addAttribute("DESRENTE",rent);


        model.addAttribute("VERSEMENTS", allVersements);
        model.addAttribute("RENTEREVALORISEES", allRenterevalorisees);
        model.addAttribute("RENTES", allRentes);
        model.addAttribute("RENTIERS", allRentiers);


        // Attention j'ai rajouté
        //model.addAttribute("bindingResult",bindingResult);
        log.info("bindingResult : ",bindingResult);

        if(bindingResult.hasErrors() || bindingResult.hasGlobalErrors() || bindingResult.hasFieldErrors()) {

            //messages.addFlashAttribute("MESSAGE","Erreur, insertion ou MAJ "+versement.getRente().getRentier().getNomfamille()+" non effectuée");
            log.info("Erreur de saisie "+ versement.getDateversem() + "  erreurs : "+ bindingResult.hasErrors());


            model.addAttribute("HiddenSaisie",false);   // on laisse la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenReval",false);    // on active la fenetre des revalorisations
            model.addAttribute("HiddenRente",false);    // on active la fenetre Des rentes

            return "rentier/newversement";                      // c'est la page newversement.html ainsi on affiche les messages erreur
            //return "redirect:../rentier/listnomForVersement/px3";             // c'est l'url du controleur
        } else {

            versementService.save(versement);

            messages.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+versement.getRenterevalorisee().getRente().getRentier().getNomfamille());

            model.addAttribute("HiddenSaisie",false);    // on cache la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenReval",false);     // on cache la fenetre des revalorisations
            model.addAttribute("HiddenRente",false);     // on cache la fenetre Des rentes

            return "redirect:/versement/getnewversem/"+versement.getId();      // c'est l'url du controleur "VersementController"
            //return "rentier/newversement";                               // c'est la page newversement.html
         }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/versement/delete/{id}")
    public String sup(Model model, @PathVariable("id") Long ID,
                          final RedirectAttributes messageRetour){

        Versement versement = versementService.getById(ID);

        versementService.delete(versement);


        log.info("Supprime le versement"+versement.getRenterevalorisee().getRente().getRentier().getNomfamille());

        messageRetour.addFlashAttribute("MESSAGE","Vous avez supprimé : "+versement.getRenterevalorisee().getRente().getRentier().getNomfamille());

        List<Rentier> allRentiers = rentierService.getAll();

        //return "redirect:/versement/list";
        model.addAttribute("HiddenSaisie",true);    // on cache la fenetre d saisie de nouvelle rente
        model.addAttribute("HiddenReval",true);     // on cache la fenetre des revalorisations
        model.addAttribute("HiddenRente",true);     // on cache la fenetre Des rentes

        model.addAttribute("RENTIERS", allRentiers);

        return "rentier/newversement";
    }
}
