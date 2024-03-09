package com.kidchang.lingopress.jwt;

import com.kidchang.lingopress._base.constant.Code;
import com.kidchang.lingopress._base.exception.GeneralException;
import com.kidchang.lingopress.jwt.dto.response.JwtResponse;
import com.kidchang.lingopress.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenUtil {

    private static final long ACCESS_TOKEN_EXPIRE_TIME_MS = 1000L * 60 * 30;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME_MS = 1000L * 60 * 60 * 24 * 7;  // 7일
    private final Key secretKey;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(User user, long expireTimeMs) {

        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("nickname", user.getNickname())
            .claim("token_type", "access")
            .claim("role", user.getRole())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String createRefreshToken(User user, long expireTimeMs) {

        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("token_type", "refresh")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public JwtResponse generateJwt(User user) {
        long now = (new Date()).getTime();

        String accessToken = createAccessToken(user, ACCESS_TOKEN_EXPIRE_TIME_MS);
        String refreshToken = createRefreshToken(user, REFRESH_TOKEN_EXPIRE_TIME_MS);

        return JwtResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME_MS)
            .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = extractClaims(accessToken);

        if (claims.get("role") == null) {
            throw new GeneralException(Code.UNAUTHORIZED);
        }

        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("role").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();

        UserDetails principal = new org.springframework.security.core.userdetails.User(
            claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String getUserId(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token);

            if (!claims.getBody().get("token_type").equals("access")) {
                throw new JwtException(Code.NOT_ACCESS_TOKEN.getMessage());
            }

            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new JwtException(Code.EXPIRED_TOKEN.getMessage());
        } catch (SignatureException e) {
            throw new JwtException(Code.NOT_SIGNATURE_TOKEN.getMessage());
        } catch (MalformedJwtException e) {
            throw new JwtException(Code.MALFORMED_TOKEN.getMessage());
        } catch (JwtException e) {
            log.info("JwtException: {}", e.getMessage());
            throw new JwtException(e.getMessage());
        }
    }

    public boolean ValidateRefreshToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token);
            if (!claims.getBody().get("token_type").equals("refresh")) {
                throw new JwtException(Code.NOT_REFRESH_TOKEN.getMessage());
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new GeneralException(Code.EXPIRED_REFRESH_TOKEN);
        } catch (SignatureException e) {
            throw new GeneralException(Code.NOT_SIGNATURE_TOKEN);
        } catch (MalformedJwtException e) {
            throw new GeneralException(Code.MALFORMED_TOKEN);
        }


    }

    private Claims extractClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
