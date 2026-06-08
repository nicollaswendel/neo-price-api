package br.com.nicollas.neo.price.security;

import br.com.nicollas.neo.price.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 12 * 60 * 60 * 1000; // 12 horas em milissegundos
    private static final String SECRET_KEY = "NpIP23@psGSd04tj890tp#5qsLcUgt68";
    private static final String ISSUER = "DevNic";

    // Gera a chave no formato esperado pelo JJWT
    private static Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public static String createToken(User user) {
        return PREFIX + Jwts.builder()
                .setSubject(user.getEmail()) // identifica o usuário no token
                .claim("userId", user.getUserId())
                .setIssuer(ISSUER) // quem gerou o token
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // data de expiração
                .signWith(getSecretKey(), SignatureAlgorithm.HS256) // assina o token
                .compact();
    }

    private static boolean isExpirationValid(Date expiration) {
        return expiration != null && expiration.after(new Date());
    }

    private static boolean isIssuerValid(String issuer) {
        return ISSUER.equals(issuer); // evita NullPointerException
    }

    private static boolean isSubjectValid(String username) {
        return username != null && !username.isBlank();
    }

    public static Authentication validate(HttpServletRequest request) {

        String token = request.getHeader(HEADER);

        // Verifica se o header existe e começa com "Bearer "
        if (token == null || !token.startsWith(PREFIX)) {
            return null;
        }

        // Remove o prefixo "Bearer "
        token = token.replace(PREFIX, "");

        try {
            // Faz o parse e valida a assinatura do token
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);

            String username = jwsClaims.getBody().getSubject();
            String issuer = jwsClaims.getBody().getIssuer();
            Date expiration = jwsClaims.getBody().getExpiration();

            // Se os dados do token forem válidos, cria uma autenticação para o Spring Security
            if (isSubjectValid(username)
                    && isIssuerValid(issuer)
                    && isExpirationValid(expiration)) {

                return new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );
            }

        } catch (Exception e) {
            // Token inválido, expirado, malformado ou assinatura incorreta
            return null;
        }

        return null;
    }
}
