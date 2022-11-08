package com.softserve.itacademy.dto;

import lombok.Data;

/***
 * Class contains login request data.
 */

@Data
public class AuthRequestUserDto {

    private String username;/*email*/
    private String password;
}
