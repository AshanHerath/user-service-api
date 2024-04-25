package com.techheaven.userserviceapi.util.mapper;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.dto.response.ResponseUserDto;
import com.techheaven.userserviceapi.entity.User;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    ResponseUserDto userRepresentationToDto (UserRepresentation userRepresent);

    User requestUserDtoToUser (RequestUserDto requestUserDto);

    default ResponseUserDto userRepresentationToDtoWithRole(UserRepresentation userRepresentation, String userRole) {
        ResponseUserDto responseUserDto = userRepresentationToDto(userRepresentation);
        responseUserDto.setUserRole(userRole);
        return responseUserDto;
    }

}
