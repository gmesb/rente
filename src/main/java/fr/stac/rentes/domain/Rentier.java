package fr.stac.rentes.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;


/**
 * Created by Antonuio RODRIGUES on 13/04/2017.
 * Pour STAC
 * Direction générale de l'aviation Civile
 * Gestion des rentes
 *
 */

@Entity
@Table(name = "RENTIER")
public class Rentier {
    private Long id;
    private String nomfamille;
    private String nommarital;
    private String prenom;
    private Date datenaissance;
    private int sexe;
    private String adresse1;
    private String adresse2;
    private String codepostal;
    private String ville;
    private Long dossier;
    private String banque;
    private String guichet;
    private String codebanque;
    private String compte;
    private String cle;
    private String iban;
    private Date findroit;
    private String motifarret;
    private Titulaire titulaire;
    private Grade grade;
    private User user;
    private Set<Rente> rentes;

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Required
    @NotBlank
    @Size(max = 100)
    @Column(name = "NOMFAMILLE")
    public String getNomfamille() {
        return nomfamille;
    }
    public void setNomfamille(String nomfamille) {
        this.nomfamille = nomfamille;
    }


    @Column(name = "NOMMARITAL")
    public String getNommarital() {
        return nommarital;
    }
    public void setNommarital(String nommarital) {
        this.nommarital = nommarital;
    }


    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    @Column(name = "PRENOM")
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    @NotNull
    @Required
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATENAISSANCE")
    public Date getDatenaissance() {
        return datenaissance;
    }
    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
    }


    @NotNull
    @Required
    @Column(name = "SEXE", nullable = false)
    public int getSexe() {
        return sexe;
    }
    public void setSexe(int sexe) {
        this.sexe = sexe;
    }


    @NotBlank
    @Required
    @Size(max = 100)
    @Column(name = "ADRESSE1")
    public String getAdresse1() {
        return adresse1;
    }
    public void setAdresse1(String adresse1) {
        this.adresse1 = adresse1;
    }

    @Basic
    @Column(name = "ADRESSE2", nullable = false)
    public String getAdresse2() {
        return adresse2;
    }
    public void setAdresse2(String adresse2) {
        this.adresse2 = adresse2;
    }


    @NotBlank
    @Required
    @Size(min = 5, max = 7)
    @Column(name = "CODEPOSTAL")
    public String getCodepostal() {
        return codepostal;
    }
    public void setCodepostal(String codepostal) {
        this.codepostal = codepostal;
    }


    @NotBlank
    @Size(max = 100)
    @Column(name = "VILLE")
    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }


    @Required
    @Column(name = "DOSSIER")
    public Long getDossier() {
        return dossier;
    }

    public void setDossier(Long dossier) {
        this.dossier = dossier;
    }


    @NotBlank
    @Required
    @Size(min = 1, max = 100)
    @Column(name = "BANQUE")
    public String getBanque() {
        return banque;
    }
    public void setBanque(String banque) {
        this.banque = banque;
    }


    @NotBlank
    @Required
    @Size(min = 1, max = 10)
    @Column(name = "GUICHET", nullable = false)
    public String getGuichet() {
        return guichet;
    }
    public void setGuichet(String guichet) {
        this.guichet = guichet;
    }


    @Size(min = 1, max = 10)
    @Required
    @NotNull
    @Column(name = "CODEBANQUE", nullable = false)
    public String getCodebanque() {
        return codebanque;
    }
    public void setCodebanque(String codebanque) {
        this.codebanque = codebanque;
    }


    @NotBlank
    @Required
    @Size(min = 1, max = 15)
    @Column(name = "COMPTE", nullable = false)
    public String getCompte() {
        return compte;
    }
    public void setCompte(String compte) {
        this.compte = compte;
    }


    @NotBlank
    @Required
    @Size(min = 2, max = 2)
    @Column(name = "CLE", nullable = false)
    public String getCle() { return cle;  }
    public void setCle(String cle) {  this.cle = cle;  }

    @NotBlank
    @Required
    @Size(min = 1, max = 25)
    @Column(name = "IBAN", nullable = false)
    public String getIban() {  return iban; }
    public void setIban(String iban) {   this.iban = iban; }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "FINDROIT", nullable = false)
    public Date getFindroit() {
        return findroit;
    }
    public void setFindroit(Date findroit) {
        this.findroit = findroit;
    }


    @Column(name = "MOTIFARRET")
    public String getMotifarret() {
        return motifarret;
    }
    public void setMotifarret(String motifarret) {
        this.motifarret = motifarret;
    }


    @NotNull
    @ManyToOne
    @JoinColumn(name = "ID_TITULAIRE", nullable = false)
    public Titulaire getTitulaire() {
        return titulaire;
    }
    public void setTitulaire(Titulaire titulaire) {
        this.titulaire = titulaire;
    }

    @ManyToOne
    @JoinColumn(name = "ID_GRADE", nullable = false)
    public Grade getGrade() {
        return grade;
    }
    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_USERS", nullable = false, referencedColumnName = "ID")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "rentier")
    public Set<Rente> getRentes() {
        return rentes;
    }
    public void setRentes(Set<Rente> rentes) {
        this.rentes = rentes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rentier rentier = (Rentier) o;

        if (sexe != rentier.sexe) return false;
        if (!id.equals(rentier.id)) return false;
        if (!nomfamille.equals(rentier.nomfamille)) return false;
        if (nommarital != null ? !nommarital.equals(rentier.nommarital) : rentier.nommarital != null) return false;
        if (!prenom.equals(rentier.prenom)) return false;
        if (!datenaissance.equals(rentier.datenaissance)) return false;
        if (!adresse1.equals(rentier.adresse1)) return false;
        if (adresse2 != null ? !adresse2.equals(rentier.adresse2) : rentier.adresse2 != null) return false;
        if (!codepostal.equals(rentier.codepostal)) return false;
        if (!ville.equals(rentier.ville)) return false;
        if (!dossier.equals(rentier.dossier)) return false;
        if (!banque.equals(rentier.banque)) return false;
        if (!guichet.equals(rentier.guichet)) return false;
        if (!codebanque.equals(rentier.codebanque)) return false;
        if (!compte.equals(rentier.compte)) return false;
        if (findroit != null ? !findroit.equals(rentier.findroit) : rentier.findroit != null) return false;
        if (motifarret != null ? !motifarret.equals(rentier.motifarret) : rentier.motifarret != null) return false;
        if (!titulaire.equals(rentier.titulaire)) return false;
        if (!grade.equals(rentier.grade)) return false;
        return user.equals(rentier.user);
    }



    @Override
    public String toString() {
        return "Rentier{" +
                "id=" + id +
                ", nomfamille='" + nomfamille + '\'' +
                ", nommarital='" + nommarital + '\'' +
                ", prenom='" + prenom + '\'' +
                ", datenaissance=" + datenaissance +
                ", sexe=" + sexe +
                ", adresse1='" + adresse1 + '\'' +
                ", adresse2='" + adresse2 + '\'' +
                ", codepostal='" + codepostal + '\'' +
                ", ville='" + ville + '\'' +
                ", dossier=" + dossier +
                ", banque='" + banque + '\'' +
                ", guichet='" + guichet + '\'' +
                ", codebanque='" + codebanque + '\'' +
                ", compte='" + compte + '\'' +
                ", cle='" + cle + '\'' +
                ", iban='" + iban + '\'' +
                ", findroit=" + findroit +
                ", motifarret='" + motifarret + '\'' +
                ", titulaire=" + titulaire +
                ", grade=" + grade +
                ", user=" + user +
                '}';
    }
}
