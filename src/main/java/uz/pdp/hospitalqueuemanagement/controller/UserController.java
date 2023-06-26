package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.LoginDto;
import uz.pdp.hospitalqueuemanagement.dto.UserCreateDto;
import uz.pdp.hospitalqueuemanagement.dto.response.JwtResponse;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.service.authUser.UserService;

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
}
