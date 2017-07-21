package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;


/**
 * Projet GESTION DES RENTES
 * STAC
 * Créé le  12/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio RODRIGUES
 */

@Entity
@Table(name= "PREUVE")
public class Preuve {
    private long id;
    private String libel;
    private Set<Preuvevie> preuvevies;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @NotBlank
    @Size(min=1,max=50)
    @Column(name = "LIBEL")
    public String getLibel() {    return libel;  }
    public void setLibel(String libel) {  this.libel = libel; }


    @OneToMany(mappedBy = "preuve")
    public Set<Preuvevie> getPreuvevies() {  return preuvevies;  }
    public void setPreuvevies(Set<Preuvevie> preuvevies) {  this.preuvevies = preuvevies;  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preuve preuve = (Preuve) o;

        if (id != preuve.id) return false;
        return libel.equals(preuve.libel);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Preuve{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                '}';
    }
}

