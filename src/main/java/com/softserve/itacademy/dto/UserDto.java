package com.softserve.itacademy.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.itacademy.model.Role;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data //@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
public class UserDto {

    public UserDto(long id, String firstName, String lastName, String email, Role role, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    @NotNull
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Role role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public Role getRole() {
        return role;
    }
}
