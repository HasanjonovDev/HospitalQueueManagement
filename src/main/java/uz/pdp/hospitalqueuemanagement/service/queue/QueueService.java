package uz.pdp.hospitalqueuemanagement.service.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.hospitalqueuemanagement.dto.queue.DrTypeDto;
import uz.pdp.hospitalqueuemanagement.dto.queue.QueueCreateDto;
import uz.pdp.hospitalqueuemanagement.dto.queue.QueueTransferDto;
import uz.pdp.hospitalqueuemanagement.dto.response.LastQueueResponse;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityType;
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

    public HttpStatus updateStatus(UUID queueId, QueueEntityStatus status,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
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
        return queueRepository.findQueueEntitiesByStatus(status);
    }

    public List<QueueEntity> getUserQueues(UUID userId){
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        return queueRepository.findQueueEntitiesByUser(user);
    }

    public List<QueueEntity> getDoctorQueues(UUID doctorId){
        DrEntity drEntity = drRepository.findById(doctorId)
                .orElseThrow(() -> new DataNotFoundException("Doctor not found"));
        return queueRepository.getDoctorQueues(drEntity.getId());
    }
    public QueueEntity getById(UUID queueId){
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("Queue not found"));
    }

    public LastQueueResponse getLastByDrType(DrTypeDto drTypeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
        LastQueueResponse lastQueueResponse = new LastQueueResponse();
        try{
            DrEntityType drEntityType = DrEntityType.valueOf(drTypeDto.getDrType());
            Long queue = queueRepository.lastQueueByType(drEntityType)
                    .orElse(0L);
            if (queue!=0){
                lastQueueResponse.setMessage("Active Queues: "+queue);
            }else {
                lastQueueResponse.setMessage("Active Queues not found");
            }
        }catch (Exception e){
            throw new BadRequestException("Doctor type is wrong");
        }

        return lastQueueResponse;
    }


    public HttpStatus cancelQueue(UUID queueId){
        QueueEntity queueEntity = queueRepository.findById(queueId)
                .orElseThrow(() -> new DataNotFoundException("Queue not found"));
        queueRepository.update(QueueEntityStatus.SKIPPED,queueEntity.getId());
        return HttpStatus.OK;
    }

    public HttpStatus transfer(QueueTransferDto queueTransferDto,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<ObjectError> errors = bindingResult.getAllErrors();
            throw new RequestValidationException(errors);
        }
        UserEntity sender = userRepository.findById(queueTransferDto.getSenderId())
                .orElseThrow(() -> new DataNotFoundException("Sender user not found"));

        UserEntity receiver = userRepository.findById(queueTransferDto.getReceiverId())
                .orElseThrow(() -> new DataNotFoundException("Receiver user not found"));

        QueueEntity senderQueue = queueRepository.getActiveQueue(sender.getId())
                .orElseThrow(() -> new DataNotFoundException("The sender must have an active queue"));

        Optional<QueueEntity> receiverQueue = queueRepository.getActiveQueue(receiver.getId());

        if (receiverQueue.isPresent()){
            if (receiverQueue.get().getDoctor().equals(senderQueue.getDoctor())){
                if (senderQueue.getQueueNumber()<receiverQueue.get().getQueueNumber()){
                    queueRepository.transfer(receiver,sender);
                    queueRepository.updateUser(sender,receiverQueue.get().getId());
                }
                else {
                    throw new BadRequestException("Receivers queue number must be smaller than sender's");
                }
            }
            else {
                throw new BadRequestException("Sender's and Receiver's Doctors is different");
            }
        }
        else {
            queueRepository.updateUser(receiver,senderQueue.getId());
            generateQueue(QueueEntity.builder()
                    .user(sender)
                    .doctor(senderQueue.getDoctor())
                    .status(QueueEntityStatus.ACTIVE)
                    .build());
        }
        return HttpStatus.OK;
    }

    private QueueEntity generateQueue(QueueEntity newQueue){
        Long lastQueue = queueRepository.lastQueue()
                .orElse(firstQueue);
        newQueue.setQueueNumber(++lastQueue);
        return queueRepository.save(newQueue);
    }
}
