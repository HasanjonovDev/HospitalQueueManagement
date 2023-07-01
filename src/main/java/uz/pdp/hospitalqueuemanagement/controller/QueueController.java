package uz.pdp.hospitalqueuemanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.dto.queue.QueueCreateDto;
import uz.pdp.hospitalqueuemanagement.dto.queue.QueueTransferDto;
import uz.pdp.hospitalqueuemanagement.dto.queue.UpdateQueueDto;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntityStatus;
import uz.pdp.hospitalqueuemanagement.service.queue.QueueService;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final QueueService queueService;
    @PostMapping("/add")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USER')")
    public ResponseEntity<QueueEntity> addQueue(
            @Valid @RequestBody QueueCreateDto queueCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(queueService.addQueue(queueCreateDto,bindingResult));
    }

    @PutMapping("/update")
    @PreAuthorize(value = "hasAnyRole('DOCTOR','ADMIN')")
    public HttpStatus update(
            @Valid @RequestBody UpdateQueueDto updateQueueDto,
            BindingResult bindingResult
    ){
        return queueService.updateStatus(updateQueueDto.getQueueId(), updateQueueDto.getStatus(),bindingResult);
    }

    @PutMapping("/cancel")
    @PreAuthorize(value = "hasRole('USER')")
    public HttpStatus cancel(
            @RequestParam UUID queueId
    ){
        return queueService.cancelQueue(queueId);
    }

    @PutMapping("/transfer")
    @PreAuthorize(value = "hasRole('USER')")
    public HttpStatus transfer(
            @Valid @RequestBody QueueTransferDto queueTransferDto,
            BindingResult bindingResult
    ){
        return queueService.transfer(queueTransferDto,bindingResult);
    }

    @GetMapping("/get/all")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<List<QueueEntity>> getAll(
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size
    ){
        return ResponseEntity.ok(queueService.getAll(page,size));
    }

    @GetMapping("/get/byStatus")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<List<QueueEntity>> getByStatus(
            @RequestParam(required = false,defaultValue = "ACTIVE") String status
    ){
        return ResponseEntity.ok(queueService.getAllByStatus(QueueEntityStatus.valueOf(status)));
    }

    @GetMapping("/get/userQueues")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<List<QueueEntity>> getUserQueues(
            @RequestParam UUID userId
    ){
        return ResponseEntity.ok(queueService.getUserQueues(userId));
    }

    @GetMapping("/get/byId")
    @PreAuthorize(value = "hasRole('ADMIN')")
    public ResponseEntity<QueueEntity> getById(
            @RequestParam UUID queueId
    ){
        return ResponseEntity.ok(queueService.getById(queueId));
    }
}
