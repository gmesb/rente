package fr.stac.rentes.service;

import fr.stac.rentes.dao.UserDao;
import fr.stac.rentes.domain.Profil;
import fr.stac.rentes.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Projet GESTION DES RENTES
 *
 * Créé le  04/04/2017.
 *
 * @author Antonio RODRIGUES
 */

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private UserDao userDao;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setUserDao(UserDao userDao) {       this.userDao = userDao;    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //User user = userDao.findByIdentifIgnoreCase(username);

        // on demande les utilisateurs actifs uniquement
        User user = userDao.findByIdentifIgnoreCaseAndActifEquals(username,true);

        log.info("Recherche utilisateur: "+username);

        if(user == null){
            throw new UsernameNotFoundException("Utilisateur introuvable : |"+username+"|");
        }


        Set<GrantedAuthority> authorities = new HashSet<>();

        // on charge les profils accordés à cet utilisateur dans la liste nommée "authorities"
        for(Profil profil: user.getProfils()){

            log.info("{username: "+user.getId()+" | "+ user.getIdentif()+"| profil : "+profil.getRole());

            authorities.add(new SimpleGrantedAuthority(profil.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getIdentif(),
                user.getMdp(),
                authorities);
    }
}
