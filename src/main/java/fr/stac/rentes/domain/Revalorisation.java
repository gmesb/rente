package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

/**
 * Projet GESTION DES RENTES
 * STAC
 * Créé le  12/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio RODRIGUES
 */

@Entity
@Table(name= "REVALORISATION")
public class Revalorisation {
    private long id;
    private String libel;
    private Float coeff;
    private Date daterevalorisation;
    private String directive;
    private Set<Renterevalorisee> renterevalorisees;

    @Id
    @GeneratedValue
    @NotNull
    @Column(name = "ID")
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @NotBlank
    @Size(min=1, max=50)
    @Column(name = "LIBEL")
    public String getLibel() {    return libel;  }
    public void setLibel(String libel) {  this.libel = libel; }


    @NotBlank
    @Required
    @Size(min=1, max=50)
    @Column(name = "DIRECTIVE")
    public String getDirective() {        return directive;    }
    public void setDirective(String directive) {        this.directive = directive;    }

    @NotNull
    @Required
    @Column(name = "COEFF")
    public Float getCoeff() {     return coeff;     }
    public void setCoeff(Float coeff) {         this.coeff = coeff;      }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEREVALORISATION")
    public Date getDaterevalorisation() {        return daterevalorisation;    }
    public void setDaterevalorisation(Date daterevalorisation) {  this.daterevalorisation = daterevalorisation;    }


    @OneToMany(mappedBy = "revalorisation",cascade = CascadeType.MERGE)
    public Set<Renterevalorisee> getRenterevalorisees() { return renterevalorisees; }
    public void setRenterevalorisees(Set<Renterevalorisee> renterevalorisees) {this.renterevalorisees = renterevalorisees;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Revalorisation that = (Revalorisation) o;

        if (id != that.id) return false;
        if (!libel.equals(that.libel)) return false;
        if (!coeff.equals(that.coeff)) return false;
        if (!daterevalorisation.equals(that.daterevalorisation)) return false;
        return directive.equals(that.directive);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        result = 31 * result + directive.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Revalorisation{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                ", coeff=" + coeff +
                ", daterevalorisation=" + daterevalorisation +
                ", directive='" + directive + '\'' +
               '}';
    }
}

