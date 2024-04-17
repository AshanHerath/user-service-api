package com.techheaven.userserviceapi.controller;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.service.UserService;
import com.techheaven.userserviceapi.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/visitor/create")
    public ResponseEntity<StandardResponse> createUser(@RequestBody RequestUserDto requestUserDto){

        userService.signupVisitor(requestUserDto);
        return new ResponseEntity<>(
                new StandardResponse(201,"visitor was saved!", requestUserDto.getEmail()), HttpStatus.CREATED
        );

    }

}
