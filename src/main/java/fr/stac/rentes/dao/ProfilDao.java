package fr.stac.rentes.dao;


import fr.stac.rentes.domain.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Projet GESTION DES RENTES
 * Pour STAC
 *
 * Créé le 12/04/2017.
 *
 * @author Antonio Rodrigues
 */


/*
public interface GroupsDao extends CrudRepository<Groups, Long> {
    Groups findByNomIgnoreCase(String nom);

}*/

public interface ProfilDao extends JpaRepository<Profil, Long> {
    Profil findByNomIgnoreCase(String nom);

}


