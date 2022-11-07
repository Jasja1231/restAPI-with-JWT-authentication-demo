package com.softserve.itacademy.dto;

import com.softserve.itacademy.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserTransformer {
    public static User toUserEntity(UserDto userDto){
        User user  = new User();
        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setPassword(userDto.getPassword()); //TODO password

        return user;
    }


    public static UserDto toUserDto(User user){
        return new UserDto(
                 user.getId()
                ,user.getFirstName()
                ,user.getLastName()
                ,user.getEmail()
                ,user.getRole()
                ,user.getPassword()
        );
    }


}
