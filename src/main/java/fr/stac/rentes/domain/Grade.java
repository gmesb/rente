package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * Projet GESTION DES RENTES
 * STAC (Direction Générale de l'Aviation Coivile )
 * Créé le  12/04/2017.
 * Le modèle de données du domaine
 * @author Antonio RODRIGUES
 */

@Entity(name="Grade")               //nom de la class
@Table(name = "GRADE")
public class Grade {
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
    @Size(min=3, max = 100)
    @Column(name = "LIBEL", nullable = false )
    public String getLibel() {    return libel;  }
    public void setLibel(String libel) {  this.libel = libel; }


    @OneToMany(mappedBy = "grade")
    public Set<Rentier> getRentiers() { return rentiers; }
    public void setRentiers(Set<Rentier> rentiers) {  this.rentiers = rentiers; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        return libel.equals(grade.libel);
    }

    @Override
    public int hashCode() {
        return libel.hashCode();
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                '}';
    }
}

