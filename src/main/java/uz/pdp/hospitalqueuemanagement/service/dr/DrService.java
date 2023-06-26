package uz.pdp.hospitalqueuemanagement.service.dr;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.hospitalqueuemanagement.dto.DrCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityStatus;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityType;
import uz.pdp.hospitalqueuemanagement.exception.AuthorizationFailedException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;
import uz.pdp.hospitalqueuemanagement.repository.DrRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrService {
    private final DrRepository drRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public DrEntity addDoctor(DrCreateDto drCreateDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }

        checkUsername(drCreateDto.getUsername());
        DrEntity drEntity = modelMapper.map(drCreateDto, DrEntity.class);
        switch (DrEntityType.valueOf(drCreateDto.getType())){
            case Urologist -> {
                drEntity.setType(DrEntityType.Urologist);
            }
            case Oncologist -> {
                drEntity.setType(DrEntityType.Oncologist);
            }
            case Neurologist -> {
                drEntity.setType(DrEntityType.Neurologist);
            }
            case Psychiatrist -> {
                drEntity.setType(DrEntityType.Psychiatrist);
            }
            case Orthopaedist -> {
                drEntity.setType(DrEntityType.Orthopaedist);
            }
        }
        drEntity.setRoles(List.of(RoleEntity.ROLE_DOCTOR));
        drEntity.setStatus(DrEntityStatus.ACTIVE);
        drEntity.setPassword(passwordEncoder.encode(drEntity.getPassword()));
        return drRepository.save(drEntity);
    }
    public void checkUsername(String username){
        Optional<DrEntity> byUsername = drRepository.findDrEntityByUsername(username);
        if (byUsername.isPresent()){
            throw new AuthorizationFailedException("Username Already Exists");
        }
    }
}
