package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.UserDao;
import fr.stac.rentes.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Projet Gestion des Rentes pour le STAC
 * Direction générale de l'aviation Civile
 * Créé le  17/04/2017.
 *
 * @author Antonio Rodrigues
 */

@Service
public class UserService {
    private UserDao userDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setUserDao(UserDao userDao) { this.userDao = userDao;   }


    /**
     * Sauvegarde d'un Utilisateur, ajouté si user.id est null, mis à jour si id existant
     * @param user C'est l'ojet qui doit être persisté
     *
     */
    @Transactional
    public void save(User user){
        userDao.save(user);
    }


    /**
     * Supprime l'utilisateur passé en paramètre
     * On fait appel à l'interfaca DAO pour acter l'opération
     * @param user l'objet à supprimer
     */
    @Transactional
    public void delete(User user){
        userDao.delete(user);
    }

    /**
     * Récupère un utilisateur par son ID passé en paramètre
     * @param id identifiant de l'utilisateur à trouver
     * @return un l'utilisateur si l'identifiant existe, null sinon
     */
    public User getById(Long id){
        return userDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les utilisateurs de la base
     * @return Renvoi la liste chargée
     */
    public List<User> getAll(){
        return Lists.newArrayList(userDao.findAll());
    }

     /**
     * Retrouve un utilisateur par son Identifiant qui sont uniques
     * @param identif le Identifiant, insensible à la casse
     * @return retourne l'utilisateur s'il est inscrit dans la base, null sinon
     */
    public User getByIdentif(String identif){
        return userDao.findByIdentifIgnoreCase(identif);
    }
}
