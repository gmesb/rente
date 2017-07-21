package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Rentier;
import fr.stac.rentes.domain.Versemtype;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Proget Gestion des rentes
 * Créé le  23/04/2017.
 *
 * @author Antonio RODRIGUES
 */


public interface RenteDao extends JpaRepository<Rente, Long> {

    /**
     * Recupere toutes les rentes non forfaitaires
     * @param versemtype est le type de versement : ici on passe le type forfaitaire à ne pas récuperer
     * @return Retourne la liste des Rentes accordées en ignorant celles qui sont forfaitaires
     */
    List<Rente> findAllByVersemtypeNotIn(Versemtype versemtype);

    /**
     * Retrouren toutes les rentes attribuées à un rentier passé en parametre
     * @param rentier C'est le rentier concerné par les rentes demandées en affichage
     * @return C'est une liste des rentes qui est retournée  pour le rentier précisé en paramètre
     */
    List<Rente> findAllByRentierEquals(Rentier rentier);

    /**
     * Retrouren toutes les rentes attribuées à un rentier passé en parametre
     * @param rentier C'est le rentier concerné par les rentes demandées en affichage
     * @return C'est une liste des rentes qui est retournée  pour le rentier précisé en paramètre
     */
    List<Rente> findRentesByRentierEquals(Rentier rentier);

    /**
     * Etablit une liste des rentes d'un rentier passé en paramètre et don le versement a toujours lieu et classés par la date de début de paiement
     * @param rentier C'est le rentier qui est passé en parametre et dont la liste des rentes est demandée
     * @return Retourne la liste demandée
     */
    List<Rente> getAllByRentierAndEtatrenteIsNullOrderByDateconsolidationDesc(Rentier rentier);

    /**
     * Donne le nombre de rentes attribuées à une rentier passé en paramètre.
     * @param rentier   C'est le rentier qui fait l'objet du nombre de demandes
     * @return Renvoi le nombre de rentes trouvé pour l'objet rentier passé en paramètre
     */
    int countAllByRentierEquals(Rentier rentier);

}




