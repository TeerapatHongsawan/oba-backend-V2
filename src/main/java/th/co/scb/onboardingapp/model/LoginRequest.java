package th.co.scb.onboardingapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;



@Data
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{5,10}$", message = "username is invalid")
    private String username;

    @NotNull
    @NotBlank(message = "password is empty")
    @Size(max = 100, message = "password more than 100 characters")
    private String password;

    @NotNull
    private String appName;

    @NotNull
    private String version;

    @NotNull
    private String deviceId;

    @NotNull
    private String deviceIp;

    private String sessionId;

    private String token;

    private Boolean isConfirmLoginDuplicateUser;

}
