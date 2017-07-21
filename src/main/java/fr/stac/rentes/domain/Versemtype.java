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
@Table(name= "VERSEMTYPE")
public class Versemtype {
    private long id;
    private String libel;
    private Set<Rente> rentes;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {      return id;    }
    public void setId(long id) {
        this.id = id;
    }

    @NotBlank
    @Size(min=2,max=50)
    @Column(name = "LIBEL")
    public String getLibel() {    return libel;  }
    public void setLibel(String libel) {  this.libel = libel; }


    @OneToMany(mappedBy = "versemtype")
    public Set<Rente> getRentes() { return rentes;   }
    public void setRentes(Set<Rente> rentes) { this.rentes = rentes;  }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Versemtype that = (Versemtype) o;

        if (id != that.id) return false;
        return libel.equals(that.libel);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Versemtype{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                '}';
    }
}

