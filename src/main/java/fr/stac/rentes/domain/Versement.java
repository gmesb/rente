package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Projet GESTION DES RENTES
 * STAC
 * Créé le  20/04/2017.
 * Direction générale de l'aviation Civile
 * @author Antonio RODRIGUES
 */

@Entity
@Table(name= "VERSEMENT")
public class Versement {
    private long id;
    private Float montant;
    private Date dateversem;
    private String periodeversee;
    private Date dernierjourpaye;
    private boolean etatedite;
    private Renterevalorisee renterevalorisee;

    @Id
    @GeneratedValue
    @Column(name = "ID" )
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @NotNull
    @Required
    @DecimalMin("0.01")
    @Column(name = "MONTANT",nullable = false)
    public Float getMontant() {  return montant;  }
    public void setMontant(Float montant) {   this.montant = montant;  }


    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEVERSEM",nullable = false)
    public Date getDateversem() { return dateversem;  }
    public void setDateversem(Date dateversem) { this.dateversem = dateversem; }

    @NotEmpty
    @Required
    @Column(name = "PERIODEVERSEE",nullable = false)
    public String getPeriodeversee() {  return periodeversee; }
    public void setPeriodeversee(String periodeversee) {  this.periodeversee = periodeversee;  }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DERNIERJOURPAYE",nullable = false)
    public Date getDernierjourpaye() {       return dernierjourpaye;     }
    public void setDernierjourpaye(Date dernierjourpaye) {    this.dernierjourpaye = dernierjourpaye;   }


    @NotNull
    @Column(name = "ETATEDITE",nullable = false)
    public boolean isEtatedite() {   return etatedite;      }
    public void setEtatedite(boolean etatedite) {    this.etatedite = etatedite;    }

    @ManyToOne
    @JoinColumn(name = "ID_RENTEREVALORISEE", nullable = false)
    public Renterevalorisee getRenterevalorisee() {  return renterevalorisee;  }
    public void setRenterevalorisee(Renterevalorisee renterevalorisee) { this.renterevalorisee = renterevalorisee;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Versement versement = (Versement) o;

        if (id != versement.id) return false;
        if (etatedite != versement.etatedite) return false;
        if (!montant.equals(versement.montant)) return false;
        if (!dateversem.equals(versement.dateversem)) return false;
        if (!periodeversee.equals(versement.periodeversee)) return false;
        return dernierjourpaye.equals(versement.dernierjourpaye);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + montant.hashCode();
        result = 31 * result + dateversem.hashCode();
        result = 31 * result + periodeversee.hashCode();
        result = 31 * result + dernierjourpaye.hashCode();
        result = 31 * result + (etatedite ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Versement{" +
                "id=" + id +
                ", montant=" + montant +
                ", dateversem=" + dateversem +
                ", periodeversee='" + periodeversee + '\'' +
                ", dernierjourpaye=" + dernierjourpaye +
                ", etatedite=" + etatedite +
                ", renterevalorisee=" + renterevalorisee +
                '}';
    }
}

