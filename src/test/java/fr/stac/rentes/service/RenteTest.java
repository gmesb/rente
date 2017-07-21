package fr.stac.rentes.service;

import fr.stac.rentes.domain.Rente;
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
 * Created by Perso-ESIEE on 15/03/2017.
 */

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
@TestPropertySource(value = "classpath:test.properties")
public class RenteTest {

    private RenteService renteService;
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setRenteService(RenteService renteService) {
        this.renteService = renteService;
    }

    @Test
    public void getAll() throws Exception {

        List<Rente> allRentes = renteService.getAll();

        log.info("Les mouvements de rentes : "+allRentes);
        log.info("Nombre d'élements trouvés : " + allRentes.size());
        assertNotNull("N'est pas null ",allRentes);
        assertTrue("Il n'y a pas exactement + d'un éléments dans la table ", allRentes.size() >= 1);
    }
}
