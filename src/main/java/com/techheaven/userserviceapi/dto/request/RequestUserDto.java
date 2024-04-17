package com.techheaven.userserviceapi.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
