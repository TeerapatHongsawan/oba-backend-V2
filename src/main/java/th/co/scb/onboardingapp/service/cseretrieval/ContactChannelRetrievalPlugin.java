package th.co.scb.onboardingapp.service.cseretrieval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.individuals.CustomerProfileIndividualsV2Api;
import th.co.scb.entapi.individuals.model.ContactChannelsV2;

import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerContactChannel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@CaseContinueExistingQualifier
@ForeignerCaseContinueExistingQualifier
public class ContactChannelRetrievalPlugin extends BaseRetrievalPlugin<ContactChannelsV2> {

    @Autowired
    private CustomerProfileIndividualsV2Api customerProfileIndividualsV2Api;

    @Autowired
    private MappingHelper mappingHelper;

    @Override
    public CompletableFuture<ContactChannelsV2> retrieveCase(CaseInfo caseInfo) {
        return this.customerProfileIndividualsV2Api.listIndividualsContactChannels1Async(caseInfo.getReferenceId(), null, null);
    }

    @Override
    public void updateCase(CaseInfo caseInfo, ContactChannelsV2 contactChannels) {
        List<CustomerContactChannel> contacts = new ArrayList<>();
        List<CustomerContactChannel> list = mappingHelper.mapAsList(Arrays.asList(contactChannels.getItems()), CustomerContactChannel.class);
        List<CustomerContactChannel> srcEmails = list.stream()
                .filter(it -> "005".equalsIgnoreCase(it.getContactTypeCode()))
                .collect(Collectors.toList());

        for (CustomerContactChannel contact : list) {
            if ("005".equalsIgnoreCase(contact.getContactTypeCode()) && "O".equalsIgnoreCase(contact.getContactUsageCode()) && srcEmails.stream().anyMatch(it -> "H".equalsIgnoreCase(it.getContactUsageCode()) && it.getContactNumber().equalsIgnoreCase(contact.getContactNumber()))) {
                continue;
            }
            contacts.add(contact);
        }
        caseInfo.getCustomerInfo().setContactChannels(contacts);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        if (ex instanceof ApiException && ((ApiException) ex).getStatusCode() == 404) {
            caseInfo.getCustomerInfo().setContactChannels(new ArrayList<>());
        } else {
            super.handleError(caseInfo, ex);
        }
    }
}