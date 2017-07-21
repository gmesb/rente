package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Renterevalorisee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


/**
 * Proget Gestion des rentes
 * STAC (Direction Générale de l'Aviation Civile )
 * Créé le  13/04/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface RenterevaloriseeDao extends JpaRepository<Renterevalorisee, Long> {

    List<Renterevalorisee> findAllByRenteIsLikeOrderByDatedeb (Rente rente);

    /**
     * Retourne le mouvement de revalorisation le plus récent afin de calculer les données du mouvement suivant
     * @param rente On passe l'identifiant de la rente à mouvementer ( nouveau mouvement à créer dans la base de données)
     * @return Retourne le dernier mouvement persisté dans la base de données
     */
    Renterevalorisee getFirstByRenteOrderByDatefinDesc(Rente rente);


    /**
     * Renvoi toutes les revalorisations attribuées à une Rente passée en paramètre
     * @param rente Paramètre passée
     * @return Renvoi une liste
     */
    List<Renterevalorisee> getAllByRente(Rente rente);

    /**
     * Nombre de mouvements de revalorisation pour une rente
     * @param rente paramètre passé
     * @return Retourne un nombre de mouvements
     */
    int countAllByRenteEquals(Rente rente);

}



