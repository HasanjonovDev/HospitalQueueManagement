package uz.pdp.hospitalqueuemanagement.service.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.repository.DrRepository;
import uz.pdp.hospitalqueuemanagement.repository.QueueRepository;
import uz.pdp.hospitalqueuemanagement.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepository;
    private final UserRepository userRepository;
    private final DrRepository drRepository;
    private final Long firstQueue = 0L;
    public QueueEntity addQueue(UUID userId, UUID drId){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));
        DrEntity dr = drRepository.findById(drId)
                .orElseThrow(() -> new DataNotFoundException("Doctor Not Found"));
        return generateQueueNumber(QueueEntity.builder()
                .doctor(dr)
                .user(user)
                .build());
    }
    private QueueEntity generateQueueNumber(QueueEntity newQueue){
        Long lastQueue = queueRepository.lastQueue()
                .orElse(firstQueue);
        newQueue.setQueueNumber(++lastQueue);
        return queueRepository.save(newQueue);
    }
}
