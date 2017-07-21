package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Antonio RODRIGUES on 17/04/2017.
 * Projet Gestion des Rentes
 * Pour le STAC
 * Direction générale de l'aviation Civile
 */
@Entity
@Table(name= "PREUVEVIE")
public class Preuvevie {

    private long id;
    private String libel;
    private Date datedemande;
    private Date datereception;
    private Preuve preuve;
    private Rentier rentier;


    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    @Required
    @NotEmpty
    @Column(name = "LIBEL", nullable = false)
    public String getLibel() {
        return libel;
    }
    public void setLibel(String libel) {
        this.libel = libel;
    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEDEMANDE",nullable = false)
    public Date getDatedemande() {
        return datedemande;
    }
    public void setDatedemande(Date datedemande) {
        this.datedemande = datedemande;
    }

    //  La date de reception peut etre null tant que pas reçue
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATERECEPTION")
    public Date getDatereception() {
        return datereception;
    }
    public void setDatereception(Date datereception) {
        this.datereception = datereception;
    }

    @ManyToOne
    @JoinColumn(name = "ID_PREUVE", nullable = false)
    public Preuve getPreuve() {  return preuve;    }
    public void setPreuve(Preuve preuve) {
        this.preuve = preuve;
    }


    @ManyToOne
    @JoinColumn(name = "ID_RENTIER", nullable = false )
    public Rentier getRentier() {
        return rentier;
    }
    public void setRentier(Rentier rentier) {
        this.rentier = rentier;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preuvevie preuvevie = (Preuvevie) o;

        if (id != preuvevie.id) return false;
        if (!libel.equals(preuvevie.libel)) return false;
        if (!datedemande.equals(preuvevie.datedemande)) return false;
        if (datereception != null ? !datereception.equals(preuvevie.datereception) : preuvevie.datereception != null)
            return false;
        if (!preuve.equals(preuvevie.preuve)) return false;
        return rentier.equals(preuvevie.rentier);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        result = 31 * result + datedemande.hashCode();
        result = 31 * result + (datereception != null ? datereception.hashCode() : 0);
        result = 31 * result + preuve.hashCode();
        result = 31 * result + rentier.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Preuvevie{" +
                "id=" + id +
                ", libel='" + libel + '\'' +
                ", datedemande=" + datedemande +
                ", datereception=" + datereception +
                ", preuve=" + preuve +
                ", rentier=" + rentier +
                '}';
    }
}
