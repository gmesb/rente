package fr.stac.rentes.domain;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Created by Antonio Rodrigues 19/04/2017.
 * Projet : Gestion des rentes
 * Pour :  STAC
 * Direction générale de l'aviation Civile
 */

@Entity(name="Renterevalorisee")   //  nom de la Class
@Table(name= "RENTEREVALORISEE")
public class Renterevalorisee {
    private Long id;
    private Date datelancement;
    private Date datedeb;
    private Date datefin;
    private Float montantrevalorise;
    private Rente rente;
    private Revalorisation revalorisation;
    private Set<Versement> versements;

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false  )
    public Long getId() {    return id;     }
    public void setId(Long id) {  this.id = id;    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATELANCEMENT" ,nullable = false )
    public Date getDatelancement() {
        return datelancement;
    }
    public void setDatelancement(Date datelancement) {
        this.datelancement = datelancement;
    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEDEB" , nullable = false )
    public Date getDatedeb() {        return datedeb;     }
    public void setDatedeb(Date datedeb) {
        this.datedeb = datedeb;
    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEFIN" , nullable = false )
    public Date getDatefin() {        return datefin;     }
    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    @NotNull
    @Required
    @Column(name = "MONTANTREVALORISE" ,nullable = false)
    public Float getMontantrevalorise() {  return montantrevalorise;    }
    public void setMontantrevalorise(Float montantrevalorise) {
        this.montantrevalorise = montantrevalorise;
    }


    @ManyToOne
    @JoinColumn(name = "ID_RENTE",nullable = false)
    public Rente getRente() {
        return rente;
    }
    public void setRente(Rente rente) {
        this.rente = rente;
    }

    @ManyToOne
    @JoinColumn(name = "ID_REVALORISATION",nullable = false)
    public Revalorisation getRevalorisation() {
        return revalorisation;
    }
    public void setRevalorisation(Revalorisation revalorisation) {
        this.revalorisation = revalorisation;
    }


    @OneToMany(mappedBy = "renterevalorisee")
    public Set<Versement> getVersements() { return versements;   }
    public void setVersements(Set<Versement> versements) { this.versements = versements; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Renterevalorisee that = (Renterevalorisee) o;

        if (!id.equals(that.id)) return false;
        if (!datelancement.equals(that.datelancement)) return false;
        if (!datedeb.equals(that.datedeb)) return false;
        if (!datefin.equals(that.datefin)) return false;
        if (!montantrevalorise.equals(that.montantrevalorise)) return false;
        if (!rente.equals(that.rente)) return false;
        return revalorisation.equals(that.revalorisation);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + datelancement.hashCode();
        result = 31 * result + datedeb.hashCode();
        result = 31 * result + datefin.hashCode();
        result = 31 * result + montantrevalorise.hashCode();
        result = 31 * result + rente.hashCode();
        result = 31 * result + revalorisation.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Renterevalorisee{" +
                "id=" + id +
                ", datelancement=" + datelancement +
                ", datedeb=" + datedeb +
                ", datefin=" + datefin +
                ", montantrevalorise=" + montantrevalorise +
                ", rente=" + rente +
                ", revalorisation=" + revalorisation +
                '}';
    }
}
