package fr.stac.rentes.controller;

import com.mysql.jdbc.Driver;
import fr.stac.rentes.domain.Versement;
import fr.stac.rentes.domain.Vueversement;
import fr.stac.rentes.service.VersementService;
import fr.stac.rentes.service.VueversementService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import java.sql.SQLException;


/**
 * Created  le 16/05/2017.
 * Direction générale de l'aviation Civile
 * Projet Gestion Rentes
 * STAC
 * @author Antonio RODRIGUES
 */

@Controller
public class PdfFilesController {
    private VueversementService vueversementService;
    private VersementService versementService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setVueversementService(VueversementService vueversementService) {
        this.vueversementService = vueversementService;
    }
    @Autowired
    public void setVersementService(VersementService versementService) {
        this.versementService = versementService;
    }

    // Display a date in day, month, year format
    private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    @GetMapping("/pdfFiles/versement")
    public void imprimer (Model model, HttpServletResponse response) throws JRException, IOException,SQLException,ServletException {

        List<Vueversement> allVueversements = vueversementService.getAll();

        ClassPathResource resource = new ClassPathResource("application.properties");
        FileReader fr = new FileReader(resource.getFile());

        // on recuprere les propriétés d'acces à la base de données active
        Properties prop = new Properties();
        prop.load(fr);
        fr.close();

        // Extraction des propriétés
        String url = prop.getProperty("spring.datasource.url");
        String login = prop.getProperty("spring.datasource.username");
        String password = prop.getProperty("spring.datasource.password");
        String fileJrxml = prop.getProperty("spring.editions.additional-paths");

        Connection connection = null;
        try {
            /****************************************************************************************/
        	
            // - Connexion à la base
            Driver monDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(monDriver);
            connection = DriverManager.getConnection(url, login, password);
            
            /****************************************************************************************/

            //Si les repertoires des JRxml n'existent pas on les crée
            File outDir = new File(fileJrxml+"/Reports");    outDir.mkdirs();
            outDir = new File("./Editions");                 outDir.mkdirs();

           // on recupère l'utilisateur loggé
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            ClassPathResource resourceJRXML_ordrePaiement = new ClassPathResource("/Reports/ordrePaiement.jrxml");
            JasperDesign jasperDesign = JRXmlLoader.load(resourceJRXML_ordrePaiement.getFile());
            
            String fileOut =  "Editions/Fic-"+username+".pdf";         // niveau Racine de L'application  ex   /rentes

            // Parameters for report Ce sont uniquemenet des parametres   PAS DE DONNEES
            Map<String, Object> parameters = new HashMap <String, Object>();
            parameters.put("Titre", "Titre");

       //     JasperDesign jasperDesign = JRXmlLoader.load(fileIn);

            // First, Compile jrxml file.
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection  );

            // Création du rapport au format PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint,fileOut );
            model.addAttribute("HiddenAffiche", false);      // active le formulaire

            // On tope etatedite pour ne plus afficher
            for ( Vueversement unVueversement : allVueversements ) {

                Versement newVersement = versementService.getById(unVueversement.getVersementid());
                newVersement.setEtatedite(true);

                //  on commence la boucle
                String ident = unVueversement.getNomfamille()+"  "+unVueversement.getPrenom()+"  Dernier jour payé : "+ formatter.format(newVersement.getDernierjourpaye()) ;
                model.addAttribute("DOSSIER",ident);

                //   IF FAUT DECOMMENETER SINON PAS DE MISE A JOUR
                versementService.save(newVersement);

                log.info("TOPE : "+newVersement.getId()+
                        " "+newVersement.getMontant()+
                        " "+newVersement.getDernierjourpaye()+
                        " "+newVersement.getPeriodeversee()+
                        " "+newVersement.getDateversem()
                );
            }

            model.addAttribute("HiddenAffiche", true);      // Desactive le formulaire

            File myFile=new File(fileOut);
            myFile.toURI().toURL(); 

            //========================AFFICHE LE PDF DIRECTEMENT ====Fonctionne  =============================

            new File(fileOut);
            int length = 0;

            response.setContentType("application/pdf");
            ServletOutputStream outStream = response.getOutputStream();
            try {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileOut + "\"");
                byte[] byteBuffer = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(fileOut));
                while ((in != null) && (( length = in.read(byteBuffer)) != -1)){
                    outStream.write(byteBuffer,0,length);
                }
                in.close();
                outStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                outStream.close();
            }

            //=============================================================================================
        } catch (JRException e) {
                e.printStackTrace();  }
    }


    @GetMapping("/imprimer/budget")
    public void imprimerBudget (Model model, HttpServletResponse response) throws JRException, IOException,SQLException  {

        ClassPathResource resource = new ClassPathResource("application.properties");
        FileReader fr = new FileReader(resource.getFile());

       // on recuprere les propriétés d'acces à la base de donnéez active
        Properties prop = new Properties();
        prop.load(fr);
        fr.close();

        // Extraction des propriétés
        String url = prop.getProperty("spring.datasource.url");
        String login = prop.getProperty("spring.datasource.username");
        String password = prop.getProperty("spring.datasource.password");
        String fileJrxml = prop.getProperty("spring.editions.additional-paths");

        Connection connection = null;

        try {

            /****************************************************************************************/
            // - Connexion à la base
            Driver monDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(monDriver);
            connection = DriverManager.getConnection(url, login, password);
            /****************************************************************************************/

            //Si les repertoires des JRxml n'existent pas on les crée
            File outDir = new File(fileJrxml+"/Reports");    outDir.mkdirs();
            outDir = new File("./Editions");                 outDir.mkdirs();

            ClassPathResource resourceJRXML_budget = new ClassPathResource("/Reports/budget.jrxml");
//            String fileIn = fileJrxml+"/Reports/budget.jrxml";

            Map<String, Object> parameters = new HashMap <String, Object>();
            parameters.put("Titre", "Titre");

            JasperDesign jasperDesign = JRXmlLoader.load(resourceJRXML_budget.getFile());

            //First, Compile jrxml file.
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection  );

            // je récupere le nom login utilisateur
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            String fileOut = "Editions/Budget-"+username+".pdf";

            // - Création du rapport au format PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint,fileOut );

            //========================AFFICHE LE PDF DIRECTEMENT ====Fonctionne  =============================

            new File(fileOut);
            int length = 0;

            response.setContentType("application/pdf");
            ServletOutputStream outStream = response.getOutputStream();
            try {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileOut + "\"");
                byte[] byteBuffer = new byte[1024];
                DataInputStream in = new DataInputStream(new FileInputStream(fileOut));
                while ((in != null) && (( length = in.read(byteBuffer)) != -1)){
                    outStream.write(byteBuffer,0,length);
                }
                in.close();
                outStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                outStream.close();
            }

            //=============================================================================================

        } catch (JRException e) {
            e.printStackTrace();
        }
   }
}


