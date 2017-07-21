package fr.stac.rentes.service;

import fr.stac.rentes.dao.RenterevaloriseeDao;
import fr.stac.rentes.domain.Renterevalorisee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Antonio RODRIGUES on 15/04/2017.
 */

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
public class RenterevaloriseeTest {

    private RenterevaloriseeService renterevaloriseeService;

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setRenterevaloriseeDao(RenterevaloriseeDao renterevaloriseeDao) {
    }
    @Autowired
    public void setRenterevaloriseeService(RenterevaloriseeService renterevaloriseeService) {
        this.renterevaloriseeService = renterevaloriseeService;
    }

    @Test
    public void getAll() throws Exception {

       List<Renterevalorisee> allRentesrevalorisees = renterevaloriseeService.getAll();

       log.info("Les mouvements de rentes revalorisées : "+allRentesrevalorisees);
       log.info("Nombre d'élements trouvés : " + allRentesrevalorisees.size());
       assertNotNull("N'est pas null ",allRentesrevalorisees);
       assertTrue("Il n'y a pas exactement + d'un éléments dans la table ", allRentesrevalorisees.size() >= 1);

    }
}
