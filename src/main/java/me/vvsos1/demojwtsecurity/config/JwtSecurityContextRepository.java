package me.vvsos1.demojwtsecurity.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import me.vvsos1.demojwtsecurity.vo.UserVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@PropertySource("classpath:application.properties")
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    @Value("${spring.security.jwt.secretKey}")
    private  String secretKey;


    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.fromCallable(()-> {
            ServerHttpResponse res = exchange.getResponse();

            Authentication auth = context.getAuthentication();

            UserVo details = (UserVo) auth.getDetails();

            String jwt = Jwts.builder()
                    .setIssuer("vvsos1")
                    .setSubject(details.getId())
                    .claim("details",details)
                    .setIssuedAt(Date.from(Instant.now()))
                    .setExpiration(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8")), SignatureAlgorithm.HS256)
                    .compact();

            res.getHeaders().add("auth-token", jwt);
            return Mono.empty();
        }).then();
    }


    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        return Mono.fromCallable(()-> {


            ServerHttpRequest req = exchange.getRequest();

            String jwt = req.getHeaders().get("auth-token").get(0);

            Claims claims =
                    Jwts.parserBuilder()
                            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes("UTF-8")))
                            .build().parseClaimsJws(jwt).getBody();



            UserVo details = (UserVo) claims.get("details");

            Authentication auth = new UsernamePasswordAuthenticationToken(details.getId(),details.getPassword(),List.of(new SimpleGrantedAuthority(details.getAuthority())));

            SecurityContext ctx = new SecurityContextImpl(auth);

            return ctx;
        });
    }
}
