package com.techheaven.userserviceapi.service;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;

public interface UserService {
    public void initializeAdmin();
    public void signup(RequestUserDto userDto);
    public boolean verifyUser(String type, String token);
}
