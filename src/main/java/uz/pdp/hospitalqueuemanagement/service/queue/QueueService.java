package uz.pdp.hospitalqueuemanagement.service.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.hospitalqueuemanagement.dto.QueueCreateDto;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntityStatus;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntityStatus;
import uz.pdp.hospitalqueuemanagement.exception.BadRequestException;
import uz.pdp.hospitalqueuemanagement.exception.DataNotFoundException;
import uz.pdp.hospitalqueuemanagement.exception.RequestValidationException;
import uz.pdp.hospitalqueuemanagement.repository.DrRepository;
import uz.pdp.hospitalqueuemanagement.repository.QueueRepository;
import uz.pdp.hospitalqueuemanagement.repository.UserRepository;

import java.util.List;
import java.util.UUID;

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

        if (queueRepository.getActiveQueue(user.getId()).isPresent()){
            throw new BadRequestException("Already in Queue");
        }

        return generateQueue(QueueEntity.builder()
                .doctor(dr)
                .user(user)
                .status(QueueEntityStatus.ACTIVE)
                .build());
    }

    public HttpStatus updateStatus(UUID queueId, QueueEntityStatus status){
        QueueEntity queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("Queue not found"));
        queueRepository.update(status,queue.getId());
        return HttpStatus.OK;
    }

    public List<QueueEntity> getAll(int page,int size){
        Sort sort = Sort.by(Sort.Direction.ASC,"status");
        Pageable pageable = PageRequest.of(page,size,sort);
        return queueRepository.findAll(pageable).getContent();
    }

    public List<QueueEntity> getAllByStatus(QueueEntityStatus status){
        return queueRepository.getAllByStatus(status);
    }

    public List<QueueEntity> getUserQueues(UUID userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return queueRepository.findQueueEntitiesByUser(user);
    }

    public QueueEntity getById(UUID queueId){
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("Queue not found"));
    }

    private QueueEntity generateQueue(QueueEntity newQueue){
        Long lastQueue = queueRepository.lastQueue()
                .orElse(firstQueue);
        newQueue.setQueueNumber(++lastQueue);
        userRepository.update(UserEntityStatus.IN_RECEPTION,newQueue.getUser().getId());
        return queueRepository.save(newQueue);
    }
}
