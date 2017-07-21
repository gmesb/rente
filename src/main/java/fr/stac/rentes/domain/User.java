package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Required;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Projet Gestion rentes
 * Pour STAC
 * Direction générale de l'aviation Civile
 * Créé le  07/04/2017
 *
 * @author Antonio ROSRIGUES
 */

@Entity
@Table(name = "USERS")
public class User {
    private Long id;
    private String identif;
    private String mdp;
    private String nom;
    private String prenom;
    private String telephone;
    private boolean actif;
    private String email;
    private Etablissement etablissement;
    private Set<Profil> profils;

    //   -----  pour confirmation de mot de passe ----------------
    /**
     * Deux variables de l'objet qui servent à controler et modifier le mot de passe
     * non persistantes en Base de données.
     */
    private String mdpconf;
    private String oldmdp;


    /**
     * Variable qui sert à verifier la saisie du nouveau de passe compatible (confirmation)
     * @return Renvoi la confirmation du mot de passe pour être traité dans l'application (controleur)
     */
    @Basic
    @Column(updatable = false, insertable = false, name = "MDP", nullable = true)
    public String getMdpconf() {  return mdpconf;   }
    public void setMdpconf(String mdpconf) {  this.mdpconf = mdpconf;  }

    /**
     * L'utilisateur doit saisir l'ancien mot de passe  pour être autorisé à le changer
     * @return L'ancien mot de passe est retourné pour être
     */
    @Basic
    @Column(updatable = false, insertable = false, name = "MDP", nullable = true)
    public String getOldmdp() {        return oldmdp;    }
    public void setOldmdp(String oldmdp) {        this.oldmdp = oldmdp;    }

    //   -----  pour confirmation de mot de passe ----------------


    @Id
    @NotNull
    @Column(name = "ID", nullable = false)
    @GeneratedValue
    public Long getId() {        return id;    }
    public void setId(Long id) {    this.id = id;    }


    @NotBlank
    @Required
    @Length(min=5,max=16)
    @Column(name = "IDENTIF", nullable = false)
    public String getIdentif() {   return identif;   }
    public void setIdentif(String identif) {  this.identif = identif;   }

    @NotBlank
    @Required
    @Length(min=5,max=70)
    @Column(name = "MDP", nullable = false)
    public String getMdp() {  return mdp;  }
    public void setMdp(String mdp) {   this.mdp = mdp;  }

    @NotBlank
    @Required
    @Length(min=2,max=50)
    @Column(name = "NOM", nullable = false )
    public String getNom() { return nom;  }
    public void setNom(String nom) { this.nom = nom;    }


    @NotBlank
    @Required
    @Length(min=2,max=50)
    @Column(name = "PRENOM", nullable = false )
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    @Basic
    @Column(name = "TELEPHONE")
    public String getTelephone() { return telephone;  }
    public void setTelephone(String telephone) { this.telephone = telephone;  }

    @NotNull
    @Column(name = "ACTIF", nullable = false)
    public boolean getActif() { return actif;  }
    public void setActif(boolean actif) { this.actif = actif;  }

    @Basic
    @Column(name = "EMAIL")
    @Email
    public String getEmail() { return email;  }
    public void setEmail(String email) { this.email = email;  }


    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_ETABLISSEMENT", nullable = true)
    public Etablissement getEtablissement() {
        return etablissement;
    }
    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

   // @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="USERPROFIL",
            joinColumns = @JoinColumn(name = "ID_USERS", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ID_PROFIL", referencedColumnName = "ID"))
    public Set<Profil> getProfils() {       return profils;     }
    public void setProfils(Set<Profil> profils) {  this.profils = profils;  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!identif.equals(user.identif)) return false;
        if (!nom.equals(user.nom)) return false;
        if (!prenom.equals(user.prenom)) return false;
        if (telephone != null ? !telephone.equals(user.telephone) : user.telephone != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return etablissement.equals(user.etablissement);
    }

    @Override
    public int hashCode() {
        int result = identif.hashCode();
        result = 31 * result + nom.hashCode();
        result = 31 * result + prenom.hashCode();
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + etablissement.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", identif='" + identif + '\'' +
                ", mdp='" + mdp + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", actif=" + actif +
                ", email='" + email + '\'' +
                '}';
    }
}


