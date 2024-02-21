package th.co.scb.onboardingapp.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.LogRequest;
import th.co.scb.onboardingapp.model.RunType;

import static net.logstash.logback.argument.StructuredArguments.kv;
import static th.co.scb.onboardingapp.model.LoggingConstant.FE_TIMESTAMP;
import static th.co.scb.onboardingapp.model.LoggingConstant.RUN_TYPE;

@Slf4j
@RestController
public class LogController {

    @PostMapping("/api/log")
    public void log(@RequestBody LogRequest[] requests) {
        // save 
        String savedRunTypeMdc = MDC.get(RUN_TYPE);
        try {
            // change runType to FRONT_END to log frontend message
            MDC.put(RUN_TYPE, RunType.FRONT_END.getCode());
            for (LogRequest request : requests) {
                switch (request.getLevel()) {
                    case "info":
                    case "log":
                        log.debug(request.getMessage(), kv(FE_TIMESTAMP, request.getTimestampISO()));
                        break;
                    case "warn":
                        log.warn(request.getMessage(), kv(FE_TIMESTAMP, request.getTimestampISO()));
                        break;
                    case "error":
                        log.error(request.getMessage(), kv(FE_TIMESTAMP, request.getTimestampISO()));
                        break;
                }
            }
        } finally {
            // restore
            MDC.put(RUN_TYPE, savedRunTypeMdc);
        }
    }
}

