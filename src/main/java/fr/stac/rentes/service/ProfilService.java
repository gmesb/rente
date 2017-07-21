package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.ProfilDao;
import fr.stac.rentes.domain.Profil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Antonio RODRIGUESon 05/04/2017.
 * Projet : Gestion des rentes
 * STAC (Service technique de l'aviation civile)
 * DGAC  (Direction Générale de l'Aviation Civile)
 */

@Service
public class ProfilService {
    private ProfilDao profilDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setProfilDao(ProfilDao profilDao) {
        this.profilDao = profilDao;
    }


    /**
     * Supprime le profile de la base de données correspondant au paramètre ID passé
     * @param profil C'est l'objet à supprimer de la base de données
     */
    @Transactional
    public void delete(Profil profil){
        profilDao.delete(profil);
    }

    /**
     * Sauvegarde d'un Profil, ajouté si profil.id est null
     * @param   profil  C'est l'objet qui sera sauvegardé dans la base de données
     */
    public void save(Profil profil){
        profilDao.save(profil);
    }

    /**
     * Récupère un Profil par son identifiant
     * @param id L'identifiant du profil à retrouver
     * @return Retournr le Profil s'il existe, null sinon
     */
    public Profil getById(Long id){
        return profilDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les Profils de la table concernée
     * @return Retourne la liste avec tous les élements de la table 'profil'
     */
    public List<Profil> getAll(){
        return Lists.newArrayList(profilDao.findAll());
    }
}
