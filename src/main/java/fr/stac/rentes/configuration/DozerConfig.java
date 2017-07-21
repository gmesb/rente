package fr.stac.rentes.configuration;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Perso-ESIEE on 03/04/2017.
 * @author RODRIGUES Antonio
 * Direction générale de l'aviation Civile
 */


@Configuration
public class DozerConfig {

    @Bean(name = "org.dozer.Mapper")
    public DozerBeanMapper dozerBean() {
        // List<String> mappingFiles = Arrays.asList(
        // "dozer-global-configuration.xml",
        // "dozer-bean-mappings.xml",
        // "more-dozer-bean-mappings.xml"
        // );

        DozerBeanMapper dozerBean = new DozerBeanMapper();
        // dozerBean.setMappingFiles(mappingFiles);
        return dozerBean;
    }

}
