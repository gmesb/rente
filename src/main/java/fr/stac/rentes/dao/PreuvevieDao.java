package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Preuvevie;
import fr.stac.rentes.domain.Rentier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Antonio RODRIGUES on 17/04/2017.
 * Projet Gestion des Rentes
 * STAC (Direction Générale de l'aviation Civile)
 *
 */
public interface PreuvevieDao extends JpaRepository<Preuvevie, Long> {

   /* *//**
     * Recherche l'élement Preuvevie par le Nom complet saisi et ignore la casse
     * Chaque Libéllé est unique
     * @param libel Paramètre reçu pour effectuer la recherche
     * @return Retourne le libellé trouvé ou null si rien de trouvé
     *//*
    Preuvevie findByLibelIgnoreCase(String libel);*/



    /**
     * Permet d'obtenir la liste d'objets 'preuvevie'  corerspodant au rentier passé en paramètre
     * @param rentier C'est la paramétre Rentier donc un Objet
     * @return retourne une liste de mouvements de 'Preuve de vie' enregistrés dans la table
     */
    List<Preuvevie> getAllByRentierEqualsOrderByDatedemandeDesc(Rentier rentier);
}
