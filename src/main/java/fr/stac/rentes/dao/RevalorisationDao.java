package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Revalorisation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

/**
 * Proget Gestion des rentes
 * Créé le  10/03/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface RevalorisationDao extends JpaRepository<Revalorisation, Long> {

    Revalorisation findByLibelIgnoreCase(String libel);


    // retourne les Revalorisations > ou = à la date passée
    ///List<Revalorisation>  findAllByDaterevalorisationIsGreaterThanEqual(Date datDebut );


    // retourne les Revalorisations entre deux dates  passées
    ///List<Revalorisation> findAllByDaterevalorisationBetween(Date datDebut,Date datFin );

    //List<Revalorisation> findAllByRentesNotContainsAndDaterevalorisationBetweenOrderByDaterevalorisationAsc(Rente rente,Date datDebut,Date datFin );

    //List<Revalorisation> findAllByRenterevaloriseesNotContainsAndDaterevalorisationBetweenOrderByDaterevalorisationAsc(Rente rente,Date datDebut,Date datFin);


    ///@Override
    ///List<Revalorisation> findAll();

   /// List<Revalorisation> findAllByRentesNotContainsAndDaterevalorisationBetweenOrderByDaterevalorisationAsc(Rente rente,Date datDebut,Date datFin );

    ///List<Revalorisation> findAllByDaterevalorisationDateBetweenAndRenterevaloriseesNotContains(Date datDebut,Date datFin,Rente rente);

    ///List<Revalorisation> findRevalorisationsByDaterevalorisationIsBetween(Date datDebut,Date datFin);


    List<Revalorisation> findAllByDaterevalorisationBetweenOrderByDaterevalorisationAsc(Date datDebut,Date datFin);

    //List<Revalorisation> findAllByRentesNotContainsAndDaterevalorisationBetweenOrderByDaterevalorisationAsc(Rente rente,Date datDebut,Date datFin );

    //Versement findFirstByRenterevalorisee_RenteIsOrderByDernierjourpayeDesc (Rente rente);
}


