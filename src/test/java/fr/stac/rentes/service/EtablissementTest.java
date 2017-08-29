package fr.stac.rentes.service;


import fr.stac.rentes.domain.Etablissement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;


/**
 * Projet Gestion des rentes  -  TESTS UNITAIRES
 * Pour STAC
 * Direction Générale de l'Aviation Civile
 * Créé le  07/04/2017
 *
 * @author Antonio ROSRIGUES
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
@Transactional
public class EtablissementTest {

    private EtablissementService etablissementService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;
    }

    /**
     * Verifie la bonne extraction des données depuis la TABLE etablissement
     * @throws Exception  Traite l'Exception
     */
    @Test
    public void getAll() throws Exception {

        List<Etablissement> allEtablissements = etablissementService.getAll();
        log.info("Les rentes : "+allEtablissements);
        log.info("Nombre d'élements trouvés : " + allEtablissements.size());
        assertNotNull("N'est pas null ",allEtablissements);
        assertTrue("Il n'y a pas exactement + d'un éléments dans la table ", allEtablissements.size() >= 1);
    }

    /**
     * Contrôle si l'enregistrement est correct :
     * 1 - on vérifie le contenu de la table actuellement en lisant tous les éléments
     * 2 - On vérifie si l'extraction a réussi
     * 3 - on vérifie le nombre délements trouvés
     * 4 - On insert un nouvel élement
     * 5 - On relit de nouveau le contenu de la table
     * 6 - On compare le nombre d'élements trouvés qui doit être à N +1
     */
    @Test
    public void save() {

        List<Etablissement> allEtablissements = etablissementService.getAll();
        assertNotNull(allEtablissements);

        Etablissement e = new Etablissement();
        e.setNom("e");
        e.setAdresse1("E");
        e.setAdresse2("ZZ");
        e.setCodepostal("22222");
        e.setVille("Ville");
        e.setEmail("email");
        e.setResponsable("responsable");
        e.setTelephone("telephone");
        e.setVille("Ville");

        etablissementService.save(e);

        List<Etablissement> allEtablissementsBis = etablissementService.getAll();

        assertNotNull("N'est pas null ",allEtablissementsBis);
        assertTrue(" Il y a exactement : ",allEtablissements.size()+1 == allEtablissementsBis.size() );
        assertTrue("Contient le dernier enregistrement ",allEtablissementsBis.contains(e));
    }

    /**
     *
     * Création d'une nouvel Etablissement,  TEST 1 :
     * 1 - On insert un nouvel élement ( Objet etablissement )
     * 2 - On relit de nouveau le contenu de la table
     * 3 - On Vérifie si le nouvel élement existe dans la liste
     */
    @Test
    public void testNewEtablissement () {

        Etablissement e = new Etablissement();
        e.setId(0L);
        e.setNom("e");
        e.setAdresse1("E");
        e.setAdresse2("ZZ");
        e.setCodepostal("22222");
        e.setVille("Ville");
        e.setEmail("email");
        e.setResponsable("responsable");
        e.setTelephone("telephone");
        e.setVille("Ville");

        etablissementService.save(e);

        List<Etablissement> allEtablissements = etablissementService.getAll();
        assertTrue("Contient dernier enregistrement : ",allEtablissements.contains(e));
    }

    /**
     * Suppression d'un enregistrement dans la BDD,  TEST 3
     * 1- On insere une nouveau enregistrement
     * 2- on verifie qu'il a été enregistré
     * 3- On supprime l'enegistrement
     * 4- On verifie qu'il n'existe plus dans la base
     */
    @Test
    public void testDeleteEnregistrement(){

        Etablissement e = new Etablissement();
        e.setNom("e");
        e.setAdresse1("E");
        e.setAdresse2("ZZ");
        e.setCodepostal("22222");
        e.setVille("Ville");
        e.setEmail("email");
        e.setResponsable("responsable");
        e.setTelephone("telephone");
        e.setVille("Ville");

        etablissementService.save(e);
        List<Etablissement> allEtablissements = etablissementService.getAll();
        assertNotNull("N'est pas null ",allEtablissements);
        assertTrue("Contient dernier enregistrement : ",allEtablissements.contains(e));

        // On recharge les données
        etablissementService.delete(e);
        allEtablissements = etablissementService.getAll();
        assertNotNull("N'est pas null ",allEtablissements);
        assertFalse("Ne Contient plus le dernier enregistrement ",allEtablissements.contains(e));
    }

}
