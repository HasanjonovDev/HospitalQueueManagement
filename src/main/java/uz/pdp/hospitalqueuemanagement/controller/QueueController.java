package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.QueueCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.service.queue.QueueService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final QueueService queueService;
    @PostMapping("/add")
    public ResponseEntity<QueueEntity> addQueue(
            @Valid @RequestBody QueueCreateDto queueCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(queueService.addQueue(queueCreateDto,bindingResult));
    }
}
