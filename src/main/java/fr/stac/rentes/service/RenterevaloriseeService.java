package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.RenterevaloriseeDao;
import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Renterevalorisee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Projet Gestion des Rentes
 * STAC (Direction Générale de l'Aviation Civile )
 * Créé le  13/04/2017.
 *
 * @author Antonio Rodrigues
 */

@Service
public class RenterevaloriseeService {

    private RenterevaloriseeDao renterevaloriseeDao;

    @Autowired
    public void setRenterevaloriseeDao(RenterevaloriseeDao renterevaloriseeDao) {
        this.renterevaloriseeDao = renterevaloriseeDao;
    }

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Enregistrement des mouvements de revalorisation en base de données, ajouté si i'ID est null sinon c'est une mise à jour effectuée
     *
     * @param renterevalorisee C'est l'objet qui sera persisté en base de données
     */
    @Transactional
    public void save(Renterevalorisee renterevalorisee){
        renterevaloriseeDao.save(renterevalorisee);
    }

    /**
     * Récupère un mouvement de Rente revalorisée par son identifiant
     * @param id identifiant du mouvement de Rente revalorisée à retrouver
     * @return un mouvement unique de Rente revalorisée est retourné si l'identifiant existe, null sinon
     */
    public Renterevalorisee getById(Long id){
        return renterevaloriseeDao.findOne(id);
    }

    /**
     * Il y a suppression de ce mouvement de revalorisation de la rente dans la base de données
     * @param renterevalorisee C'est l'objet de revalorisation de la rente qui va être supprimé
     */
    @Transactional
    public void delete(Renterevalorisee renterevalorisee){
        renterevaloriseeDao.delete(renterevalorisee);
    }

    /**
    * Récupère dans une Liste tous les mouvements des rentes revalorisees de la base
    * @return Cette liste est renvoyée vers la methode qui a fait appel
    */
    public List<Renterevalorisee> getAll(){
        return Lists.newArrayList(renterevaloriseeDao.findAll());
      }

    /**
     * Etablit la liste des mouvements de revalorisatioon concernant une rente passée en paramètre et classés par date de début de mouvement croissante
     *
     * @param rente C'est le paramètre passée afin d'obtenir la liste des mouvements correspondants
     * @return C'est le résultat c'est à dire la liste obtenue qui est renvoyée.
     */
    public List<Renterevalorisee> getRenterevalorisesByRente(Rente rente){
        return Lists.newArrayList(renterevaloriseeDao.findAllByRenteIsLikeOrderByDatedeb(rente));
    };


    /**
     * On recupere le mouvement de revalorisation le plus recent appliqué a la rente passée en paramètre
     * Cela permettra de récuperer les renseignements jusqu là applicable puis d'appliquer le nouvel coefficient
     * à l'ancien montant de rente
     * @param rente C'est la rente concernée par le mouvement de revalorisation passé en paramètre
     * @return Cette requete retourne un seul mouvement
     */
    public Renterevalorisee findPlusRecente(Rente rente){
        return renterevaloriseeDao.getFirstByRenteOrderByDatefinDesc(rente);
    }

    /**
     * Requete qui retourne la liste des mouvements de revalorisation attachés à une rente passée en paramètre
     * @param rente C'est l'objet paramètre c'est à dire la rente concernée
     * @return Retourne la liste obtenue avec les critères de recherche passés.
     */
    public List<Renterevalorisee> getAllByRente(Rente rente){
        return Lists.newArrayList(renterevaloriseeDao.getAllByRente(rente));
    };

    /**
     * Requete qui permet de savoir s'il y a des mouvements de revalorisation assosiés a cette rente
     * @param rente C'est la rente passée en parametre pour faire la recherche
     * @return Retourne un nombre d'élements trouvés
     */
    public int countRentes(Rente rente){return renterevaloriseeDao.countAllByRenteEquals(rente);};

    /**
     * Retourne le mouvement de revalorisation le plus récent afin de calculer les données du mouvement suivant
     * @param rente On passe l'identifiant de la rente à mouvementer ( nouveau mouvement à créer dans la base de données)
     * @return Retourne le dernier mouvement persisté dans la base de données
     */
    public Renterevalorisee getMaxRenterevalorisee(Rente rente) {
        return renterevaloriseeDao.getFirstByRenteOrderByDatefinDesc(rente);
    }
}
