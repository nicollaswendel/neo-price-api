package br.com.nicollas.neo.price.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro de segurança personalizado que será executado em cada requisição
public class SecurityFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        // Só tenta validar se realmente existir token no header
        if (authorization != null && !authorization.isBlank()) {
            Authentication auth = TokenUtil.validate(request);

            // Só salva no contexto se a autenticação for válida
            if (auth != null) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continua o fluxo da requisição para os próximos filtros
        filterChain.doFilter(request, response);

    }
}
