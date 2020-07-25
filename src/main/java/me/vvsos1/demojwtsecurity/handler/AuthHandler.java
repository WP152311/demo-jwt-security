package me.vvsos1.demojwtsecurity.handler;

import me.vvsos1.demojwtsecurity.service.UserService;
import me.vvsos1.demojwtsecurity.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Component
public class AuthHandler {
    @Autowired
    private UserService service;

    @Autowired
    private ReactiveAuthenticationManager manager;

    @Autowired
    private ServerSecurityContextRepository ctxRepo;


    @Bean
    public RouterFunction<ServerResponse> authRouter() {
        return RouterFunctions
                .route()
                .GET("/auth", this::getCurrentUser)
                .POST("/auth", this::login)
                .build();
    }


    public Mono<ServerResponse> login(ServerRequest req) {
        Mono<UserVo> userMono = req.bodyToMono(UserVo.class).cache();

        Mono<Authentication> authMono = userMono
                .map(user -> new UsernamePasswordAuthenticationToken(user.getId(), user.getPassword()))
                .flatMap(manager::authenticate).cache();

        return authMono
                .map(auth -> ctxRepo.save(req.exchange(), new SecurityContextImpl(auth)).subscribe())
                .then(

                        ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userMono, UserVo.class)
                );


    }

    public Mono<ServerResponse> getCurrentUser(ServerRequest req) {
        return req.principal()
                .map(Principal::getName)
                .flatMap(name ->
                        ServerResponse
                                .ok()
                                .contentType(MediaType.TEXT_PLAIN)
                                .body(Mono.just(name), String.class)
                );

    }

}
