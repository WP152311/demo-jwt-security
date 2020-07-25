package me.vvsos1.demojwtsecurity;

import me.vvsos1.demojwtsecurity.service.UserService;
import me.vvsos1.demojwtsecurity.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@SpringBootApplication
@EnableWebFlux
public class DemoJwtSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoJwtSecurityApplication.class, args);



    }

    @Bean
    public ApplicationRunner initData(@Autowired UserService service){
        return args ->
            service.save(new UserVo("vvsos1","1234","park")).subscribe();

    }
}
