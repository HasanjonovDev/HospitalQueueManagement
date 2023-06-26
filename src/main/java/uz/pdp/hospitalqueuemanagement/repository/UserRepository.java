package uz.pdp.hospitalqueuemanagement.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntity;
import uz.pdp.hospitalqueuemanagement.entity.user.UserEntityStatus;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByUsername (String username);

    @Modifying
    @Transactional
    @Query("update users set status = :status where id = :id")
    void update(@Param("status")UserEntityStatus status,@Param("id") UUID id);
    @Query("select u from users u where u.roles =:roles order by u.status asc")
    List<UserEntity> getAll(@Param("roles")List<RoleEntity> roleEntities);
}
