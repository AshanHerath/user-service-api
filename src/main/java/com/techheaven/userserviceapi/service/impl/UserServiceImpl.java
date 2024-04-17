package com.techheaven.userserviceapi.service.impl;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.service.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public void initializeAdmin() {

    }

    @Override
    public void signup(RequestUserDto userDto) {

    }

    @Override
    public boolean verifyUser(String type, String token) {
        return false;
    }
}
