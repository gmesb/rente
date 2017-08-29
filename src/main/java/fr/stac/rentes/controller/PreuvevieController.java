package fr.stac.rentes.controller;

import fr.stac.rentes.domain.*;
import fr.stac.rentes.service.*;
import net.sf.jasperreports.engine.JRException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;


/**
 * Projet Gestion Rentes
 * Par Antonio RODRIGUES
 * Pour STAC
 * Créé le  18/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio
 */

@Controller
public class PreuvevieController {

    private PreuvevieService preuvevieService;
    private RentierService rentierService;
    private PreuveService preuveService;

    // Display a date in day, month, year format
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    Logger log = LoggerFactory.getLogger(this.getClass());

    //necessaire pour la verification de la saisie obligatoire
    @Autowired
    private Mapper mapper;

    @Autowired
    public void setPreuvevieService(PreuvevieService preuvevieService) {
        this.preuvevieService = preuvevieService;
    }
    @Autowired
    public void setRentierService(RentierService rentierService) {
        this.rentierService = rentierService;
    }
    @Autowired
    public void setPreuveService(PreuveService preuveService) {
        this.preuveService = preuveService;
    }


    @GetMapping("/preuvevie/courrier")
    public String courrierPreuvevie(Model model) throws JRException, IOException {

        Collection<Rentier> allRentiers = rentierService.getPresents();   // tous les presents

        ClassPathResource resource = new ClassPathResource("application.properties");
        FileReader fr = new FileReader(resource.getFile());

        // on recuprere les propriétés d'acces à la base de données active et autres
        Properties prop = new Properties();
        prop.load(fr);
        fr.close();

        prop.getProperty("spring.editions.additional-paths");

        // on recupère l'utilisateur loggé
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // SAUVEGARDE DU FICHIER POUR LE PUBLIPOSTAGE DE DEMANDE DE PREUVE DE VIE
        Path cheminFileCSV = Paths.get("Editions/F-"+username+"PreuveVie.csv");
        Charset charsetType = Charset.forName("UTF-8");
        
        log.info("cheminFileCSV : "+cheminFileCSV);


        try (BufferedWriter bufferPublipostage = Files.newBufferedWriter(cheminFileCSV,charsetType))
        {

            // pour avoir l'entete du fichier

            String entete = "Civilité;"+"Nom;"+"Prenom;"+"Adresse1;"+"Adresse2;"+"Codepostal;"+"Ville;";
            bufferPublipostage.write(entete, 0, entete.length());
            bufferPublipostage.newLine();     // pour passer a la ligne suivante dans le fichier .txt

            for (Rentier unRentier: allRentiers ) {

                log.info("Charger le rentier FICHIER  : " + unRentier.getId()
                        +" "+ unRentier.getNomfamille()
                        +" "+ unRentier.getUser().getNom()
                );

                String civilite ;

                if(unRentier.getSexe() == 1) { civilite="Mr"; } else { civilite="Mme"; }

                String enr = civilite+";"+unRentier.getNomfamille()+";"+unRentier.getPrenom()+";"+unRentier.getAdresse1()+";"+
                             unRentier.getAdresse2()+";"+unRentier.getCodepostal()+";"+unRentier.getVille()+";";

                log.info("ENR pour le FICHIER  : "+enr);

                bufferPublipostage.write(enr, 0, enr.length());
                bufferPublipostage.newLine();     // pour passer a la ligne suivante dans le fichier .txt
            }
        }catch(IOException e){e.printStackTrace();}
 
        return "menu";                // on repart sur la page HTML de la nav pricipale qui sera rafraichie
    }



    @GetMapping("/preuvevie/list/{idRentier}")
    public String preuvesvieByRentier(Model model, @PathVariable("idRentier") Long idRentier){

        Rentier rentier = rentierService.getById(idRentier);

        List<Preuvevie> allPreuvevies = preuvevieService.getAllPreuvevieForRentierDesc(rentier);

        log.info("Charger le rentier ddddd  : " + rentier.getId()
                +" "+ rentier.getNomfamille()
                +" "+ rentier.getUser().getNom()
        );

        List<Rentier> allRentiers = rentierService.getAll();
        List<Preuve> allPreuves = preuveService.getAll();

        log.info("Preuvesvies trouvées : "+allPreuvevies.toString());

        log.info("Charger le rentier -------- : " + rentier.getId()
                +" "+ rentier.getNomfamille()
                +" "+ rentier.getUser().getNom()
        );

        Preuvevie preuvevie = new Preuvevie();
        preuvevie.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long
        preuvevie.setDatedemande(new Date());           // date du jour
        preuvevie.setRentier(rentier);

        String ident = rentier.getNomfamille()+" " +rentier.getPrenom()+ " né(e) le "+ formatter.format(rentier.getDatenaissance());

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("PREUVES", allPreuves);

        model.addAttribute("PREUVEVIE", preuvevie);
        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif
        model.addAttribute("HiddenSaisieRente", false);      // Active le formulaire

        model.addAttribute("IDENTRENTIER",ident);

        //TODO je dois verifier pourqoui > 10 fonctione et pas >0 ??????
        if (allPreuvevies.toString().length() >10 ){

            log.info("des Preuves pour ce rentier");
            model.addAttribute("PREUVEVIES", allPreuvevies);
        }

        return "user/preuvevie";            // on repart sur la page HTML qui sera rafraichie
    }


