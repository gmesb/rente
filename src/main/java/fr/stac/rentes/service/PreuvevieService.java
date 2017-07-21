package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.PreuvevieDao;
import fr.stac.rentes.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Antonio RODRIGUES on 12/04/2017.
 * Pour STAC (Direction Générale de l'Aviation Civile )
 *
 */
@Service
public class PreuvevieService {
    private PreuvevieDao preuvevieDao;

    @Autowired
    public void setPreuvevieDao(PreuvevieDao preuvevieDao) {
        this.preuvevieDao = preuvevieDao;
    }

    /**
     * Supprime le mouvement de justification 'preuve de vie' de la base de données correspondant au paramètre ID passé
     * @param preuvevie C'est l'objet à supprimer de la base de données
     */
    @Transactional
    public void delete(Preuvevie preuvevie){
        preuvevieDao.delete(preuvevie);
    }


    /**
     * Sauvegarde d'une Preuve de vie, ajouté si preiuvevie.id est null
     * @param   preuvevie C'est l'ojet qui est passé en parametre et qui va être supprimé de la base de données
     */
    public void save(Preuvevie preuvevie){
        preuvevieDao.save(preuvevie);
    }


    /**
     * Récupère un mouvement  de 'Preuve de vie' par son identifiant
     * @param id identifiant de la Preuve de vie à retrouver
     * @return Renvoi un mouvement de  'Preuve de vie' si l'identifiant existe, null sinon
     */
    public Preuvevie getById(Long id){
        return preuvevieDao.findOne(id);
    }


    /**
     * Récupère dans une Liste toutes les Preuves de vie de la base
     * @return Retourne la liste obtenue contenant la totalité des mouvements de la table
     */
    public List<Preuvevie> getAll(){
        return Lists.newArrayList(preuvevieDao.findAll());
    }


    /**
     * Permet d'obtenir la liste d'objets 'preuvevie'  corerspodant au rentier passé en paramètre
     * @param rentier C'est la paramétre Rentier donc un Objet
     * @return retourne une liste de mouvements de 'Preuve de vie' enregistrés dans la table
     */

    public List<Preuvevie> getAllPreuvevieForRentierDesc(Rentier rentier){
        return Lists.newArrayList(preuvevieDao.getAllByRentierEqualsOrderByDatedemandeDesc(rentier));
    };

}
