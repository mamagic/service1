package com.gopang.oauth2server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gopang.oauth2server.filter.JsonAuthenticationFilter;
import com.gopang.oauth2server.security.JpaUserDetailsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gopang
 */

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;

    public DefaultSecurityConfig(PasswordEncoder passwordEncoder,
                                 JpaUserDetailsManager jpaUserDetailsManager,
                                 ObjectMapper objectMapper,
                                 @Lazy AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.jpaUserDetailsManager = jpaUserDetailsManager;
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/", "/error", "/join/**", "/oauth2/login/**").permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/connect/logout")).permitAll()
                                .anyRequest().permitAll())

                .formLogin(formLogin-> formLogin.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(new JsonAuthenticationFilter(objectMapper, authenticationManager), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(jpaDaoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider jpaDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsManager);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "OPTIONS", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
