package com.example.pi.config;

import com.example.pi.filter.JwtAuthFilter;
import com.example.pi.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
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
                        .requestMatchers("/trophies/**").hasAuthority("ROLE_ADMIN")
                        
                        // User endpoints
                        .requestMatchers("/auth/user/**").hasAnyAuthority("ROLE_USER", "ROLE_COACH", "ROLE_NUTRITIONIST")
                        .requestMatchers("/bookings", "/abonnement-requests/**").hasAnyAuthority("ROLE_USER", "ROLE_CLUB_OWNER")
                        .requestMatchers("/recipe/favorites/**").authenticated()
                        
                        // Coach endpoints
                        .requestMatchers("/auth/coach/**").hasAuthority("ROLE_COACH")
                        .requestMatchers("/bookings/*/approve", "/bookings/*/reject").hasAuthority("ROLE_COACH")
                        .requestMatchers("/coach/sessions", "/coach/sessions/range").hasAuthority("ROLE_COACH")
                        
                        // Nutritionist endpoints
                        .requestMatchers("/auth/nutritionist/**").hasAuthority("ROLE_NUTRITIONIST")
                        .requestMatchers("/recipe/**").authenticated()
                        .requestMatchers("/dietprogram/**").authenticated()
                        .requestMatchers("/analytics/**").authenticated()
                        .requestMatchers("/mealplan/**").authenticated()
                        
                        // Club owner endpoints
                        .requestMatchers("/auth/owner/**").hasAuthority("ROLE_OWNER")
                        .requestMatchers("/clubs/**", "/packs/**").hasAuthority("ROLE_CLUB_OWNER")
                        .requestMatchers("/abonnements/**").hasAnyAuthority("ROLE_USER", "ROLE_CLUB_OWNER")
                        
                        // Trophy system
                        .requestMatchers("/trophies/assignTrophy").authenticated()
                        
                        // General authenticated endpoints
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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