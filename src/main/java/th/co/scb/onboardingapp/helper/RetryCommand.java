package th.co.scb.onboardingapp.helper;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class RetryCommand {
    private final int maxRetries;

    public RetryCommand(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public void retry(Supplier<Object> function) {
        log.info("Command failed will be retried {} times.", maxRetries);
        int retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                TimeUnit.SECONDS.sleep(2);
                function.get();
                return;
            } catch (Exception ex) {
                retryCounter++;
                log.info("Command failed on retry {} of {} error: {}", retryCounter, maxRetries, ex);
                if (retryCounter >= maxRetries) {
                    log.info("Max retries exceeded.");
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
    }
}
