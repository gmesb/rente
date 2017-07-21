package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Projet Gestion des rentes
 * Pour STAC
 * * Direction générale de l'aviation Civile
 * Créé le  07/04/2017
 *
 * @author Antonio RODRIGUES
 */

@Entity(name="Etablissement")
@Table(name = "ETABLISSEMENT")
public class Etablissement {
    private Long id;
    private String nom;
    private String adresse1;
    private String adresse2;
    private String codepostal;
    private String ville;
    private String telephone;
    private String email;
    private String responsable;
    private Set<User> users;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank
    @Size(min=1, max=100)
    @Column(name = "NOM", nullable = false)
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    @NotBlank
    @Size(min=1, max=150)
    @Column(name = "ADRESSE1", nullable = false)
    public String getAdresse1() {         return adresse1;     }
    public void setAdresse1(String adresse1) {         this.adresse1 = adresse1;     }

    @Basic
    @Column(name = "ADRESSE2")
    public String getAdresse2() {         return adresse2;     }
    public void setAdresse2(String adresse2) {         this.adresse2 = adresse2;     }

    @NotBlank
    @Size(min=5, max=8)
    @Column(name = "CODEPOSTAL", nullable = false)
    public String getCodepostal() {
        return codepostal;
    }
    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }

    @NotNull
    @Size(min=2, max=150)
    @Column(name = "VILLE", nullable = false)
    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }

    @Basic
    @Column(name = "TELEPHONE")
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "RESPONSABLE" )
    public String getResponsable() {         return responsable;     }
    public void setResponsable(String responsable) { this.responsable = responsable;   }


    @OneToMany(mappedBy = "etablissement")
    public Set<User> getUsers() {    return users;  }
    public void setUsers(Set<User> users) {   this.users = users;  }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Etablissement that = (Etablissement) o;

        if (!nom.equals(that.nom)) return false;
        if (!adresse1.equals(that.adresse1)) return false;
        if (!adresse2.equals(that.adresse2)) return false;
        if (!codepostal.equals(that.codepostal)) return false;
        return ville.equals(that.ville);
    }

    @Override
    public int hashCode() {
        int result = nom.hashCode();
        result = 31 * result + adresse1.hashCode();
        result = 31 * result + adresse2.hashCode();
        result = 31 * result + codepostal.hashCode();
        result = 31 * result + ville.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Etablissement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse1='" + adresse1 + '\'' +
                ", adresse2='" + adresse2 + '\'' +
                ", codepostal='" + codepostal + '\'' +
                ", ville='" + ville + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", responsable='" + responsable + '\'' +
                '}';
    }
}
