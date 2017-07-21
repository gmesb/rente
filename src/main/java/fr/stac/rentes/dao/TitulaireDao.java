package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Titulaire;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Proget Gestion des rentes
 * Créé le  10/03/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface TitulaireDao extends JpaRepository<Titulaire, Long> {
    Titulaire findByLibelIgnoreCase(String libel);

}


