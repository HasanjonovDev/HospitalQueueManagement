package uz.pdp.hospitalqueuemanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;

import java.util.UUID;

@Repository
public interface QueueRepository extends JpaRepository<DrEntity, UUID> {
}
