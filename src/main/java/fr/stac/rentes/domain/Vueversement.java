package fr.stac.rentes.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


/**
 * Projet GESTION DES RENTES
 * STAC
 * Créé le  12/04/2017.
 *
 * @author Antonio RODRIGUES
 */

@Entity(name="Vueversement")
@Table(name= "vordreliquidatif")
public class Vueversement {
    private long versementid;
    private long rentierid;
    private int dossier;
    private String nomfamille;
    private String nommarital;
    private String prenom;
    private Date datenaissance;
    private long renteid;

    @Id
    @Basic
    @Column(updatable = false, insertable = false, name = "VERSEMENTID", nullable = false)
    public long getVersementid() {   return versementid;  }
    public void setVersementid(long versementid) { this.versementid = versementid;  }

    @Basic
    @Column(updatable = false, insertable = false, name = "RENTIERID", nullable = false)
    public long getRentierid() {  return rentierid; }
    public void setRentierid(long rentierid) {  this.rentierid = rentierid;  }

    @Basic
    @Column(updatable = false, insertable = false, name = "DOSSIER", nullable = false)
    public int getDossier() {   return dossier;   }
    public void setDossier(int dossier) {   this.dossier = dossier;  }

    @Basic
    @Column(updatable = false, insertable = false, name = "NOMFAMILLE", nullable = false)
    public String getNomfamille() {   return nomfamille;    }
    public void setNomfamille(String nomfamille) {  this.nomfamille = nomfamille; }

    @Basic
    @Column(updatable = false, insertable = false, name = "NOMMARITAL", nullable = false)
    public String getNommarital() {   return nommarital;  }
    public void setNommarital(String nommarital) { this.nommarital = nommarital; }

    @Basic
    @Column(updatable = false, insertable = false, name = "PRENOM", nullable = false)
    public String getPrenom() {  return prenom;   }
    public void setPrenom(String prenom) {   this.prenom = prenom;  }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(updatable = false, insertable = false, name = "DATENAISSANCE", nullable = false)
    public Date getDatenaissance() { return datenaissance;  }
    public void setDatenaissance(Date datenaissance) { this.datenaissance = datenaissance;  }

    @Basic
    @Column(updatable = false, insertable = false, name = "RENTEID", nullable = false)
    public long getRenteid() {   return renteid;  }
    public void setRenteid(long renteid) { this.renteid = renteid;  }



}

