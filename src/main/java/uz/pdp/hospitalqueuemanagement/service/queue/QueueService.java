package uz.pdp.hospitalqueuemanagement.service.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.hospitalqueuemanagement.dto.QueueCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntityStatus;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.exception.BadRequestException;
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;
import uz.pdp.hospitalqueuemanagement.repository.DrRepository;
import uz.pdp.hospitalqueuemanagement.repository.QueueRepository;
import uz.pdp.hospitalqueuemanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final QueueRepository queueRepository;
    private final UserRepository userRepository;
    private final DrRepository drRepository;
    private final Long firstQueue = 0L;
    public QueueEntity addQueue(QueueCreateDto queueCreateDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
        UserEntity user = userRepository.findById(queueCreateDto.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User Not Found"));
        DrEntity dr = drRepository.findById(queueCreateDto.getDrId())
                .orElseThrow(() -> new DataNotFoundException("Doctor Not Found"));

        Optional<QueueEntity> entityByUser = queueRepository.findQueueEntityByUser(user);
        if (entityByUser.isPresent()){
            QueueEntity queueEntity = entityByUser.get();
            if (!(queueEntity.getStatus().equals(QueueEntityStatus.COMPLETED) || queueEntity.getStatus().equals(QueueEntityStatus.SKIPPED))){
                throw new BadRequestException("Already in Queue");
            }
        }
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
