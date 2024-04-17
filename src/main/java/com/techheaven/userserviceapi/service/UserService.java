package com.techheaven.userserviceapi.service;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;

public interface UserService {
    public void initializeAdmin();
    public void signupVisitor(RequestUserDto userDto);
    public void updateUser(String userId, RequestUserDto userDto);
    public boolean verifyUser(String type, String token);
    public void deleteUser(String userId);
}