    @GetMapping("/preuvevie/getnewpreuve/{id}")
    public String getnewpreuve(Model model,  @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){

        Collection<Preuve> allPreuves = preuveService.getAll();
        Collection<Rentier> allRentiers = rentierService.getAll();

        Preuvevie preuvevie = preuvevieService.getById(ID);
        Rentier rentier = rentierService.getById(preuvevie.getRentier().getId());

        Collection<Preuvevie> allPreuvevie = preuvevieService.getAllPreuvevieForRentierDesc(rentier);

        String ident = rentier.getNomfamille()+" " +rentier.getPrenom()+ " né(e) le "+ formatter.format(rentier.getDatenaissance());


        model.addAttribute("PREUVEVIE", preuvevie);
        model.addAttribute("PREUVEVIES", allPreuvevie);
        model.addAttribute("PREUVES", allPreuves);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        model.addAttribute("IDENTRENTIER",ident);

        log.info("Charger le preuvevie : " + preuvevie.getId()
                +" "+ preuvevie.getLibel()
                +" "+ preuvevie.getDatedemande()
                +" "+ preuvevie.getDatereception()
                +" "+ preuvevie.getPreuve()
                +" "+ preuvevie.getRentier()
        );

        String mess = "Vous modifiez les données de : "+preuvevie.getLibel();
        messageRetour.addFlashAttribute("message",mess);

        return "user/preuvevie";            // on repart sur la page HTML qui sera rafraichie
    }



    @GetMapping("/preuvevie/get/{id}")
    public String get(Model model,  @PathVariable("id") Long ID,  final RedirectAttributes messageRetour){

        Collection<Preuvevie> allPreuvevie = preuvevieService.getAll();
        Collection<Preuve> allPreuves = preuveService.getAll();
        Collection<Rentier> allRentiers = rentierService.getAll();

        Preuvevie preuvevie = preuvevieService.getById(ID);

        rentierService.getById(preuvevie.getRentier().getId());

        model.addAttribute("PREUVEVIE", preuvevie);
        model.addAttribute("PREUVEVIES", allPreuvevie);
        model.addAttribute("PREUVES", allPreuves);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("DISABLEBTN", false);  // active le bouton   Ajout/modif

        log.info("Charger le preuvevie : " + preuvevie.getId()
                        +" "+ preuvevie.getLibel()
                        +" "+ preuvevie.getDatedemande()
                        +" "+ preuvevie.getDatereception()
                        +" "+ preuvevie.getPreuve()
                        +" "+ preuvevie.getRentier()
        );

        String mess = "Vous modifiez les données de : "+preuvevie.getLibel();
        messageRetour.addFlashAttribute("message",mess);

        return "user/preuvevie";            // on repart sur la page HTML qui sera rafraichie
    }



