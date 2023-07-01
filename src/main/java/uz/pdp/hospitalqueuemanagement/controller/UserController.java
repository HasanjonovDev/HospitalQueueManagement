package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.LoginDto;
import uz.pdp.hospitalqueuemanagement.dto.UserCreateDto;
import uz.pdp.hospitalqueuemanagement.dto.response.JwtResponse;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntityStatus;
import uz.pdp.hospitalqueuemanagement.service.authUser.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserEntity> signUp(
            @Valid @RequestBody UserCreateDto userCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(userService.addUser(userCreateDto,bindingResult, RoleEntity.ROLE_USER));
    }
    @GetMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginDto loginDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(userService.login(loginDto,bindingResult));
    }

    @PostMapping("/addAdmin")
    public ResponseEntity<UserEntity> addAdmin(
            @Valid @RequestBody UserCreateDto userCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(userService.addUser(userCreateDto,bindingResult,RoleEntity.ROLE_ADMIN));
    }

    @GetMapping("/get/all")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<List<UserEntity>> getAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/get/byId")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<UserEntity> getById(
            @RequestParam UUID userId
    ){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @GetMapping("/get/byUsername")
    public ResponseEntity<UserEntity> getByUsername(
            @RequestParam String username
    ){
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PutMapping("/changeStatus")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    public ResponseEntity<HttpStatus> changeStatus(
            @RequestParam UUID userId,
            @RequestParam String status
    ){
        return ResponseEntity.ok(userService.updateStatus(UserEntityStatus.valueOf(status),userId));
    }

    @DeleteMapping("/delete")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(
            @RequestParam UUID userId
    ){
        return ResponseEntity.ok(userService.delete(userId));
    }

}
