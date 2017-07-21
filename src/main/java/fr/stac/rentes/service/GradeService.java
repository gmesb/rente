package fr.stac.rentes.service;

import com.google.common.collect.Lists;
import fr.stac.rentes.dao.GradeDao;
import fr.stac.rentes.domain.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by Perso-ESIEE on 12/04/2017.
 * Pour STAC (Direction Générale de l'Aviation Civile )
 *
 * Author : Antonio RODRIGUES
 *
 */
@Service
public class GradeService {
    private GradeDao gradeDao;

    @Autowired
    public void setGradeDao(GradeDao gradeDao) {
        this.gradeDao = gradeDao;
    }


    /**
     * Supprime l'élément 'grade' de la base de données correspondant au paramètre Objet passé
     * L'annotation @Transactional permet de gérér les transactions directement par Spring
     * @param grade C'est l'objet à supprimer de la base de données
     */
    @Transactional
    public void delete(Grade grade){
        gradeDao.delete(grade);
    }

    /**
     * Sauvegarde Grade dans la base de données, Il y aura insertion dans la table si Grade.id est = 0
     * L'annotation @Transactional permet de gérér les transactions directement par Spring
     * @param grade  C'est l'ojet qui doit être persisté dans la base de données
     */
    @Transactional
    public void save(Grade grade){
        gradeDao.save(grade);
    }

    /**
     * Récupère un élément Grade par son identifiant passé en paramètre
     * @param id C'est l'identifiant de Grade à retrouver
     * @return retorune une mouvement de Grade si l'identifiant existe, null sinon
     */
    public Grade getById(Long id){
        return gradeDao.findOne(id);
    }

    /**
     * Récupère dans une Liste tous les Grades de la base sans conditions de recherche
     * @return Retourne la liste obtenue
     */
    public List<Grade> getAll(){
        return Lists.newArrayList(gradeDao.findAll());
    }

}
