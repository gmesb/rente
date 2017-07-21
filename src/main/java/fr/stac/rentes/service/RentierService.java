package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.RentierDao;
import fr.stac.rentes.domain.Rentier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Projet Gestion des Rentes
 * Direction générale de l'aviation Civile
 * Créé le  13/04/2017.
 *
 * @author Antonio Rodrigues
 */

@Service
public class RentierService {
    private RentierDao rentierDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setRentierDao(RentierDao rentierDao) {
        this.rentierDao = rentierDao;
    }

    /**
     * Sauvegarde d'un élement  "rentier", ajouté si rentier.id est null
     * Si Id n'existe pas, c'est une insertion qui se fera
     *
     * @param rentier C'est l'ojet qui doit être persisté dans la base de données soit en insertion soit en mise à jour.
     *
     */
    @Transactional
    public void save(Rentier rentier){
        rentierDao.save(rentier);
    }


    /**
     * Supprime le Rentier de la base de données s'il existe.
     * @param rentier c'est l'ojet Rentier  qui va être supprimé
     */
    @Transactional
    public void delete(Rentier rentier){
        rentierDao.delete(rentier);
    }


    /**
     * Récupère un Rentier par son identifiant
     * @param id identifiant du Rentier à retrouver
     * @return un Rentier si l'identifiant existe, null sinon
     */
    public Rentier getById(Long id){
        return rentierDao.findOne(id);
    }


    /**
     * Récupère dans une Liste tous les rentiers de la base
     * @return La liste est renvoyée avec tous les rentiers de la base
     */
    public List<Rentier> getAll(){
        return Lists.newArrayList(rentierDao.findAll());
    }

    /**
     *  Récupère dans une Liste toutes les rentiers présents uniquement (recevant une rente) dans de la base
     *  dont la date de fin de droits n'est pa renseignée, findroit est un commenatire libre à positionner par l'utilisateur
     * @return C'est une liste qui est retournée
     */

    public List<Rentier> getPresents(){
        return Lists.newArrayList(rentierDao.findAllByFindroitNull());
    }

    /**
     *  Récupère dans une Liste toutes les personnes dont le nom de famille est saisi partielement tout en ignorant la saisie majuscule ou miniscules.
     * @param nomrentier C'est le paramètre passé
     * @return retourne donc une liste contenant les rentiers dont le Nom de famille correspond à la saisie partielle
     */
    public List<Rentier> getNomComme(String nomrentier){
        return Lists.newArrayList(rentierDao.findByNomfamilleIsLikeIgnoreCase(nomrentier));
    }

    /**
     * Retrouve un ou plusieurs rentiers par leur nom entier, n'ayant plus de droits en cours.
     *
     * @param nom le nom de la personne, insensible à la casse
     * @return  Un ou plusieurs rentiers peuvent correspondre à la saisie donc s'est une liste qui est retournée.
     */
    public List<Rentier> getByNomfamille(String nom){
        return Lists.newArrayList(rentierDao.findByNomfamilleAndFindroitIsNotNullIgnoreCase(nom));
    }

}
