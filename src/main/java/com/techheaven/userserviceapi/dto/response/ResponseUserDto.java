package com.techheaven.userserviceapi.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class ResponseUserDto {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
