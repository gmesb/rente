package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.RevalorisationDao;
import fr.stac.rentes.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;


/**
 * Created Antonio RODRIGUES on 12/04/2017.
 * Pour STAC (Dierction Générale de l'Aviation Civile )
 *
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class RevalorisationService {
    private RevalorisationDao revalorisationDao;

    @Autowired
    public void setRevalorisationDao(RevalorisationDao revalorisationDao) {
        this.revalorisationDao = revalorisationDao;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Supprime l'élment "revalorisation" de la base de données
     * @param revalorisation C'est l'objet à supprimer
     */
    @Transactional
    public void delete(Revalorisation revalorisation){
        revalorisationDao.delete(revalorisation);
    }

    /**
     * Sauvegarde un objet "revalorisation" ou bien il est rajouté si le id est null
     * @param   revalorisation C'est l'ojet devant être sauvegardé
     */
    public void save(Revalorisation revalorisation){
        revalorisationDao.save(revalorisation);
    }

    /**
     * Récupère l'objet "revalorisation" par son identifiant
     * @param id identifiant de l'objet à retrouver
     * @return  l'objet revalorisation si l'identifiant existe, null sinon
     */
    public Revalorisation getById(Long id){
        return revalorisationDao.findOne(id);
    }

    /**
     * Récupère dans une Liste toutes les éléments de la table des revalorisations enregistrées dans la base
     * @return Lists Retourne une liste remplie avec tous les éléments de la table contenant les "Revalorisations" enregistrées
     */
    public List<Revalorisation> getAll(){
        return Lists.newArrayList(revalorisationDao.findAll());
    }

   /**
     * Retourne toutes les revalorisations qui n'ont pas encore été prises en compte pour revaloriser la rente du patient
     * pour une rente précise passée en paramètre et cela pour la période de référence passée en paramètres via une date de début et une date de fin.
     *
     * @param rente C'est l'objet de la rente en cours de traitement pour laquelle on doit vérifier s'il y a des mouvements de
     *                revalorisation à considérer et à traiter.
     * @param datDebut C'est le début de priode contenant les mouvements à récupérer
     * @param datFin    c'est la fin de période pour retenir les mouvements.
     *
     * @return Retourne une liste ordonnée des élements trouvés et qui doivent être traités.
     */
    public List<Revalorisation> AllRevalorisationNotIn(Rente rente,Date datDebut,Date datFin){
        return Lists.newArrayList(revalorisationDao.findAllByDaterevalorisationBetweenOrderByDaterevalorisationAsc(datDebut,datFin));
    };


    // TODO  requete SELECT * FROM RENTE ...  à déplacer

    /**
     * Retourne toutes les revalorisations qui n'ont pas encore été prises en compte pour revaloriser la rente du patient
     * pour une rente précise passée en paramètre et cela pour la période de référence passée en paramètres via une date de début et une date de fin.
     *
     * @param idRente C'est l'identifiant de la rente en cours de traitement pour laquelle on doit vérifier s'il y a des mouvements de
     *                revalorisation à considérer et à traiter.
     * @param datDebut C'est le début de priode contenant les mouvements à récupérer
     * @param datFin    c'est la fin de période pour retenir les mouvements.
     *
     * @return Retourne une liste ordonnée des élements trouvés et qui doivent être traités.
     */
    public List<Revalorisation> getRevalorisationNonValidee(Long idRente,String datDebut,String datFin ){

        String sql="SELECT * FROM revalorisation WHERE revalorisation.ID NOT IN(SELECT ID_REVALORISATION FROM renterevalorisee " + "WHERE ID_RENTE="+idRente+") " +
                    "AND daterevalorisation BETWEEN '"+datDebut+"' AND '"+datFin+"' ORDER BY daterevalorisation ASC";

        System.out.print(" Requete : "+sql);

        return jdbcTemplate.query(sql ,new RowMapper<Revalorisation>(){

            public Revalorisation mapRow(ResultSet rs, int rowNum) throws SQLException {
                Revalorisation r = new Revalorisation();

                r.setId(rs.getInt("id"));
                r.setLibel(rs.getString("libel"));
                r.setCoeff(rs.getFloat("coeff"));
                r.setDaterevalorisation(rs.getDate("daterevalorisation"));
                r.setDirective(rs.getString("directive"));
                return r;
            }
        });
    }
}
