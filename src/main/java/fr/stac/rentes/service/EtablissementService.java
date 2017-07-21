package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.EtablissementDao;
import fr.stac.rentes.domain.Etablissement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Projet Gestion des Rentes
 * Pour STAC
 * Direction générale de l'aviation Civile
 * Créé le  07/04/2017.
 *
 * @author Antonio Rodrigues
 */

@Service
public class EtablissementService {
    private EtablissementDao etablissementDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setEtablissementDao(EtablissementDao etablissementDao) {
        this.etablissementDao = etablissementDao;
    }

    /**
     * Sauvegarde d'un Etablissement, ajouté si etablissement.id est null
     * @param etablissement C'est l'objet qui doit être persisté en base de données
     */
    @Transactional
    public void save(Etablissement etablissement){ etablissementDao.save(etablissement);
    }

    /**
     * Supprime l'élément 'etablissement' de la base de données correspondant au paramètre ID passé
     * @param etablissement C'est l'objet à supprimer de la base de données
     */
    @Transactional
    public void delete(Etablissement etablissement){
        etablissementDao.delete(etablissement);
    }

    /**
     * Récupère un etablissement par son identifiant
     * @param id C'est le paramètre identifiant de l'etablissement à retrouver
     * @return Renvoi un objet etablissement si l'identifiant existe, null sinon
     */
    public Etablissement getById(Long id){ return etablissementDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les etablissements de la base
     * @return Renvoi la liste au module appelant
     */
    public List<Etablissement> getAll(){
        return Lists.newArrayList(etablissementDao.findAll());
    }

}
