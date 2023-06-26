package uz.pdp.hospitalqueuemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDto {
    @NotBlank(message = "FullName cannot be blank")
    private String fullName;
    @NotBlank(message = "Username cannot be blank")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",message = "Password must contain at least one uppercase letter(A-Z),at least one lowercase letter(a-z),at least one digit (0-9) and length at least 8 characters")
    private String password;
}
