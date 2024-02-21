package th.co.scb.onboardingapp.service;

import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@ManagedResource(objectName = "indiJmx:name=JmxFlags")
@Profile("hack")
public class JmxFlags {
    private Set<String> downList = new HashSet<>();

    public boolean has(String feature) {
        return downList.contains(feature);
    }

    @ManagedOperation
    public Set<String> list() {
        return downList;
    }

    @ManagedOperation
    public void addOperationId(String feature) {
        downList.add(feature);
    }

    @ManagedOperation
    public void removeOperationId(String feature) {
        downList.remove(feature);
    }

    @ManagedOperation
    public void clear() {
        downList.clear();
    }
}
