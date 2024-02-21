package th.co.scb.onboardingapp.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class GeneralParam {
    private String type;
    private Map<String, Object> value;
    private boolean serverOnly;

    public <T> List<T> getValueList() {
        return value == null ? Collections.emptyList() : (List<T>) value.get("list");
    }
}
