package com.ltp.contacts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private BCryptPasswordEncoder passwordEncoder;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()    //check http request is authorized
            .antMatchers(HttpMethod.DELETE, "/delete/contact/*").hasRole("ADMIN")  // /*repsents and contact  id in the deleteMappin path
            .antMatchers(HttpMethod.POST).hasAnyRole("ADMIN", "USER")
            .antMatchers(HttpMethod.GET).permitAll()
            .anyRequest().authenticated()   //any request needs to be authorized
            .and()
            .httpBasic()    //use basic auth
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //prevent creating sessions

        return http.build();  //build object from the config that gets turned into bean

    }   

    @Bean
    public UserDetailsService users(){
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("admin-pass"))
            .roles("ADMIN")
            .build();

        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder.encode("user-pass"))
            .roles("USER")
            .build();    
        return new InMemoryUserDetailsManager(admin, user);   // details stored in memory
    }
}
    
    

