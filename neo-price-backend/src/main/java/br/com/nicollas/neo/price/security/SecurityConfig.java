package br.com.nicollas.neo.price.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marca como classe de configuração do Spring
@EnableWebSecurity // Ativa o módulo de segurança do Spring Security
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Desativa proteção CSRF (comum em APIs REST que não usam sessão)
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Define regras de autorização para as requisições
                .authorizeHttpRequests(auth -> auth
                        // Permite acessar POST /login sem autenticação
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )

                // Habilita configuração de CORS (necessário para front-end acessar API)
                .cors(cors -> {})

                // Adiciona um filtro personalizado antes do filtro padrão de autenticação
                .addFilterBefore(new SecurityFilter(), UsernamePasswordAuthenticationFilter.class);

        // Constrói e retorna a cadeia de filtros de segurança
        return http.build();
    }

}
