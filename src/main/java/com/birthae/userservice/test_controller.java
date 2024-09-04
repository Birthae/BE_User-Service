package com.birthae.userservice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class test_controller {

    Environment env;

    @Autowired
    public test_controller(Environment env){
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome!";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("user-request") String header){
        log.info(header);
        return "Hello World  in User-Service";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port={}", request.getServerPort());
        return String.format("Hi, there. This is a message from User-service on PORT %s", env.getProperty("local.server.port"));
    }

}
