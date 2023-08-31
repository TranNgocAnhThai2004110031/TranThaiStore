package com.tranthai.tranthaistore.configuration;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tranthai.tranthaistore.controller.CartController;
import com.tranthai.tranthaistore.service.UserService;
import com.tranthai.tranthaistore.utils.UserUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/register**", "/", "/shop/**", "/shop**", "/cart/**") 
                .permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN") 
                .antMatchers("/users/**").hasRole("USER") 
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403") 
                .and()
                .formLogin()
                .loginPage("/login") 
                .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())                                                               
                .defaultSuccessUrl("/") 
                .permitAll() 
                .and()
                .logout()
                .invalidateHttpSession(true) 
                .clearAuthentication(true) 
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
                .logoutSuccessHandler(logoutSuccessHandler()) 
                .permitAll();

        return http.build(); 
    }

    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            CartController.clearCart();
            response.sendRedirect("/login");
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
   
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**",
                "/error");
    }

}