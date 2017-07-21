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
 * * Direction générale de l'aviation Civile
 * @author Antonio RODRIGUES
 */

@Entity
@Table(name= "TITULAIRE")
public class Titulaire{
    private long id;
    private String libel;
    private Set<Rentier> rentiers;

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
    @Size(min=2,max=50)
    @Column(name = "LIBEL", nullable = false )
    public String getLibel() {    return libel;  }
    public void setLibel(String libel) {  this.libel = libel; }

    @OneToMany(mappedBy = "titulaire")
    public Set<Rentier> getRentiers() { return rentiers; }
    public void setRentiers(Set<Rentier> rentiers) {  this.rentiers = rentiers; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Titulaire titulaire = (Titulaire) o;

        if (id != titulaire.id) return false;
        return libel.equals(titulaire.libel);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Titulaire{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                '}';
    }
}

