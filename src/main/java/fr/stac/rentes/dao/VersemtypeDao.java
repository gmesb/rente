package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Versemtype;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Proget Gestion des rentes
 * Créé le  10/03/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface VersemtypeDao extends JpaRepository<Versemtype, Long> {
    Versemtype findByLibelIgnoreCase(String libel);

}


