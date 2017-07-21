package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Projet Gestion des rentes
 * STAC (Direction Générale de l'aviation Civile)
 * Créé le  10/04/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface GradeDao extends JpaRepository<Grade, Long> {
    /**
     * Recherche l'élement Grade par le Nom complet saisi et ignore la casse
     * Chaque Libéllé est unique
     * @param libel Paramètre reçu pour effectuer la recherche
     * @return Retourne le libellé trouvé ou null si rien de trouvé
     */
    Grade findByLibelIgnoreCase(String libel);

}


