package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.RenteDao;
import fr.stac.rentes.dao.RentierDao;
import fr.stac.rentes.dao.VersemtypeDao;
import fr.stac.rentes.domain.Rente;
import fr.stac.rentes.domain.Rentier;
import fr.stac.rentes.domain.Versemtype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * Projet Gestion des Rentes
 * Direction générale de l'aviation Civile
 * Créé le  13/04/2017.
 *
 * @author Antonio Rodrigues
 */

@Service
public class RenteService {
    private RenteDao renteDao;
    private RentierDao rentierDao;
    private VersemtypeDao versemtypeDao;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setRenteDao(RenteDao renteDao) {
        this.renteDao = renteDao;
    }
    @Autowired
    public void setRentierDao(RentierDao rentierDao) {
        this.rentierDao = rentierDao;
    }
    @Autowired
    public void setVersemtypeDao(VersemtypeDao versemtypeDao) {
        this.versemtypeDao = versemtypeDao;
    }

    /**
     * Sauvegarde d'une rente dans la base de données, il y a ajout si rente.id est null
     * Si Id n'existe pas, c'est une insertion qui se fera
     *
     * @param rente C'est l'objet Rente qui est persisté en BDD
     *
     */
    @Transactional
    public void save(Rente rente){
        renteDao.save(rente);
    }


    /**
     * Dans ce cas, il y a suppression de l'objet passé en paramètre,
     * @param rente c'est l'objet 'rente' qui doit être supprimé
     */
    @Transactional
    public void delete(Rente rente){
        renteDao.delete(rente);
    }


    /**
     * Récupère une Rente par son identifiant unique passé
     * @param id L'identifiant de la Rente à retrouver
     * @return un objet Rente est renvoyé si l'identifiant existe, null sinon
     */
    public Rente getById(Long id){
        return renteDao.findOne(id);
    }

    /**
     * Récupère dans une Liste de toutes les rentes de la base
     * @return Cette liste renvoyée contient la totalité des éléments de la table
     */
    public List<Rente> getAll(){
        return Lists.newArrayList(renteDao.findAll());
    }

    /**
     * Récupère dans une Liste toutes les rentes d'un rentier passé en paramètre
     * @return C'est la liste des rentes attribuées à un rentier précis passé en paramètre
     * @param rentier Ce paramètre passé permet d'avoir la liste limitée
     */
     public List<Rente> getRentesByRentier(Rentier rentier){
        return Lists.newArrayList(renteDao.findAllByRentierEquals(rentier));
    }

    /**
     * Récupère dans une Liste toutes les rentes d'un rentier passé en paramètre
     * @param rentier C'est l'objet passé en paramètre concerné par la liste des rentes associées
     * @return C'est la liste des rentes attribuées à un rentier précis passé en paramètre
     */
    public List<Rente> getAllRentesByRentier(Rentier rentier){
        return Lists.newArrayList(renteDao.findRentesByRentierEquals(rentier));
    };


    /**
     *  On recupere les rentes dont le type de paiement n'est pa forfaire, le Type 3  = Forfait de rente pour enregistrer les revalorisations
     * @param versemtype C'est le type de rente forfaitaire qu'il ne faut pas récupérer car il n'y a pas de revalorisation
     *                   à appliquer sur un forfait
     * @return Retourne la liste de toutes les rentes sauf les rentes forfaitaires
     */
    public List<Rente> allRentesPourRevalorisation(Versemtype versemtype){
        return Lists.newArrayList(renteDao.findAllByVersemtypeNotIn(versemtype));
    }

    /**
     * Récupération des rentes correspondant à un rentier passé en paramètre et toujours en cours de versement et classées par la
     * date de prise en compte du début (date de consolidation ).
     * @param rentier Le rentier passé en paramètre et en cours de traitement
     * @return Renvoi une liste classée par date de début d'application décroissante, onn affiche la dernière rente en tête
     */
    public List<Rente> findAllByRentierOrderByDateDesc(Rentier rentier){
        return Lists.newArrayList(renteDao.getAllByRentierAndEtatrenteIsNullOrderByDateconsolidationDesc(rentier));
    };

    /**
     * permet de savoir s'il y a des mouvements de revalorisation assosiés a ce rentier passé en paramètre
     * @param rentier L'objet rentier qui est concerné par la recherche
     * @return Retourne le nombre de rentes attribuées à ce rentier passé en paramètre
     */
    public int countRentiers(Rentier rentier){return renteDao.countAllByRentierEquals(rentier);};



    // TODO  requete SELECT * FROM RENTE ...  à déplacer
    public List<Rente> findAllByEtatrentePresent(Long id){

        // on ne prend pas les forfaits    id_versemtype=3

        String sql="SELECT rente.* FROM rente WHERE ID_VERSEMTYPE IN(1,2,3) " +
                    /* " AND rente.etatrente IS NULL " + */
                   " AND ID IN("+id+")"+
                   /* " AND ID IN(30) "+*/
                   /* " AND ID_RENTIER IN(1,2,6) " +*/
                   " ORDER BY ID_RENTIER,dateco  nsolidation";     //  versemtype = 3  c'est des rentes forfaitaires Pas de revalorisation

        return jdbcTemplate.query( sql ,new RowMapper<Rente>(){
            public Rente mapRow(ResultSet rs, int rowNum) throws SQLException {

                Rente r = new Rente();
                Versemtype versem = new Versemtype();
                Rentier rentier = new Rentier();

                versem.setId(rs.getInt("ID_VERSEMTYPE"));
                r.setVersemtype(versemtypeDao.findOne(versem.getId()));

                rentier.setId(rs.getLong("ID_RENTIER"));

                r.setRentier(rentierDao.findOne(rentier.getId()));
                r.setId(rs.getInt("id"));
                r.setLibel(rs.getString("libel"));
                r.setDateconsolidation(rs.getDate("dateconsolidation"));
                r.setDateaccident(rs.getDate("dateaccident"));
                r.setTxippaycause(rs.getFloat("txippaycause"));
                r.setTxippaydroit(rs.getFloat("txippaydroit"));
                r.setMntrenteinitial(rs.getFloat("mntrenteinitial"));
                r.setEtatrente(rs.getDate("etatrente"));
                return r;
            }
        });
    }


    // TODO  requete SELECT * FROM RENTE ...  à déplacer
    public List<Rente> getAllRentesForRentier(Rentier rentier){

        String sql="SELECT * FROM rente WHERE ID_RENTIER="+rentier.getId()+" AND etatrente IS NULL ORDER BY dateconsolidation DESC";

        return jdbcTemplate.query( sql ,new RowMapper<Rente>(){
            public Rente mapRow(ResultSet rs, int rowNum) throws SQLException {

                Rente r = new Rente();
                Versemtype v = new Versemtype();

                v.setId(rs.getInt("ID_VERSEMTYPE"));
                v = versemtypeDao.getOne(v.getId());

                r.setId(rs.getInt("id"));
                r.setLibel(rs.getString("libel"));
                r.setDateaccident(rs.getDate("dateaccident"));
                r.setDateconsolidation(rs.getDate("dateconsolidation"));
                r.setTxippaycause(rs.getFloat("txippaycause"));
                r.setTxippaydroit(rs.getFloat("txippaydroit"));
                r.setMntrenteinitial(rs.getFloat("mntrenteinitial"));
                r.setEtatrente(rs.getDate("etatrente"));
                r.setVersemtype(v);
                r.setRentier(rentier);

                return r;
            }
        });
    }
}
