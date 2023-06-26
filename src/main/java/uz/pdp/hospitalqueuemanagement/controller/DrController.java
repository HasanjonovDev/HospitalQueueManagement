package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.DrCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityStatus;
import uz.pdp.hospitalqueuemanagement.service.dr.DrService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dr")
public class DrController {
    private final DrService drService;
    @PostMapping("/addDoctor")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<DrEntity> addDoctor(
            @Valid @RequestBody DrCreateDto drCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(drService.addDoctor(drCreateDto,bindingResult));
    }

    @GetMapping("/get/byUsername")
    public ResponseEntity<DrEntity> getByUsername(
            @RequestParam String username
    ){
        return ResponseEntity.ok(drService.getDr(username));
    }

    @GetMapping("/get/byId")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<DrEntity> getById(
            @RequestParam UUID drId
    ){
        return ResponseEntity.ok(drService.getDr(drId));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<DrEntity>> getAll(
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size
    ){
        return ResponseEntity.ok(drService.getAll(page,size));
    }

    @PutMapping("/changeStatus")
    @PreAuthorize(value = "hasAnyRole('DOCTOR','ADMIN')")
    public ResponseEntity<HttpStatus> changeStatus(
            @RequestParam UUID drId,
            @RequestParam String status
    ){
        return ResponseEntity.ok(drService.updateStatus(drId, DrEntityStatus.valueOf(status)));
    }

    @DeleteMapping("/delete")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(
            @RequestParam UUID drId
    ){
        return ResponseEntity.ok(drService.delete(drId));
    }
}
