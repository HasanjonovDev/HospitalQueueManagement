package uz.pdp.hospitalqueuemanagement.dto.queue;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.hospitalqueuemanagement.entity.queue.QueueEntityStatus;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateQueueDto {
    @NotNull(message = "Queue Id must be present")
    private UUID queueId;
    @NotNull(message = "Status must be present")
    private QueueEntityStatus status;
}
