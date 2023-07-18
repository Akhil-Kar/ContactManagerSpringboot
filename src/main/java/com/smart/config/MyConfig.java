package com.smart.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class MyConfig {

    @Bean
    public UserDetailsService getUserDetailService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailService());
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthenticationProvider;
    }

//    @Autowired
//    public void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.authenticationProvider(this.authenticationProvider());
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll()
            ).formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/user/index")
                .permitAll()
            );
        return http.build();
    }
}


//http.csrf().disable()
//        .authorizeRequests()
//        .antMatchers("/register/**").permitAll()
//        .antMatchers("/index").permitAll()
//        .antMatchers("/users").hasRole("ADMIN")
//        .and()
//        .formLogin(
//        form -> form
//        .loginPage("/login")
//        .loginProcessingUrl("/login")
//        .defaultSuccessUrl("/users")
//        .permitAll()
//        ).logout(
//        logout -> logout
//        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//        .permitAll()
//
//        );