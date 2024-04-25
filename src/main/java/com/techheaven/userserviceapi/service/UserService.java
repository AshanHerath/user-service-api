package com.techheaven.userserviceapi.service;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.dto.response.ResponseUserDto;

public interface UserService {
    public void initializeAdmin();
    public ResponseUserDto createSystemUser(RequestUserDto userDto);
    public ResponseUserDto createCustomer(RequestUserDto userDto);
    public void updateUser(String userId, RequestUserDto userDto);
    public boolean verifyUser(String type, String token);
    public void deleteUser(String userId);
    public void toggleAccountStatus(String userId);
    public Object login(String username, String password);
    public ResponseUserDto findUserById(String userId);

}
