package com.birthae.userservice.dto.user;

import lombok.Data;

import java.sql.Date;

@Data
public class UserCreateRequestDto extends UserCommonDto {

    private String name;
    private String nickname;
    private Date birth;
    private String phone_number;

}
