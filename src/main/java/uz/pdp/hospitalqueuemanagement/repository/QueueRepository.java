package uz.pdp.hospitalqueuemanagement.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityType;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntity;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntityStatus;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface QueueRepository extends JpaRepository<QueueEntity, UUID> {
    @Query("select max(q.queueNumber) from queues q")
    Optional<Long> lastQueue();

    @Query("select max(q.queueNumber) from queues q where q.doctor.type = :type and q.status = 0" )
    Optional<Long> lastQueueByType(@Param("type")DrEntityType drEntityType);

    @Modifying
    @Transactional
    @Query("update queues set status = :status where id = :id")
    void update(@Param("status") QueueEntityStatus status, @Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("update queues set user =:receiver where user = :sender")
    void transfer(@Param("receiver") UserEntity rec,@Param("sender") UserEntity sen);

    @Modifying
    @Transactional
    @Query("update queues set user = :user where id = :id")
    void updateUser(@Param("user") UserEntity sender,@Param("id") UUID id);

    @Query("select q from queues q where q.user.id = :id and q.status = 0")
    Optional<QueueEntity> getActiveQueue(@Param("id")UUID userId);

    @Query("select q from queues q where q.doctor.id =:id")
    List<QueueEntity> getDoctorQueues(@Param("id") UUID id);

    List<QueueEntity> findQueueEntitiesByStatus(QueueEntityStatus status);

    List<QueueEntity> findQueueEntitiesByUser(UserEntity user);

}
