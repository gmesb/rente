package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.VersemtypeDao;
import fr.stac.rentes.domain.Versemtype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Antonio RODRIGUES on 12/04/2017.
 * Pour STAC (Direction Générale de l'Aviation Civile )
 * Application : Gestion des rentes
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class VersemtypeService {
    private VersemtypeDao versemtypeDao;

    @Autowired
    public void setVersemtypeDaoDao(VersemtypeDao versemtypeDao) {
        this.versemtypeDao = versemtypeDao;
    }

    /**
     * Supprime un libéllé de la base
     * @param versemtype identifiant de l'élément à supprimer
     */
    @Transactional
    public void delete(Versemtype versemtype){
        versemtypeDao.delete(versemtype);
    }

    /**
     * Sauvegarde d'une nouvelle ou mise à jour du libéllé, ajoutée si versemtype.id est null
     * @param  versemtype C'est le paramètre passé pour être persisté dans la base
     */
    public void save(Versemtype versemtype){
        versemtypeDao.save(versemtype);
    }

    /**
     * Récupère un libéllé par son identifiant
     * @param id identifiant de l'élément typeversement à retrouver
     * @return Retourne un Objet  versemetype si l'identifiant existe, null sinon
     */
    public Versemtype getById(Long id){
        return versemtypeDao.findOne(id);
    }

    /**
     * Récupère dans une Liste toutes les éléments contenus dans la table versemtype de la base
     * @return Renvoi la liste complète des type de versement autorisés pour la saisie
     */
    public List<Versemtype> getAll(){
        return Lists.newArrayList(versemtypeDao.findAll());
    }

   }
