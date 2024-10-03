package com.birthae.userservice.user;

import com.birthae.userservice.domain.User;
import com.birthae.userservice.dto.user.UserCreateRequestDto;

public interface UserService {

    User createUser(UserCreateRequestDto userCreateRequestDto);
}
