package com.birthae.userservice.user;

import com.birthae.userservice.domain.User;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public User createUser(UserCreateRequestDto userCreateRequestDto) {

        User user = new User();
        user.setEmail(userCreateRequestDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userCreateRequestDto.getPassword()));
        user.setName(userCreateRequestDto.getName());
        user.setNickname(userCreateRequestDto.getNickname());
        user.setBirth(userCreateRequestDto.getBirth());
        user.setPhone_number(userCreateRequestDto.getPhone_number());
        user.setLogin_type("web");
        return userRepository.save(user);
    }

}
