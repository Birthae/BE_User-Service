package com.birthae.userservice.user;

import com.birthae.userservice.Greeting;
import com.birthae.userservice.domain.User;
import com.birthae.userservice.domain.UserRoleEnum;
import com.birthae.userservice.dto.ResponseMessage;
import com.birthae.userservice.dto.user.UserCommonDto;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
import com.birthae.userservice.security.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    private final JwtUtil jwtUtil;

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

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody UserCreateRequestDto userCreateRequestDto) throws BadRequestException {

        System.out.println("userCreateRequestDto : " + userCreateRequestDto);
        User createdUser = userService.createUser(userCreateRequestDto);

        ResponseMessage response = ResponseMessage.builder()
                .data(createdUser)
                .statusCode(201)
                .resultMessage("User created successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody UserCommonDto userCommonDto, HttpServletResponse res) throws BadRequestException {

        String token = userService.login(userCommonDto, res);

        ResponseMessage response = ResponseMessage.builder()
                .data(token)
                .statusCode(201)
                .resultMessage("Login successfully")
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/create-jwt")
    public String createJwt(HttpServletResponse res) {
        // Jwt 생성
        String token = jwtUtil.createToken(1, UserRoleEnum.USER);

        // Jwt 쿠키 저장
        jwtUtil.addJwtToCookie(token, res);

        return "createJwt : " + token;
    }

    @GetMapping("/get-jwt")
    public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
        // JWT 토큰 substring
        String token = jwtUtil.substringToken(tokenValue);

        // 토큰 검증
        if(!jwtUtil.validateToken(token)){
            throw new IllegalArgumentException("Token Error");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims info = jwtUtil.getUserInfoFromToken(token);
        // 사용자 username
        String username = info.getSubject();
        System.out.println("username = " + username);
        // 사용자 권한
        String authority = (String) info.get(JwtUtil.AUTHORIZATION_KEY);
        System.out.println("authority = " + authority);

        return "getJwt : " + username + ", " + authority;
    }
}
