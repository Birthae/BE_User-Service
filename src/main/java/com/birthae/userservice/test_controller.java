package com.birthae.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class test_controller {

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome!";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("user-request") String header){
        log.info(header);
        return "Hello World  in User-Service";
    }

}
