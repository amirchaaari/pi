package com.example.pi.config;

import com.example.pi.filter.JwtAuthFilter;
import com.example.pi.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService(); // Ensure UserInfoService implements UserDetailsService
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(
                                "/auth/welcome",
                                "/auth/addNewUser",
                                "/auth/generateToken",
                                "/auth/deleteUser/{id}",
                                "/ws/**",
                                "/pi/ws/**",
                                "/pi/ws/websocket",
                                "/training-sessions/recommended",
                                "/auth/logout",
                                "/training-sessions/chat",
                                "/training-sessions/chat/**",
                                "/training-sessions/users",
                                "/training-sessions/messages/**",
                                "/auth/**"
                        ).permitAll()

                        // Admin endpoints
                        .requestMatchers("/auth/admin/**", "/clubs/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/clubs/admin/pending-requests").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/clubs/admin/{clubId}/performance").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/trophies/add-trophy","trophies/update-trophy/{id}","trophies/update-trophy/{id}").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/images/**").permitAll()

                        // User endpoints
                        .requestMatchers("/auth/user/**").hasAnyAuthority("ROLE_USER", "ROLE_COACH", "ROLE_NUTRITIONIST")
                        .requestMatchers("/bookings", "/abonnement-requests/**").hasAnyAuthority("ROLE_USER", "ROLE_OWNER")
                        .requestMatchers("/recipe/favorites/**").authenticated()

                        // Coach endpoints
                        .requestMatchers("/auth/coach/**").hasAuthority("ROLE_COACH")
                        .requestMatchers("/bookings/*/approve", "/bookings/*/reject").hasAuthority("ROLE_COACH")
                        .requestMatchers("/coach/sessions", "/coach/sessions/range").hasAuthority("ROLE_COACH")

                        // Nutritionist endpoints
                        .requestMatchers("/auth/nutritionist/**").hasAuthority("ROLE_NUTRITIONIST")
                        .requestMatchers("/recipe/favorites/**").authenticated()
                        .requestMatchers("/recipe/**").authenticated()
                        .requestMatchers("/dietprogram/**").authenticated()
                        .requestMatchers("/analytics/**").authenticated()
                        .requestMatchers("/mealplan/**").authenticated()

                        // Club owner endpoints
                        .requestMatchers("/images/**").permitAll()


                        .requestMatchers("/api/payments/health").permitAll()
                                .requestMatchers("/api/payments/create-session").permitAll()
                                .requestMatchers("/trigger-notification").permitAll()

                                // Category endpoints
                                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/categories/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/categories/**").permitAll()

                                // Product endpoints
                                .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/products").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()
                                .requestMatchers("/api/products/test-notification").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/Livraison").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/Livraison/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/Livraison").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/Livraison").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/Livraison").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/livreurs/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/livreurs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/livreurs").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/livreurs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/Maps/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/Maps/**").permitAll()




                                // WebSocket
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/topic/**").permitAll()
                                // Club owner endpoints
                                .requestMatchers("/auth/owner/**").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/clubs/retrieve-all-clubs").hasAuthority("ROLE_USER")
                                .requestMatchers("/clubs/**", "/packs/**").hasAnyAuthority("ROLE_OWNER","ROLE_ADMIN")

                                .requestMatchers("/abonnements/**").hasAnyAuthority("ROLE_USER", "ROLE_OWNER","ROLE_ADMIN")
                                .requestMatchers("/abonnement-requests/request/{packId}").hasAuthority("ROLE_USER")

                                // Trophy system
                                .requestMatchers("/trophies/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                                .requestMatchers("/trophies/assignTrophy").authenticated()

                        //products
                        // Public endpoints
                        .requestMatchers("/api/payments/health").permitAll()
                        .requestMatchers("/api/payments/create-session").permitAll()
                        .requestMatchers("/trigger-notification").permitAll()

                        // Category endpoints
                        .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").permitAll()

                        // Product endpoints
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").permitAll()
                        .requestMatchers("/api/products/test-notification").permitAll()

                        // Command endpoints
                        .requestMatchers(HttpMethod.GET, "/api/commands").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/commands/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/commands").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/commands/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/commands/**").permitAll()
                        .requestMatchers("/api/commands/user").permitAll()

                        // WebSocket
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/topic/**").permitAll()



                        // General authenticated endpoints
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .authenticationProvider(authenticationProvider()) // Custom authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(false);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}