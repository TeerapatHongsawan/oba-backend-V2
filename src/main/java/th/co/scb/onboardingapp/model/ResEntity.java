package th.co.scb.onboardingapp.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
public class ResEntity {
    private String code;
    private HttpStatus status;
    private String description;
}
