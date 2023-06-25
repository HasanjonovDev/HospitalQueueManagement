package uz.pdp.hospitalqueuemanagement.entity.dr;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.pdp.hospitalqueuemanagement.entity.BaseEntity;
import uz.pdp.hospitalqueuemanagement.entity.RoleEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DrEntity extends BaseEntity implements UserDetails {
    private String fullName;
    @Column(unique = true)
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private DrEntityType type;
    @Enumerated(EnumType.STRING)
    private DrEntityStatus status;
    @Enumerated(EnumType.STRING)
    private List<RoleEntity> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(roleEntity -> {
            authorities.add(new SimpleGrantedAuthority(roleEntity.name()));
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
