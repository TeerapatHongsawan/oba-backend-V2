package th.co.scb.onboardingapp.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;

@Data
public class ServiceDescription implements Comparable<ServiceDescription> {
    private String serviceName;
    private boolean deleted;
    private String productNo;
    private String errorDescription;
    private LocalDateTime NSSDateTime;

    @Override
    public int compareTo(@NotNull ServiceDescription o) {
        return this.serviceName.compareToIgnoreCase(o.serviceName);
    }
}
