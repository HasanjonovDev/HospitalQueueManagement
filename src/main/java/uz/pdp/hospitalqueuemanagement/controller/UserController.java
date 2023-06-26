package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.LoginDto;
import uz.pdp.hospitalqueuemanagement.dto.SignUpDto;
import uz.pdp.hospitalqueuemanagement.dto.response.JwtResponse;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.service.authUser.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<UserEntity> signUp(
            @Valid @RequestBody SignUpDto signUpDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(userService.signUp(signUpDto,bindingResult));
    }
    @GetMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginDto loginDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(userService.login(loginDto,bindingResult));
    }
}
