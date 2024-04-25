package com.techheaven.userserviceapi.controller;

import com.techheaven.userserviceapi.dto.request.RequestUserDto;
import com.techheaven.userserviceapi.service.UserService;
import com.techheaven.userserviceapi.util.StandardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/customer/create")
    public ResponseEntity<StandardResponse> createCustomer(@RequestBody RequestUserDto requestUserDto){

        return new ResponseEntity<>(
                new StandardResponse(201,"Visitor was saved!", userService.createCustomer(requestUserDto)), HttpStatus.CREATED
        );

    }

    @PostMapping("/create")
    public ResponseEntity<StandardResponse> createSystemUser(@RequestBody RequestUserDto requestUserDto){

        return new ResponseEntity<>(
                new StandardResponse(201,"User was saved!", userService.createSystemUser(requestUserDto)), HttpStatus.CREATED
        );

    }

    @PutMapping(params = "id", path = "/status")
    public ResponseEntity<StandardResponse> toggleAccountStatus(@RequestParam String id){
        userService.toggleAccountStatus(id);
        return new ResponseEntity<>(
                new StandardResponse(201,"User status was updated!", id), HttpStatus.CREATED
        );

    }

    @PutMapping(params = "id")
    public ResponseEntity<StandardResponse> updateUser(@RequestParam String id, @RequestBody  RequestUserDto requestUserDto){

        userService.updateUser(id, requestUserDto);
        return new ResponseEntity<>(
                new StandardResponse(201,"User was updated!", requestUserDto.getEmail()), HttpStatus.CREATED
        );

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StandardResponse> deleteUser(@PathVariable String id){

        userService.deleteUser(id);
        return new ResponseEntity<>(
                new StandardResponse(204,"User was deleted!", id), HttpStatus.NO_CONTENT
        );

    }

    @PostMapping("/login")
    public Object login(@RequestParam String username, @RequestParam String password){
        return userService.login(username, password);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandardResponse> findUser(@PathVariable String id){
        return new ResponseEntity<>(
                new StandardResponse(200,"Id no."+id+" User data found!", userService.findUserById(id)), HttpStatus.OK
        );
    }

}
