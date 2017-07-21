package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.VueversementDao;
import fr.stac.rentes.domain.Vueversement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Created by Perso-ESIEE on 12/04/2017.
 * Pour STAC (Direction Générale de l'Aviation Civile )
 *
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class VueversementService {


    private VueversementDao vueversementDao;

    @Autowired
    public void setVueversementDao(VueversementDao vueversementDao) {
        this.vueversementDao = vueversementDao;
    }

    /**
     * Sauvegarde d'un Objet vueversement, ajouté si location.id est null
     * @param   vueversement C'est l'objet a persister dans la base
     */
    public void save(Vueversement vueversement){
        vueversementDao.save(vueversement);
    }


    /**
     * Récupère dans une Liste tous les éléments de cettes table vueversement de la base
     * @return Renvoi une liste complète
     */
    public List<Vueversement> getAll(){
        return Lists.newArrayList(vueversementDao.findAll());
    }

}
