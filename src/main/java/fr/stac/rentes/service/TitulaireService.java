package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.TitulaireDao;
import fr.stac.rentes.domain.Titulaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Antonio RODRIGUES on 12/04/2017.
 * Pour STAC (Direction générale de l'Aviation Civile )
 *
 * Author : Antonio RODRIGUES
 */
@Service
public class TitulaireService {
    private TitulaireDao titulaireDao;

    @Autowired
    public void setTitulaireDao(TitulaireDao titulaireDao) {
        this.titulaireDao = titulaireDao;
    }

    /**
     * Supprime un Titulaire par l'objet est passé en paramètre
     * @param titulaire L'objet passé  à supprimer
     */
    @Transactional
    public void delete(Titulaire titulaire){
        titulaireDao.delete(titulaire);
    }

    /**
     * Sauvegarde d'un objet dans la base, il y a ajout dans la tbale si titulaire.id est null
     * @param  titulaire c'est l'objet qui est passé en paramètre qui sera persisté
     */
    public void save(Titulaire titulaire){
        titulaireDao.save(titulaire);
    }

    /**
     * Récupère un Titulaire par son identifiant
     * @param id identifiant de Titulaire à retrouver
     * @return un Titulaire si l'identifiant existe, null sinon
     */
    public Titulaire getById(Long id){
        return titulaireDao.findOne(id);
    }

    /**
     * Récupère dans une Liste, tous les Titulaires de la base
     * @return C'est cette liste qui est retournée avec son contenu
     */
    public List<Titulaire> getAll(){
        return Lists.newArrayList(titulaireDao.findAll());
    }
}
