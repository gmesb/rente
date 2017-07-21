package fr.stac.rentes.dao;

import fr.stac.rentes.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Projet Gestion des rentes
 * Créé le  05/04/2017 pour le compte du STAC(Direction Générale de l'Aviation Civile)
 * @author Antonio RODRIGUES
 */

/**
 * Cette Interface DAO permet la persistance des données dans la base concernant l'objet User ( Utilisateurs )
 */
public interface UserDao extends JpaRepository<User, Long> {
    User findByNomIgnoreCase(String nom);

     /**
     * recherche par code Login (v Identifiant ) sans condition d'activité en cours
     * @param identif L'identifiant de l'utilisateur qui veut se connecter doit contenir minimum 5 carateres et au maximum 16 caracteres
     * @return retourne l'identifiant de l'utilisateur
     */
    User findByIdentifIgnoreCase(String identif);


    /**
     * charge les utilisateurs actifs uniquement en recevant deux paramètres
     * @param identif   L'identifiant de l'utilisateur qui veut se connecter doit contenir minimum 5 carateres et au maximum 16 caracteres
     * @param actif   L'utilisateur peut être en activité ou non, c'est l'administrateur seul qui peut chager cet état
     * @return Récupere l'identifiant de l'utilisateur qui est en activité en ignorant la casse
     */
     User findByIdentifIgnoreCaseAndActifEquals(String identif,boolean actif);
}




