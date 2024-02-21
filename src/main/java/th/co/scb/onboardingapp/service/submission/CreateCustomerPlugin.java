package th.co.scb.onboardingapp.service.submission;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.individuals.CustProfileIndividualsApi;
import th.co.scb.entapi.individuals.model.CreateIndividual;
import th.co.scb.entapi.individuals.model.ReferenceID;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerAddress;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.service.TJLogProcessor;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class CreateCustomerPlugin implements BaseSubmissionPlugin {

    public static final String NAME = "createCustomer";
    @Autowired
    CustProfileIndividualsApi custProfileIndividualsApi;

    @Autowired
    MappingHelper mappingHelper;

    @Autowired
    TJLogProcessor tjLogProcessor;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isApply(CaseInfo caseInfo) {
        return "N".equalsIgnoreCase(caseInfo.getCustomerInfo().getIsExistingCustomer());
    }

    @Override
    public boolean isHardStop() {
        return true;
    }

    @Override
    public void submit(CaseInfo caseInfo, SubmissionState state) {
        createCustomer(caseInfo, state);
    }

    private void createCustomer(CaseInfo caseInfo, SubmissionState state) {
        CustomerInfo customerInfo = caseInfo.getCustomerInfo();
        CreateIndividual individualCust = mappingHelper.map(customerInfo, CreateIndividual.class);

        //Primary Address
        CustomerAddress address = customerInfo.getAddresses().stream()
                .filter(it -> "Y".equalsIgnoreCase(it.getIsPrimary()))
                .findAny().orElse(null);
        if (address != null) {
            address.setContactIndicator("Y");
            address.setCurrentAddressFlag("Y");
            Arrays.stream(individualCust.getContactChannels()).forEach(it -> {
                it.setSourceOfInfo("INDI");
                it.setLastUpdateUser(caseInfo.getEmployeeId().substring(1));
            });
            String[] thaiAddressNumberArr = address.getThaiAddressNumber().split("หมู่บ้าน");
            if (thaiAddressNumberArr.length == 2) {
                address.setThaiAddressNumber(thaiAddressNumberArr[0]);
                address.setThaiAddressVillage(thaiAddressNumberArr[1]);
            }
        }
        individualCust.setAddress(address);

        //Issue Date & Expire Date
        ReferenceID reference = new ReferenceID();
        reference.setReferenceExpiryDate("9999-99-99".equalsIgnoreCase(customerInfo.getExpDate()) ? "9999-12-31" : customerInfo.getExpDate());
        reference.setReferenceIDNumber(customerInfo.getDocNo());
        if ("P1".equalsIgnoreCase(customerInfo.getDocType())) {
            reference.setReferenceEffectiveDate(customerInfo.getIssueDate());
            individualCust.setCitizenID(reference);
        } else if ("P8".equalsIgnoreCase(customerInfo.getDocType())) {
            reference.setReferenceEffectiveDate(StringUtils.isEmpty(customerInfo.getIssueDate()) ? "1900-01-01" : customerInfo.getIssueDate());
            individualCust.setPassportIssuingCountry(customerInfo.getDocNo().substring(0, 2));
            individualCust.setPassportNumber(reference);
        } else if ("P7".equalsIgnoreCase(customerInfo.getDocType())) {
            reference.setReferenceEffectiveDate(StringUtils.isEmpty(customerInfo.getIssueDate()) ? "1900-01-01" : customerInfo.getIssueDate());
            individualCust.setAlienID(reference);
        }

        individualCust.setFirstAcctOpenBranch(caseInfo.getBookingBranchId());

        individualCust.setISICCode(customerInfo.getOccupationIsicCode());

        CompletableFuture<CreateIndividual> future = custProfileIndividualsApi.createIndividualAsync(individualCust);
        state.submit(future, response -> {
            caseInfo.setReferenceId(response.getRMCustID());
            tjLogProcessor.getTxnWithName(caseInfo, "TXN017", state.getTjlogList());
            tjLogProcessor.getTxnAddCustInfo(caseInfo, state.getTjlogList());
        });
    }
}
