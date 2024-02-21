package th.co.scb.onboardingapp.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedListKey implements Serializable {
    private String caseId;
    private String email;
}
