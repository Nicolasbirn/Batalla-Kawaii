package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

            auth.userDetailsService(userName-> {
                Player player = playerRepository.findByUserName(userName);
                if (player != null)
                    if (player.getUserName().equals("nicolasbirnbach.n@gmail.com")) {
                        return new User(player.getUserName(), player.getPassword(),
                                AuthorityUtils.createAuthorityList("ADMIN"));
                    }else{
                        return new User(player.getUserName(), player.getPassword(),
                                AuthorityUtils.createAuthorityList("USER"));
                    } else {
                    throw new UsernameNotFoundException("Unknown user: " + userName);
                }
            });
    }
}