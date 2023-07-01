package uz.pdp.hospitalqueuemanagement.dto.queue;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DrTypeDto {
    @NotBlank(message = "Dr type must be present")
    private String drType;
}
