package uz.pdp.hospitalqueuemanagement.service.authUser;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.hospitalqueuemanagement.dto.LoginDto;
import uz.pdp.hospitalqueuemanagement.dto.UserCreateDto;
import uz.pdp.hospitalqueuemanagement.dto.response.JwtResponse;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntityStatus;
import uz.pdp.hospitalqueuemanagement.exception.AuthorizationFailedException;
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;
import uz.pdp.hospitalqueuemanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;


    public UserEntity addUser(UserCreateDto userCreateDto, BindingResult bindingResult,RoleEntity role){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
        checkUsername(userCreateDto.getUsername());
        UserEntity user = modelMapper.map(userCreateDto, UserEntity.class);
        switch (role){
            case ROLE_USER ->{
                user.setRoles(List.of(RoleEntity.ROLE_USER));
            }
            case ROLE_ADMIN -> {
                user.setRoles(List.of(RoleEntity.ROLE_ADMIN));
            }
        }
        user.setStatus(UserEntityStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    public JwtResponse login(LoginDto loginDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
        UserEntity user = userRepository.findUserEntityByUsername(loginDto.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));

        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            String token = jwtService.generateAccessToken(user);
            return JwtResponse.builder().accessToken(token).build();
        }
        throw new AuthorizationFailedException("Password Incorrect");
    }
    public void checkUsername(String username){
        Optional<UserEntity> byUsername = userRepository.findUserEntityByUsername(username);
        if (byUsername.isPresent()){
            throw new AuthorizationFailedException("Username Already Exists");
        }
    }
}
