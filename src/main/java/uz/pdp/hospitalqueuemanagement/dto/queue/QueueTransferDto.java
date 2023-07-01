package uz.pdp.hospitalqueuemanagement.dto.queue;

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
public class QueueTransferDto {
    @NotNull(message = "Sender Id must be present")
    private UUID senderId;
    @NotNull(message = "Receiver Id must be present")
    private UUID receiverId;
}
