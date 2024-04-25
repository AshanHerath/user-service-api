package com.techheaven.userserviceapi.service.impl;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.dto.response.ResponseUserDto;
import com.techheaven.userserviceapi.entity.User;
import com.techheaven.userserviceapi.exception.EntryAlreadyExistsException;
import com.techheaven.userserviceapi.exception.EntryNotFoundException;
import com.techheaven.userserviceapi.service.UserService;
import com.techheaven.userserviceapi.util.mapper.UserMapper;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-token}")
    private String clientToken;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void initializeAdmin() {

        Keycloak keycloak = getKeycloakInstance();

        try {
            List<UserRepresentation> users = keycloak.realm(realm).users().list();

            if (users.size() == 1){

                User user = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .firstName("admin")
                        .lastName("admin")
                        .password("1234")
                        .build();

                UserRepresentation userRep = mapUser(user);

                Response response = keycloak.realm(realm).users().create(userRep);

                if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
                    String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                    RoleRepresentation userRole = keycloak.realm(realm).roles().get("admin").toRepresentation();
                    keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRole));
                }

            }


        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user role not found");
        }
    }

    @Override
    public ResponseUserDto createSystemUser(RequestUserDto userDto) {

        try {
            Keycloak keycloak = getKeycloakInstance();
            List<UserRepresentation> users = keycloak.realm(realm).users().search(userDto.getEmail(), null, null, true);

            if (users.isEmpty()) {

                User user = userMapper.requestUserDtoToUser(userDto);

                UserRepresentation userRep = mapUser(user);


                Response response = keycloak.realm(realm).users().create(userRep);

                if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
                    String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                    RoleRepresentation userRole = keycloak.realm(realm).roles().get(userDto.getUserRole()).toRepresentation();
                    keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRole));

                    UserRepresentation createdUser = keycloak.realm(realm).users().get(userId).toRepresentation();

                    ResponseUserDto responseUserDto = userMapper.userRepresentationToDto(createdUser);

                    responseUserDto.setId(userId);

                    return responseUserDto;
                }

            } else {
                throw new EntryAlreadyExistsException("User email already exists.");
            }

            return null;

        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user role not found");
        }

    }

    @Override
    public ResponseUserDto createCustomer(RequestUserDto userDto) {

        try {
            Keycloak keycloak = getKeycloakInstance();
            List<UserRepresentation> users = keycloak.realm(realm).users().search(userDto.getEmail(), null, null, true);

            if (users.isEmpty()) {
                User user = userMapper.requestUserDtoToUser(userDto);
                UserRepresentation userRep = mapUser(user);
                Response response = keycloak.realm(realm).users().create(userRep);

                if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
                    String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
                    RoleRepresentation userRole = keycloak.realm(realm).roles().get("customer").toRepresentation();
                    keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRole));

                    UserRepresentation createdUser = keycloak.realm(realm).users().get(userId).toRepresentation();

                    ResponseUserDto responseUserDto = userMapper.userRepresentationToDto(createdUser);

                    responseUserDto.setId(userId);

                    return responseUserDto;
                }
            } else {
                throw new EntryAlreadyExistsException("User email already exists.");
            }

            return null;

        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user role not found");
        }
    }

    @Override
    public void updateUser(String userId, RequestUserDto userDto) {

        Keycloak keycloak = getKeycloakInstance();

        try {
            UserRepresentation selectedUser = keycloak.realm(realm).users().get(userId).toRepresentation();

            if (selectedUser != null){
                User updatedUser = User.builder()
                        .username(userDto.getUsername())
                        .email(userDto.getEmail())
                        .firstName(userDto.getFirstName())
                        .lastName(userDto.getLastName())
                        .password(userDto.getPassword())
                        .build();
                UserRepresentation updatedUserRep = mapUser(updatedUser);

                keycloak.realm(realm).users().get(userId).update(updatedUserRep);
            }
        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user not found");
        }



    }

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username("host")
                .password("1234")
                .clientSecret(clientSecret)
                .clientId(clientId)
                .build();
    }

    private UserRepresentation mapUser(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setEmail(user.getEmail());

        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);

        List<CredentialRepresentation> creds = new ArrayList<>();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(user.getPassword());
        creds.add(credentialRepresentation);
        userRepresentation.setCredentials(creds);
        return  userRepresentation;

    }


    @Override
    public boolean verifyUser(String type, String token) {



        return false;
    }

    @Override
    public void deleteUser(String userId) {
        Keycloak keycloak = getKeycloakInstance();

        try {

            UserRepresentation existingUser = keycloak.realm(realm).users().get(userId).toRepresentation();
            if (existingUser != null) {
                keycloak.realm(realm).users().get(userId).remove();
            }

        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user not found");
        }
    }

    @Override
    public void toggleAccountStatus(String userId) {
        Keycloak keycloak = getKeycloakInstance();

        try {
            UserRepresentation existingUser = keycloak.realm(realm).users().get(userId).toRepresentation();
            if (existingUser != null) {
                boolean isActive = existingUser.isEnabled();
                existingUser.setEnabled(!isActive);
                keycloak.realm(realm).users().get(userId).update(existingUser);
            }

        } catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user not found");
        }
    }

    @Override
    public Object login(String username, String password) {

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id",clientId);
        requestBody.add("grant_type", OAuth2Constants.PASSWORD);
        requestBody.add("username",username);
        requestBody.add("client_secret",clientSecret);
        requestBody.add("password",password);

        HttpHeaders headers =  new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> response = restTemplate.postForEntity(clientToken, requestBody, Object.class);
        return response.getBody();

    }

    @Override
    public ResponseUserDto findUserById(String userId) {

        Keycloak keycloak = getKeycloakInstance();

        try {

            UserRepresentation userRepresentation = keycloak.realm(realm).users().get(userId).toRepresentation();

            List<RoleRepresentation> userRoles = keycloak.realm(realm).users().get(userId).roles().realmLevel().listEffective();
            String userRole = userRoles.stream().map(RoleRepresentation::getName).findFirst().orElse(null);

            return userMapper.userRepresentationToDtoWithRole(userRepresentation, userRole);

        }catch (NotFoundException e) {
            throw new EntryNotFoundException("Selected user not found");
        }
    }

}
