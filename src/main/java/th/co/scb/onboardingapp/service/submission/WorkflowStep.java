package th.co.scb.onboardingapp.service.submission;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class WorkflowStep {

    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    public static final String   ROLLBACK_SUCCESS = "ROLLBACK_SUCCESS";
    public static final String   ROLLBACK_ERROR = "ROLLBACK_ERROR";
    private String name;
    private String status;
    private String errorDesc;
    private boolean isHardStop;
    private LocalDateTime createdDate;
    private long timeUsed;
    private List<String> accountNumbers;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WorkflowStep that = (WorkflowStep) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static WorkflowStep fromPlugin(BaseSubmissionPlugin plugin, Exception ex) {
        return new WorkflowStep(plugin.getName(), ex == null ? SUCCESS : ERROR, ex == null ? null : ex.getMessage(), plugin.isHardStop(), LocalDateTime.now(), 0, null);
    }
}
