package fr.stac.rentes.dao;


import fr.stac.rentes.domain.Vueversement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Proget Gestion des rentes
 * Créé le  20/04/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface VueversementDao extends JpaRepository<Vueversement, Long> {

    List<Vueversement> getAllByRenteid(Long id);

    Vueversement getByVersementid(Long id);
    Vueversement findOne(Long id);

    /*
    VueVersement findTopByDernierjourpayeOrderByDernierjourpayeDesc(Rente rente);

    VueVersement getFirstByRenteOrderByDernierjourpayeDesc(Rente rente);

    Set<VueVersement> getAllByRenteOrderByDernierjourpayeDesc(Rente rente);

    List<VueVersement> getAllByRente(Rente rente);

    List<VueVersement> getVersementsByDernierjourpaye(Date dat);*/


}


