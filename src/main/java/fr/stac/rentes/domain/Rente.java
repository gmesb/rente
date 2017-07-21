package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by Antonio RODRIGUES on 17/04/2017.
 * Projet Gestion des Rentes
 * Pour le STAC
 *
 */
@Entity
@Table(name= "RENTE")
public class Rente {
    private long id;
    private Date dateconsolidation;
    private String libel;
    private Date dateaccident;
    private Float txippaycause;
    private Float txippaydroit;
    private Float mntrenteinitial;
    private Date etatrente;
    private Rentier rentier;
    private Versemtype versemtype;
    private Set<Renterevalorisee> renterevalorisees;

    //   -----  pour traitement facilité ----------------

    private Date datefinrente;

    @Basic
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(updatable = false, insertable = false, name = "ETATRENTE" )
    public Date getDatefinrente() {  return datefinrente;   }
    public void setDatefinrente(Date datefinrente) { this.datefinrente = datefinrente;  }

    //   -----   pour traitement facilité --------------------


    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    @NotEmpty
    @Required
    @NotNull
    @Size(min=1,max=50)
    @Column(name = "LIBEL", nullable = false )
    public String getLibel() {
        return libel;
    }
    public void setLibel(String libel) {
        this.libel = libel;
    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATECONSOLIDATION", nullable = true)
    public Date getDateconsolidation() { return dateconsolidation;    }
    public void setDateconsolidation(Date dateconsolidation) {
        this.dateconsolidation = dateconsolidation;
    }

    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATEACCIDENT", nullable = true)
    public Date getDateaccident() {
        return dateaccident;
    }
    public void setDateaccident(Date dateaccident) {
        this.dateaccident = dateaccident;
    }

    @Required
    @NotNull
    @Column(name = "TXIPPAYCAUSE", nullable = false )
    public Float getTxippaycause() {
        return txippaycause;
    }
    public void setTxippaycause(Float txippaycause) {
        this.txippaycause = txippaycause;
    }

    @Required
    @NotNull
    @Column(name = "TXIPPAYDROIT", nullable = false)
    public Float getTxippaydroit() {
        return txippaydroit;
    }
    public void setTxippaydroit(Float txippaydroit) {
        this.txippaydroit = txippaydroit;
    }

    // @NotEmpty    // ce n'est pas un bon type c'est pour STring
    @Required
    @NotNull
    @Column(name = "MNTRENTEINITIAL", nullable = false)
    public Float getMntrenteinitial() {
        return mntrenteinitial;
    }
    public void setMntrenteinitial(Float mntrenteinitial) {
        this.mntrenteinitial = mntrenteinitial;
    }

    @Basic
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ETATRENTE" )
    public Date getEtatrente() {
        return etatrente;
    }
    public void setEtatrente(Date etatrente) {
        this.etatrente = etatrente;
    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_RENTIER", nullable = false)
    public Rentier getRentier() {     return rentier;   }
    public void setRentier(Rentier rentier) {     this.rentier = rentier;    }

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_VERSEMTYPE", nullable = false)
    public Versemtype getVersemtype() {
        return versemtype;
    }
    public void setVersemtype(Versemtype versemtype) {
        this.versemtype = versemtype;
    }

    @OneToMany(mappedBy = "rente")
    public Set<Renterevalorisee> getRenterevalorisees() {  return renterevalorisees;  }
    public void setRenterevalorisees(Set<Renterevalorisee> renterevalorisees) { this.renterevalorisees = renterevalorisees; }


    /* @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="RENTEREVALORISEE",
            joinColumns = @JoinColumn(name = "ID_RENTE", referencedColumnName = "ID"),
            inverseJoinColumns = {@JoinColumn(name = "ID_REVALORISATION", referencedColumnName = "ID")} )
    public Set<Revalorisation> getRevalorisations() {
        return revalorisations;
    }
    public void setRevalorisations(Set<Revalorisation> revalorisations) {
        this.revalorisations = revalorisations;
    }
*/

   /* @OneToMany(mappedBy = "rente")
    public List<Versement> getVersements() { return versements;  }
    public void setVersements(List<Versement> versements) {  this.versements = versements; }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rente rente = (Rente) o;

        if (id != rente.id) return false;
        if (!dateconsolidation.equals(rente.dateconsolidation)) return false;
        if (!libel.equals(rente.libel)) return false;
        if (!dateaccident.equals(rente.dateaccident)) return false;
        if (!txippaycause.equals(rente.txippaycause)) return false;
        if (!txippaydroit.equals(rente.txippaydroit)) return false;
        if (!mntrenteinitial.equals(rente.mntrenteinitial)) return false;
        if (etatrente != null ? !etatrente.equals(rente.etatrente) : rente.etatrente != null) return false;
        if (rentier != null ? !rentier.equals(rente.rentier) : rente.rentier != null) return false;
        return versemtype.equals(rente.versemtype);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + libel.hashCode();
        result = 31 * result + (etatrente != null ? etatrente.hashCode() : 0);
        result = 31 * result + (datefinrente != null ? datefinrente.hashCode() : 0);
        result = 31 * result + (rentier != null ? rentier.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "Rente{" +
                "id=" + id +
                ", dateconsolidation=" + dateconsolidation +
                ", libel='" + libel + '\'' +
                ", dateaccident=" + dateaccident +
                ", txippaycause=" + txippaycause +
                ", txippaydroit=" + txippaydroit +
                ", mntrenteinitial=" + mntrenteinitial +
                ", etatrente=" + etatrente +
                ", rentier=" + rentier +
                ", versemtype=" + versemtype +
                ", datefinrente=" + datefinrente +
                '}';
    }
}
