package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMessage implements Serializable {
    private String topic;
    private String message;
}
