package fr.stac.rentes.service;


import fr.stac.rentes.dao.ProfilDao;
import fr.stac.rentes.dao.UserDao;
import fr.stac.rentes.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * Projet GESTION DES RENTES
 * STAC ( Direction Générale de l'Aviation Civile )
 * Créé le  12/04/2017.
 *
 * @author Antonio RODRIGUES
 */

@Service
public class JpaUserService {
    private UserDao userDao;
    private ProfilDao profilDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setProfilDao(ProfilDao profilDao) {
        this.profilDao = profilDao;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public void save(User user){

        // encodage du mot de passe
        user.setMdp(bCryptPasswordEncoder.encode(user.getMdp()));

        // enregistrement des profils sauf ceux deja s'y trouvant
        user.setProfils(new HashSet<>(profilDao.findAll()));

        // enregistrement de l'user dans la base
        userDao.save(user);
    }

    public User findByNomIgnoreCase(String userNom){
        return userDao.findByNomIgnoreCase(userNom);
    }
}
