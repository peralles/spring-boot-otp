package com.peralles.authenticator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;

import com.peralles.authenticator.security.jwt.JWTConfigurer;

import com.peralles.authenticator.security.jwt.TokenProvider;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private Http401UnauthorizedEntryPoint authenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder auth;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(authenticationEntryPoint))
                .csrf((csrf) -> csrf.disable())
                .headers(headers -> headers.frameOptions().disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("/", "/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated())
                .apply(securityConfigurerAdapter());

        /**
         * .authorizeRequests(requests -> requests
         * .requestMatchers("/api/**").authenticated()
         * .requestMatchers("/auth/**").permitAll()
         * .requestMatchers("/").permitAll()
         * .and()
         * .apply(securityConfigurerAdapter()));
         */

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/swagger-ui/**",
                "/v3/api-docs/**");
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    /**
     * @Bean
     *       public AuthenticationManager authenticationManager() throws Exception {
     *       this.auth
     *       .userDetailsService(userDetailsService)
     *       .passwordEncoder(passwordEncoder());
     * 
     *       return auth.getOrBuild();
     *       }
     */
}
