package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Projet GESTION DES RENTES
 * STAC
 * Créé le  12/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio RODRIGUES
 */

@Entity(name="Profil")      // nom de la class
@Table(name= "PROFIL")
public class Profil {
    private long id;
    private String nom;
    private String role;
    private Set<User> users;

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @NotBlank
    @Size(min=3, max=100)
    @Column(name = "NOM", nullable = false)
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }


    @Size(min=6, max=15)
    @NotBlank
    @Column(name = "ROLE", nullable = false )
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }

    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "profils")
    public Set<User> getUsers() {
        return users;
    }
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Profil profil = (Profil) o;

        if (!nom.equals(profil.nom)) return false;
        return role.equals(profil.role);
    }

    @Override
    public int hashCode() {
        int result = nom.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Profil{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}

