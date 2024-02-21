package th.co.scb.onboardingapp.config.security;

// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import th.co.scb.onboardingapp.service.ObaTokenService;
import th.co.scb.onboardingapp.filter.JwtAuthenticationFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${allowed.origins}")
    private List<String> allowedOrigins;

    @Autowired
    private ObaTokenService tokenService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        List<String> permitAllurlPatterns = new ArrayList<String>();

//        if ( !activeProfiles.contains("prod") ) {
//            permitAllurlPatterns.addAll(
//                    Arrays.asList(
//                            "/dev/**"
//                    )
//            );
//        }

        // actuator
        permitAllurlPatterns.addAll(
                Arrays.asList(
                        "/actuator/**"
                )
        );

        // swagger
        permitAllurlPatterns.addAll(
                Arrays.asList(
                        "/swagger-resources/**",
                        "/api-docs/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "**/swagger-ui/**",
                        "/static/**"
                )
        );

        permitAllurlPatterns.addAll(
                Arrays.asList(
                        "/public/**",
                        "/**"
                )
        );

        http.authorizeHttpRequests(rmr -> rmr
//                .requestMatchers("/webapiB").hasRole("Admin")
//                .requestMatchers("/scope/**").hasAuthority("SCOPE_User.Read")
                .requestMatchers(permitAllurlPatterns.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
        ).sessionManagement(smc -> smc
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ).csrf(AbstractHttpConfigurer::disable
        ).cors((cors) -> cors
                .configurationSource(corsConfigurationSource())
        ).addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }

//    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(getOrigin()));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(1800L);
        configuration.setExposedHeaders(Arrays.asList("X-TOKEN"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public String[] getOrigin() {
        int size = allowedOrigins.size();
        String[] originArray = new String[size];
        return allowedOrigins.toArray(originArray);
    }
}
