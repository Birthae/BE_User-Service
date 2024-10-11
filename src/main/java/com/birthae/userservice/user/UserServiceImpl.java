package com.birthae.userservice.user;

import com.birthae.userservice.domain.User;
import com.birthae.userservice.domain.UserRoleEnum;
import com.birthae.userservice.dto.user.UserCommonDto;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
import com.birthae.userservice.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) throws BadRequestException{

        String email = userCreateRequestDto.getEmail();
        String password = userCreateRequestDto.getPassword();

        if ( existsByEmail(email).isPresent()) {
            throw new BadRequestException("중복된 이메일 입니다.");
        }

        // 비밀번호 검증
        if (!isPasswordValid(password)) {
            throw new BadRequestException("비밀번호: 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.");
        }

        User user = new User();
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setName(userCreateRequestDto.getName());
        user.setNickname(userCreateRequestDto.getNickname());
        user.setBirth(userCreateRequestDto.getBirth());
        user.setPhone_number(userCreateRequestDto.getPhone_number());
        user.setLogin_type("web");
        user.setRole(UserRoleEnum.USER);

        return userRepository.save(user);
    }

    // 비밀번호 유효성 검사
    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*?_])[A-Za-z\\d!@#$%^&*?_]{8,16}$";
        return password.matches(passwordPattern);
    }

    @Override
    public String login(UserCommonDto userCommonDto, HttpServletResponse res) throws BadRequestException {

        Optional<User> user = existsByEmail(userCommonDto.getEmail());

        if (user.isEmpty()) {
            throw new BadRequestException("이메일 또는 비밀번호가 유효하지 않습니다.");
        }

        if (!bCryptPasswordEncoder.matches(userCommonDto.getPassword(), user.get().getPassword())) {
            throw new BadRequestException("이메일 또는 비밀번호가 유효하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.get().getEmail(), user.get().getRole());
        jwtUtil.addJwtToCookie(token, res);

        return token;
    }

    public Optional<User> existsByEmail (String email) {
       return userRepository.findByEmail(email);
    }



}