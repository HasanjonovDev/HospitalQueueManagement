package uz.pdp.hospitalqueuemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QueueRepository extends JpaRepository<QueueEntity, UUID> {
    @Query("select max(q.queueNumber) from queues q")
    Optional<Long> lastQueue();

    Optional<QueueEntity> findQueueEntityByUser(UserEntity user);
}
