package uz.pdp.hospitalqueuemanagement.service.dr;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;
import uz.pdp.hospitalqueuemanagement.repository.DrRepository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<DrEntity> getAll(int page,int size){
        Sort sort = Sort.by(Sort.Direction.ASC,"status");
        Pageable pageable = PageRequest.of(page,size,sort);
        return drRepository.findAll(pageable).getContent();
    }

    public DrEntity getDr(String username){
        return drRepository.findDrEntityByUsername(username)
                .orElseThrow(()-> new DataNotFoundException("Doctor Not Found"));
    }

    public DrEntity getDr(UUID id){
        return drRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Doctor Not Found"));
    }

    public HttpStatus updateStatus(UUID drId,DrEntityStatus status){
        checkId(drId);
        drRepository.update(status,drId);
        return HttpStatus.OK;
    }

    public HttpStatus delete(UUID drId){
        checkId(drId);
        drRepository.deleteById(drId);
        return HttpStatus.OK;
    }
    private void checkUsername(String username){
        Optional<DrEntity> byUsername = drRepository.findDrEntityByUsername(username);
        if (byUsername.isPresent()){
            throw new AuthorizationFailedException("Username Already Exists");
        }
    }
    private void checkId(UUID id){
        Optional<DrEntity> byId = drRepository.findById(id);
        if (byId.isEmpty()){
            throw new DataNotFoundException("Doctor Not Found");
        }
    }
}
