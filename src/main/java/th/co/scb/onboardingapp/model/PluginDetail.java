package th.co.scb.onboardingapp.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import th.co.scb.onboardingapp.model.entity.BatchTaskEntity;

@Data
@RequiredArgsConstructor
public class PluginDetail {
    String name;
    boolean fixWhenFail;
    boolean service;
    String initialStatus;
    boolean ecWhenFail;

    public PluginDetail(String name, boolean fixWhenFail, boolean service) {
        this(name, fixWhenFail, service, BatchTaskEntity.PENDING_STATUS, false);
    }

    public PluginDetail(String name, boolean ecWhenFail) {
        this(name, false, false, BatchTaskEntity.PENDING_STATUS, ecWhenFail);
    }

    public PluginDetail(String name, boolean fixWhenFail, boolean service, String initialStatus, boolean ecWhenFail) {
        this.name = name;
        this.fixWhenFail = fixWhenFail;
        this.service = service;
        this.initialStatus = initialStatus;
        this.ecWhenFail = ecWhenFail;
    }
}
