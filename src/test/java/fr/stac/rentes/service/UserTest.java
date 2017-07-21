package fr.stac.rentes.service;

import fr.stac.rentes.domain.Profil;
import fr.stac.rentes.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

/**
 * Created by Perso-ESIEE on 15/04/2017.
 * STAC - Direction Générale de l'Aviation Civile
 *
 * @author Antonio RODRIGUES
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
@Transactional
public class UserTest {

    private UserService userService;
    private EtablissementService etablissementService;
    private ProfilService profilService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setEtablissementService(EtablissementService etablissementService) {
        this.etablissementService = etablissementService;  }
    @Autowired
    public void setProfilService(ProfilService profilService) {
        this.profilService = profilService;
    }

    /**
     * Verifie la bonne extraction des données depuis la TABLE users
     * @throws Exception  Traite l'Exception
     */
    @Test
    public void getAll() throws Exception {

        List<User> allUsers = userService.getAll();
        log.info("Elements trouvés : " + allUsers.size());
        assertNotNull("N'est pas null ",allUsers);
        assertTrue("Il n'y a pas exactement 5 éléments dans la table ", allUsers.size() >= 1);
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

        List<User> allUsers = userService.getAll();
        assertNotNull(allUsers);
        Set<Profil> profiles = new HashSet<>();

        User u = new User();
        u.setId(0L);
        u.setMdp("mdp123");
        u.setNom("Nom");
        u.setIdentif("identif");
        u.setPrenom("Prenom");
        u.setEmail("email@bb.fr");
        u.setTelephone("telephone");
        u.setActif(false);
        u.setEtablissement(etablissementService.getById(1L));

        Profil profil = profilService.getById(1L);

        profiles.add(profil);
        u.setProfils(profiles);

        userService.save(u);

        List<User> allUsersBis = userService.getAll();
        assertNotNull("Est pas null ",allUsersBis);
        assertTrue("Il n'y a pas exactement le bon nombre",allUsers.size()+1 == allUsersBis.size() );
        assertTrue("Ne contient pas le dernier enregistrement ",allUsersBis.contains(u));
    }


    /**
     * Création d'une nouvel Utilisateur,  TEST 1 :
     * 1 - On insert un nouvel élement ( Objet user )
     * 2 - On relit de nouveau le contenu de la table
     * 3 - On Vérifie si le nouvel élement existe dans la liste
     */
    @Test
    public void testNewUser() {
        List<User> allUsers = userService.getAll();
        assertNotNull(allUsers);
        Set<Profil> profils = new HashSet<>();

        User u = new User();

        u.setId(0L);
        u.setMdp("mdp123");
        u.setNom("Nom");
        u.setIdentif("identif");
        u.setPrenom("Prenom");
        u.setEmail("email@bb.fr");
        u.setTelephone("telephone");
        u.setActif(false);
        u.setEtablissement(etablissementService.getById(1L));

        Profil profil = profilService.getById(2L);
        profils.add(profil);
        u.setProfils(profils);

        userService.save(u);

        List<User> allUsersBis = userService.getAll();

        assertNotNull("Est pas null ",allUsersBis);
        assertTrue(" Il n'y a pas exactement le bon nombre ",allUsers.size()+1 == allUsersBis.size());
        assertTrue("Ne contient pas le dernier enregistrement inséré ",allUsersBis.contains(u));
    }

    /**
     * Modification d'un utilisateur, TEST 2
     * 1- on charge un enregistrement depuis la BDD, dans deux Objets differents, puis on les compare
     * 2- On modifie
     * 3- On persiste dans la BDD
     * 4- On recharge depuis la BDD puis on compare, doivent être differents
     * 5- On compare les deux Objets
     */
    @Test
    public void testUpdateUser(){
        User u1 = userService.getById(7L);
        User u2 = userService.getById(7L);

        assertNotNull("N'est pas null ",u1);
        assertEquals(u1,u2);

        u1.setNom("New UserTest");
        u1.setMdp("New Mdp sur 70 caracteres");
        u1.setPrenom("Prenom");
        u1.setEmail("email");
        u1.setTelephone("telephone");

        Set<Profil> pp = new HashSet<>();
        pp = u1.getProfils();

        Profil profil = profilService.getById(1L);
        profil.setNom("ROLE_TEST");

        pp.add(profil);
        log.info("Profild : "+pp);

        u1.setProfils(pp);

        userService.save(u1);

        u1 = userService.getById(7L);

        //assertNotEquals("u1 différent de u2 : ",u1,u2);
    }
  }
