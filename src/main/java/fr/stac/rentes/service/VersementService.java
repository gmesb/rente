package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.VersementDao;
import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Renterevalorisee;
import fr.stac.rentes.domain.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * Created by Perso-ESIEE on 20/04/2017.
 * Pour STAC Gestion des rentes
 * Direction générale de l'aviation Civile
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class VersementService {
    private VersementDao versementDao;

    @Autowired
    public void setVersementDao(VersementDao versementDao) {
        this.versementDao = versementDao;
    }

    /**
     * Suprime le versement passe en paramètre
     * Il y a l'appel à l'interface DAO pour éxécuter l'opération dans la base de données
     * @param versement C'est l'objet à supprimer et passé en paramètre
     */
    @Transactional
    public void delete(Versement versement){
        versementDao.delete(versement);
    }

    /**
     * Faot la mise a jour en base de données l'objet Versement passé en paramètre
     *  * Il y a l'appel à l'interface DAO pour éxécuter l'opération dans la base de données
     * @param versement Le paramètre passée est l'objet devant être persisté dans la base de données
     */
    public void save(Versement versement){
        versementDao.save(versement);
    }

    /**
     * Récupère un identifiant par son ID
     * Toujours l'Interface DAO qui applique l'opération demandée
     * @param id  c'est l'identifiant du versement
     * @return retourne l'Objet retrouvé
     */
    public Versement getById(Long id){
        return versementDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les versements de la Table 'versement'  de la base
     * @return  C'est la liste retournée vers le demandeur
     */
    public List<Versement> getAll(){
        return Lists.newArrayList(versementDao.findAll());
    }


    /**
     * Renvoie tous les versements payés pour le jour  passé en paramètre
     * @param date C'est la date de demande passée
     * @return Renvoi une liste répondant aux conditions
     */
    public List<Versement> getAllByDernierjourpaye(Date date){
        return Lists.newArrayList(versementDao.getVersementsByDernierjourpaye(date));
    };
    /**
     * Recupere tous les versements liés à une période valorisée passée en paramètre
     * @param renterevalorisee C'est la période de référence passée
     * @return Retourne la liste résultat de la recherche
     */
    public List<Versement> getVersementsByRenterevalorisee(Renterevalorisee renterevalorisee) {
        return Lists.newArrayList(versementDao.getAllByRenterevaloriseeOrderByDernierjourpayeDesc(renterevalorisee));
    }

    /**
     * Recherche le versement le plus récent liés à une rente du patient quelque soit la période valorisée
     * Si aucun versement n'est trouvé, on retourne un Objet null
     * @param rente C'est la rente passée en paramètre
     * @return Retourne un Objet résultat  le versement trouvé
     */
    public Versement getLastVersement(Rente rente){
        Versement ver = versementDao.findFirstByRenterevalorisee_RenteIsOrderByDernierjourpayeDesc(rente);
        return ver == null? new Versement() : ver;
    };
}
