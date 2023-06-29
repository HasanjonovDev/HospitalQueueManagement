package uz.pdp.hospitalqueuemanagement.entity.queue;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import uz.pdp.hospitalqueuemanagement.entity.BaseEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;

@Entity(name = "queues")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QueueEntity extends BaseEntity {
    @OneToOne
    private UserEntity user;
    private Long queueNumber;
    @ManyToOne
    private DrEntity doctor;
    private QueueEntityStatus status;
}
