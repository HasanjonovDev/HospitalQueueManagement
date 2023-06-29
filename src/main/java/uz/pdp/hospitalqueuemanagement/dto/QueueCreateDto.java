package uz.pdp.hospitalqueuemanagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QueueCreateDto {
    @NotNull(message = "User id must be present")
    private UUID userId;

    @NotNull(message = "Doctor id must be present")
    private UUID drId;
}
