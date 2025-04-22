package com.pcrt.Pcrt.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //test
//    @Bean
//    public AuthenticationSuccessHandler myAuthenticationSuccessHandler (){
//        return new ...
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers("/gdv/**").hasAuthority("GDV")
                        .requestMatchers("/ksv/**").hasAuthority("KSV")
                        .requestMatchers("/aml-manager/**").hasAuthority("AML_MANAGER")
                        .requestMatchers("/aml-staff/**").hasAuthority("AML_STAFF")
                        .anyRequest().permitAll());

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        //login process
        httpSecurity.formLogin(login -> login
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error"));

        //logout process
        httpSecurity.logout(logout -> logout.logoutSuccessUrl("/login"));

        httpSecurity.exceptionHandling(exception -> exception.accessDeniedPage("/403"));


        return httpSecurity.build();
    }
}
