package com.birthae.userservice.user;

import com.birthae.userservice.domain.User;
import com.birthae.userservice.dto.user.UserCommonDto;
import com.birthae.userservice.dto.user.UserCreateRequestDto;
import org.apache.coyote.BadRequestException;

public interface UserService {

    User createUser(UserCreateRequestDto userCreateRequestDto);

    User login(UserCommonDto userCommonDto) throws BadRequestException;
}
