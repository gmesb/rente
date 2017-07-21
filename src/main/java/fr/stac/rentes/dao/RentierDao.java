package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Rentier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Projet Gestion des rentes
 * Créé le  13/04/2017.
 * L'interface DAO de l'objet Rentier
 * @author Antonio RODRIGUES
 */


public interface RentierDao extends JpaRepository<Rentier, Long> {

    // recherche par nom de famille
    List<Rentier>  findByNomfamilleIgnoreCase(String nomfamille);

    // recherche par nom de mariée ( pour les femmes )
    Rentier findByNommaritalIgnoreCase(String nommarital);

    // recherche seulement ceux en cours d'indemnisation
    List<Rentier>  findByNomfamilleAndFindroitIsNullIgnoreCase(String nom);

    List<Rentier>  findRentiersByFindroitIsNull();

    List<Rentier>  getByFindroitIsNull();

    // les patients sortis ( OK)
    List<Rentier> findAllByFindroitNotNull();

    // les présents
    List<Rentier> findAllByFindroitNull();



    ///   j'utilise ceux du dessous

    // recherche seulement ceux non indemnisés
    List<Rentier>  findByNomfamilleAndFindroitIsNotNullIgnoreCase(String nom);

    // tous ceux encore presents
    List<Rentier>  findAllByFindroitIsNull();

    List<Rentier> findByNomfamilleIsLikeIgnoreCase(String nom);

}




