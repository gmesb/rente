package fr.stac.rentes.dao;

import fr.stac.rentes.domain.Etablissement;
import org.springframework.data.repository.CrudRepository;

/**Projet Gestion des rentes
 * Par Antonio Rodrigues
 * Pour le STAC
 * Créé le  10/04/2017.
 *
 * @author Antonio RODRIGUES
 */

public interface EtablissementDao extends CrudRepository<Etablissement, Long>{
    Etablissement findByNomIgnoreCase(String nom);
}
