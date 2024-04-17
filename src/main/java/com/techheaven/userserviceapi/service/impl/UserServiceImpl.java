package com.techheaven.userserviceapi.service.impl;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.entity.User;
import com.techheaven.userserviceapi.service.UserService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Override
    public void initializeAdmin() {

    }

    @Override
    public void signupVisitor(RequestUserDto userDto) {

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(userDto.getPassword())
                .build();

        UserRepresentation userRep = mapUser(user);

        Keycloak keycloak = getKeycloakInstance();
        Response response = keycloak.realm(realm).users().create(userRep);

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()){
            RoleRepresentation userRole = keycloak.realm(realm).roles().get("user").toRepresentation();
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            keycloak.realm(realm).users().get(userId).roles().realmLevel().add(Arrays.asList(userRole));
        }

    }

    @Override
    public void updateUser(String userId, RequestUserDto userDto) {

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

    }
}
