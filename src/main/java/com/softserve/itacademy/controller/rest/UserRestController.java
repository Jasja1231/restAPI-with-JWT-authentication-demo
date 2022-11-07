package com.softserve.itacademy.controller.rest;

import com.softserve.itacademy.dto.UserDto;
import com.softserve.itacademy.transformer.UserTransformer;
import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserRestController {
        private final UserService userService;
        private final RoleService roleService;
        private final ModelMapper modelMapper;

        private PasswordEncoder passwordEncoder;

        @Autowired
        UserRestController(UserService userService, RoleService roleService,PasswordEncoder passwordEncoder) {
            this.userService = userService;
            this.roleService = roleService;
            this.passwordEncoder = passwordEncoder;
            this.modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setSkipNullEnabled(true);
        }

        /**
         * Get one user:
         * Request: GET /api/users/{id}
         * Response:
         * status 200 OK
         * body: {“id”:1, “first_name”: ”Mike”, “email”: “a@g.com”}
         **/
        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public UserDto getUser(@PathVariable Long id) {
                return UserTransformer.toUserDto(userService.readById(id));
        }


        /**
         * Get list of all users:
         * Request: GET /api/users
         * Response:
         * 	status 200 OK
         * 	body: [{“id”:1, “first_name”: ”Mike”, “email”: “a@g.com”}, {…}, {…}]
         * */
        @GetMapping("")
         public List<UserDto> getAllUsers(){
                return userService.getAll()
                        .stream()
                        .map(UserTransformer::toUserDto)
                        .collect(Collectors.toList());
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public void delete(@PathVariable Long id){
                userService.delete(id);
        }

        @PostMapping("/{id}")
        public void update(@PathVariable("id") long id, @RequestBody UserDto userDto) {
                userDto.setId(id);
                User user = userService.readById(id);
                //Fill in missed data
                modelMapper.map(userDto,user);
                //user = UserTransformer.toUserEntity(userDto);
                userService.update(user);
        }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void  create(@RequestBody UserDto userDto){
            User newUser = new User();
            modelMapper.map(userDto,newUser);
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setRole(roleService.readById(2));//Set new to Role_user
            userService.create(newUser);
    }
}

