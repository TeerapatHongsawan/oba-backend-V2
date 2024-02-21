package th.co.scb.onboardingapp.service;

import com.spotify.futures.CompletableFutures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.service.cseretrieval.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    @Autowired(required = false)
    @CaseCreateExistingQualifier
    private List<BaseRetrievalPlugin<?>> existingCustomerPlugins;

    @Autowired(required = false)
    @CaseCreateNewQualifier
    private List<BaseRetrievalPlugin<?>> newCustomerPlugins;

    @Autowired(required = false)
    @CaseContinueExistingQualifier
    private List<BaseRetrievalPlugin<?>> continueExistingCustomerPlugins;

    @Autowired(required = false)
    @CaseContinueNewQualifier
    private List<BaseRetrievalPlugin<?>> continueNewCustomerPlugins;

    @Autowired(required = false)
    @ForeignerCaseCreateExistingQualifier
    private List<BaseRetrievalPlugin<?>> existingForeignerCustomerPlugins;

    @Autowired(required = false)
    @ForeignerCaseCreateNewQualifier
    private List<BaseRetrievalPlugin<?>> newForeignerCustomerPlugins;

    @Autowired(required = false)
    @ForeignerCaseContinueExistingQualifier
    private List<BaseRetrievalPlugin<?>> continueForeignerExistingCustomerPlugins;

    public CompletableFuture<Void> fetchExistingCustomerData(CaseInfo caseInfo) {
        return fetchData(this.existingCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchNewCustomerData(CaseInfo caseInfo) {
        return fetchData(this.newCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchContinueExistingCustomerData(CaseInfo caseInfo) {
        return fetchData(this.continueExistingCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchContinueNewCustomerData(CaseInfo caseInfo) {
        return fetchData(this.continueNewCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchExistingForeignerCustomerData(CaseInfo caseInfo) {
        return fetchData(this.existingForeignerCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchNewForeignerCustomerData(CaseInfo caseInfo) {
        return fetchData(this.newForeignerCustomerPlugins, caseInfo);
    }

    public CompletableFuture<Void> fetchContinueForeignerExistingCustomerData(CaseInfo caseInfo) {
        return fetchData(this.continueForeignerExistingCustomerPlugins, caseInfo);
    }

    private static CompletableFuture<Void> fetchData(List<BaseRetrievalPlugin<?>> plugins, CaseInfo caseInfo) {
        List<CompletableFuture<Consumer<CaseInfo>>> tasks = plugins.stream()
                .filter(plugin -> plugin.isApply(caseInfo))
                .map(plugin -> plugin.retrieve(caseInfo))
                .collect(Collectors.toList());

        return CompletableFutures.allAsList(tasks)
                .thenAccept(results -> {
                    for (Consumer<CaseInfo> result : results) {
                        result.accept(caseInfo);
                    }
                });
    }

    public void setContinueNewCustomerPlugins(List<BaseRetrievalPlugin<?>> continueNewCustomerPlugins) {
        this.continueNewCustomerPlugins = continueNewCustomerPlugins;
    }
}
