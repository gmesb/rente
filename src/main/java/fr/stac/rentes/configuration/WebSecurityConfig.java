package fr.stac.rentes.configuration;

import fr.stac.rentes.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Projet Gestion des rentes
 * STAC (Direction Generale de l'Aviation Civile )
 * Direction générale de l'aviation Civile
 * Cree le  03/04/2017.
 *
 * @author RODRIGUES Antonio
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JpaUserDetailsService jpaUserDetailsService  ;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login*","/", "/home","/css/**","/images/**","/resources/**")
                    .permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/login/menu",true)
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
                    .and()
                .sessionManagement()
                    .maximumSessions(3)                     //   nombre max d'users
                    .sessionRegistry(sessionRegistry())
                    .maxSessionsPreventsLogin(false)       //(If true, prevents a user from authenticating when the SessionManagementConfigurer.maximumSessions(int) has been reached.
                    .expiredUrl("/login");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("recupstac")
                .password("recupstac")
                .roles("ADMIN");
        auth.userDetailsService(jpaUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
