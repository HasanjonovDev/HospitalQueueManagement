package uz.pdp.hospitalqueuemanagement.service.authUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.access.expiry}")
    private long accessTokenExpiry;
    public String generateAccessToken(UserEntity userEntity) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512,secretKey)
                .setSubject(userEntity.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+accessTokenExpiry))
                .addClaims(Map.of("roles", getRoles(userEntity.getAuthorities())))
                .compact();
    }
    private List<String> getRoles(Collection<? extends GrantedAuthority> roles) {
        return roles.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
    public Jws<Claims> extractToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
    }
}
