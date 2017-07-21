package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Preuve;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Proget Gestion des rentes
 * Créé le  10/03/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface PreuveDao extends JpaRepository<Preuve, Long> {
    /**
     * Recherche l'élementPreuve par le Nom complet saisi et ignore la casse
     * Chaque Libéllé est unique
     * @param libel Paramètre reçu pour effectuer la recherche
     * @return Retourne le libellé trouvé ou null si rien de trouvé
     */

    Preuve findByLibelIgnoreCase(String libel);

}


