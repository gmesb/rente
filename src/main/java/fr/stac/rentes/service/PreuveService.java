package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.PreuveDao;
import fr.stac.rentes.domain.Preuve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Perso-ESIEE on 12/04/2017.
 * Pour STAC (Direction générale de l'Aviation Civile)
 *
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class PreuveService {
    private PreuveDao preuveDao;

    @Autowired
    public void setPreuveDao(PreuveDao preuveDao) {
        this.preuveDao = preuveDao;
    }

    /**
     * Supprime l'élément 'preuve' de la base de données correspondant au paramètre ID passé
     * @param preuve C'est l'objet à supprimer de la base de données
     */
    @Transactional
    public void delete(Preuve preuve){
        preuveDao.delete(preuve);
    }

    /**
     * Sauvegarde d'une Preuve , ajouté si preuve.id est null
     * @param   preuve est l'objet qui va être concerné par l'action
     */
    public void save(Preuve preuve){
        preuveDao.save(preuve);
    }

    /**
     * Récupère une Preuve par son identifiant
     * @param id identifiant de la Preuve à retrouver
     * @return Renvoi un élément 'preuve' si l'identifiant existe, null sinon
     */
    public Preuve getById(Long id){
        return preuveDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les élements de la table preuve
     * @return Renvoi la liste complète des éléments de la table
     */
    public List<Preuve> getAll(){
        return Lists.newArrayList(preuveDao.findAll());
    }
}
