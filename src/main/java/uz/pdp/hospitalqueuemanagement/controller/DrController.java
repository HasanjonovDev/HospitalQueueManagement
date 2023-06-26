package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.hospitalqueuemanagement.dto.DrCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.service.dr.DrService;

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
}
