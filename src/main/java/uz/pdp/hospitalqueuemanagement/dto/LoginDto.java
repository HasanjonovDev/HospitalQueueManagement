package uz.pdp.hospitalqueuemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "Username Cannot Be Empty")
    private String username;
    @NotBlank(message = "Password Cannot Be Empty")
    private String password;
}
