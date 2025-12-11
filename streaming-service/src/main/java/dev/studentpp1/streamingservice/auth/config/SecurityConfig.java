package dev.studentpp1.streamingservice.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private static final String[] WHITELIST = {
        "/api/auth/login",
        "/api/auth/register",
        "/api/subscriptions",
        "/api/subscriptions/{id}",
        "/api/subscription-plans",
        "/api/subscription-plans/{id}",
        "/api/subscription-plans/{id}/movies",
        "/api/movie/{movie_id}",
        "/api/movie/actor/{actor_id}",
        "/api/movie/director/{director_id}",
        "/api/payments/webhook"
    };

    private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-resources/**",
        "/swagger-resources",
        "/swagger-ui.html"
    };

    // one AuthenticationManager for one filter chain
    // contains list of auth providers (email+password, jwt, etc...)
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(config -> config
                .requestMatchers(WHITELIST).permitAll()
                .requestMatchers(SWAGGER_WHITELIST).permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
