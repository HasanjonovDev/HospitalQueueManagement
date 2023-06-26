package uz.pdp.hospitalqueuemanagement.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntity;
import uz.pdp.hospitalqueuemanagement.entity.dr.DrEntityStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DrRepository extends JpaRepository<DrEntity, UUID> {
    Optional<DrEntity> findDrEntityByUsername(String username);
    @Modifying
    @Transactional
    @Query("update doctors set status = :status where id =:id")
    void update(@Param("status")DrEntityStatus status,@Param("id") UUID id);
}
