package me.vvsos1.demojwtsecurity.config;


import me.vvsos1.demojwtsecurity.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain webFilterChain(ServerHttpSecurity http,
                                                 ServerSecurityContextRepository repo,
                                                 ReactiveAuthenticationManager manager) {

        return http
                .cors().disable()
                .csrf().disable()
                .securityContextRepository(repo)
                .authenticationManager(manager)
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ServerSecurityContextRepository repository(){
        return new JwtSecurityContextRepository();
    }

    @Bean
    public ReactiveAuthenticationManager manager(UserService service) {
        return new UserDetailsRepositoryReactiveAuthenticationManager(service);
    }
}
