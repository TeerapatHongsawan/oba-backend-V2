package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.BadRequestException;
import th.co.scb.onboardingapp.exception.ErrorCodes;

@Slf4j
@Service
public class ServerVersionService {

    @Value("${server.version}")
    private String version;

    public void checkVersion(String requestVersion) {
        if (!version.equals(requestVersion)) {
            throw new BadRequestException(ErrorCodes.INCORRECT_VERSION.name(), ErrorCodes.INCORRECT_VERSION.getMessage() + " (" + version + ")");
        }
    }
}
