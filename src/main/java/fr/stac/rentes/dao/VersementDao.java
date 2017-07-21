package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Renterevalorisee;
import fr.stac.rentes.domain.Versement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Proget Gestion des rentes
 * Créé le  20/04/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface VersementDao extends JpaRepository<Versement, Long> {

    Set<Versement>  getAllByRenterevaloriseeOrderByDernierjourpayeDesc(Renterevalorisee renterevalorisee);

    List<Versement> getAllByRenterevalorisee(Renterevalorisee renterevalorisee);

    List<Versement> getVersementsByDernierjourpaye(Date dat);

    List<Versement> getAllByRenterevaloriseeEquals(Renterevalorisee renterevalorisee );

    Versement  findFirstByRenterevalorisee_RenteIsOrderByDernierjourpayeDesc (Rente rente);
}


