package th.co.scb.onboardingapp.service.cseretrieval;

import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import th.co.scb.onboardingapp.model.CaseInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;


@Slf4j
public abstract class BaseRetrievalPlugin<T> {

    public boolean isApply(CaseInfo caseInfo) {
        return true;
    }

    protected abstract CompletableFuture<T> retrieveCase(CaseInfo caseInfo);

    protected abstract void updateCase(CaseInfo caseInfo, T result);

    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        throw Throwables.propagate(ex);
    }

    public CompletableFuture<Consumer<CaseInfo>> retrieve(CaseInfo caseInfo) {
        String pluginName = this.getClass().getSimpleName();
        LocalDateTime startTime = LocalDateTime.now();
        return retrieveCase(caseInfo).handle((ok, ko) -> {
            long timeMillis = Duration.between(startTime, LocalDateTime.now()).toMillis();
            if (ko == null) {
                log.info("Case Retrieval Success => {}: {} ms", pluginName, timeMillis);
                return c -> this.updateCase(c, ok);
            } else {
                log.info("Case Retrieval Error => {}: {} ms", pluginName, timeMillis);
                return c -> this.handleError(c, ko);
            }
        });
    }
}