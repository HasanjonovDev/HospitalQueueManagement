package uz.pdp.hospitalqueuemanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.service.queue.QueueService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class QueueController {
    private final QueueService queueService;
    @PostMapping("/add")
    public ResponseEntity<QueueEntity> addQueue(
            @RequestParam UUID userId,
            @RequestParam UUID drId
    ){
        return ResponseEntity.ok(queueService.addQueue(userId,drId));
    }
}