    @GetMapping("/preuvevie/list")
    public String list(Model model){
        List<Preuvevie> allPreuvevie = preuvevieService.getAll();
        List<Rentier> allRentiers = rentierService.getAll();
        List<Preuve> allPreuves =preuveService.getAll();

        // on initialize pour ne pas avoir  null dans l'ID
        Preuvevie preuvevie = new Preuvevie();
        preuvevie.setId(0L);                            // 0 pour la valeur et L pour indiquer que c'est un type Long

        // on cache la fenetre d saisie de nouvelle rente pour afficher les erreurs
        model.addAttribute("HiddenSaisie",true);

        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("PREUVES", allPreuves);
        model.addAttribute("PREUVEVIES", allPreuvevie);
        model.addAttribute("PREUVEVIE", preuvevie);
        model.addAttribute("DISABLEBTN", true);  // desactive le bouton   Ajout/modif

        return "user/preuvevie";  // c'est la page preuvevie.html
    }

    
    //methode avec controle de saisie et mappage de Objet saisi "RentierForm" vers Objet definitif "Preuvevie"
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','USER')")
    @RequestMapping(value = "/preuvevie/add")
    public String add(Model model, @ModelAttribute("PREUVEVIE") @Valid Preuvevie preuvevie,
                      BindingResult bindingResult, RedirectAttributes messages){

        log.error(mapper.toString());

        log.info("Form saisie PreuveVie " + preuvevie.getId()
                +" "+ preuvevie.getLibel()
                +" "+ preuvevie.getDatedemande()
                +" "+ preuvevie.getDatereception()
               // +" "+ preuvevie.getPreuve().getLibel()
                +" "+ preuvevie.getRentier().getId()
                +" "+ preuvevie.getRentier().getNomfamille()
        );

        // List<Preuvevie> allPreuvevie = preuvevieService.getAllPreuvevieForRentier(preuvevie.getRentier());
        List<Preuvevie> allPreuvevie = preuvevieService.getAllPreuvevieForRentierDesc(preuvevie.getRentier());
        List<Preuve> allPreuves = preuveService.getAll();
        List<Rentier> allRentiers = rentierService.getAll();

        model.addAttribute("PREUVEVIES", allPreuvevie);
        model.addAttribute("RENTIERS", allRentiers);
        model.addAttribute("PREUVES", allPreuves);

        log.info("Form saisie PreuveVie " + preuvevie.getId()
                        +" "+ preuvevie.getLibel()
                        +" "+ preuvevie.getDatedemande()
                        +" "+ preuvevie.getDatereception()
                       // +" "+ preuvevie.getPreuve().getLibel()
                        +" "+ preuvevie.getRentier().getId()
                        +" "+ preuvevie.getRentier().getNomfamille()
        );

        model.addAttribute("DISABLEBTN", true);             // desactive le bouton   Ajout/modif


        // on renvoi seulement les rentes du rentier traité
        String ident = preuvevie.getRentier().getNomfamille()+" " +preuvevie.getRentier().getPrenom()
                +" né(e) le "+ formatter.format(preuvevie.getRentier().getDatenaissance());

        model.addAttribute("IDENTRENTIER",ident);           // paramètre pour affichier l'identité du rentier dans le FORM


        // Attention j'ai rajouté
        model.addAttribute("bindingResult",bindingResult);

        if(bindingResult.hasErrors()) {

            // on laisse la fenetre d saisie de nouvelle rente
            model.addAttribute("HiddenSaisie",false);

            log.info("Erreur de saisie "+ preuvevie.getLibel());
            //return "redirect:/rentier/listnomForPreuvevie/px3";  // c'est l'url du controleur Rentier

            return "user/preuvevie";  // c'est la page preuvevie.html

        } else {

            // on cache la fenetre d saisie de nouvelle rente pour afficher les erreurs
            model.addAttribute("HiddenSaisie",true);

            log.info("Form saisie :PreuveVie:: " + preuvevie.getId()
                    +" "+ preuvevie.getLibel()
                    +" "+ preuvevie.getDatedemande()
                    +" "+ preuvevie.getDatereception()
                    +" "+ preuvevie.getPreuve().getLibel()
                    +" "+ preuvevie.getPreuve().getId()
                    +" "+ preuvevie.getRentier().getNomfamille()
                    +" "+ preuvevie.getRentier().getId()
            );

            String today = formatter.format(preuvevie.getDatedemande());

            log.info("Date reformatee  : " + today );

            preuvevieService.save(preuvevie);
            messages.addFlashAttribute("MESSAGE","Insertion ou MAJ de "+preuvevie.getLibel()+" effectuée");

            //return "redirect:/rentier/listnomForPreuvevie/px3";  // c'est l'url du controleur Rentier
            return "user/preuvevie";  // c'est la page preuvevie.html
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/preuvevie/delete/{id}")
    public String sup(@PathVariable("id") Long supID,
                          final RedirectAttributes messageRetour){

        Preuvevie preuvevie = preuvevieService.getById(supID);

        preuvevieService.delete(preuvevie);

        log.info("Supprime l'utilisateur" + preuvevie.getLibel());

        //envoi un message vers la page HTML apres suppression
        //messageRetour.addFlashAttribute("SUPid",supID);
        messageRetour.addFlashAttribute("MESSAGE", "Vous avez supprimé : " + preuvevie.getLibel());

        return "redirect:/preuvevie/list";

    }
}
