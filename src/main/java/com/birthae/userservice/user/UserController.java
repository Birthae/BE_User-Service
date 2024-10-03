package com.birthae.userservice.user;

import com.birthae.userservice.Greeting;
import com.birthae.userservice.domain.User;
import com.birthae.userservice.dto.ResponseMessage;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final Environment env; // application.yml 파일에 등록된 정보 가져오는 첫번째 방법 Environment 객체 사용
    private final Greeting greeting; // application.yml 파일에 등록된 정보 가져오는 두번째 방법, Value 어노테이션 사용
    private final UserService userService;

    @GetMapping("/welcome")
    public String welcome(){
//        return "welcome!";
//        return env.getProperty("greeting.message"); // 첫번째 방법
        return greeting.getMessage();
    }

    @GetMapping("/message")
    public String message(@RequestHeader("user-request") String header){
        log.info(header);
        return "Hello World  in User-Service";
    }

    @GetMapping("/health_check")
    public String check(HttpServletRequest request){
        log.info("Server port={}", request.getServerPort());
        return String.format("Hi, there. This is a message from User-service on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto){

        User createdUser = userService.createUser(userCreateRequestDto);

        ResponseMessage response = ResponseMessage.builder()
                .data(createdUser)
                .statusCode(201)
                .resultMessage("User created successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }


}
