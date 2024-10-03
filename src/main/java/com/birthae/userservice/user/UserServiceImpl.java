package com.birthae.userservice.user;

import com.birthae.userservice.domain.User;
import com.birthae.userservice.dto.user.UserCommonDto;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
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

    @Override
    public User login(UserCommonDto userCommonDto) throws BadRequestException {

        Optional<User> user = userRepository.findByEmail(userCommonDto.getEmail()) ;

        if ( user.isEmpty() ) {
            throw new BadRequestException("Invalid email or password");
        }

        if( user.get().getPassword().equals(userCommonDto.getPassword())){
            System.out.println("로그인 성공!");
            return user.get();
        }

        throw new BadRequestException("Invalid email or password");

    }

}
