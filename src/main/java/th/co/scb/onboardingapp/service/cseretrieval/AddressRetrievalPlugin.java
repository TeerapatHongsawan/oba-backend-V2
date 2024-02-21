package th.co.scb.onboardingapp.service.cseretrieval;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.individuals.CustProfileIndividualsApi;
import th.co.scb.entapi.individuals.model.Addresses;

import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.onboardingapp.helper.CaseHelper;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerAddress;
import th.co.scb.onboardingapp.service.CaseService;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@CaseContinueExistingQualifier
@ForeignerCaseContinueExistingQualifier
public class AddressRetrievalPlugin extends BaseRetrievalPlugin<Addresses> {

    @Autowired
    private CustProfileIndividualsApi individualsApi;

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private CaseService caseService;

    @Override
    protected CompletableFuture<Addresses> retrieveCase(CaseInfo caseInfo) {
        return this.individualsApi.listIndividualsAddressesAsync(caseInfo.getReferenceId(), null, null, null);
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, Addresses addresses) {
        List<CustomerAddress> customerAddressSortedByAddressSeqId = mappingHelper.mapAsList(
                Arrays.stream(addresses.getItems())
                        .sorted((firstAddress, secondAddress) -> secondAddress.getAddressSeqID().compareTo(firstAddress.getAddressSeqID()))
                        .collect(Collectors.toList()), CustomerAddress.class);

        List<CustomerAddress> customerAddresses = new ArrayList<>();

        CustomerAddress currentAddress = getFirstCustomerAddress(customerAddressSortedByAddressSeqId);
        if (currentAddress != null) {
            setCurrentAddress(currentAddress, customerAddresses);
        }

        if (!CaseHelper.isForeigner(caseInfo)) {
            setIdentificationCardAddress(customerAddressSortedByAddressSeqId, customerAddresses);
        }

        setOfficeAddress(customerAddressSortedByAddressSeqId, customerAddresses);

        setDomicileAddress(customerAddressSortedByAddressSeqId, customerAddresses);

        caseInfo.getCustomerInfo().setAddresses(customerAddresses);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        if (ex instanceof ApiException && ((ApiException) ex).getStatusCode() == 404) {
            caseInfo.getCustomerInfo().setAddresses(new ArrayList<>());
        } else {
            super.handleError(caseInfo, ex);
        }
    }

    private void setCurrentAddress(CustomerAddress firstCustomerAddress, List<CustomerAddress> customerAddresses) {
        firstCustomerAddress = mappingHelper.map(firstCustomerAddress, CustomerAddress.class);

        firstCustomerAddress.setInternalTypeCode("C");
        if (StringUtils.isEmpty(firstCustomerAddress.getUsageCode())) {
            firstCustomerAddress.setUsageCode("H");
        }

        customerAddresses.add(firstCustomerAddress);
    }

    private void setIdentificationCardAddress(List<CustomerAddress> customerAddressSortedByAddressSeqId, List<CustomerAddress> customerAddresses) {
        CustomerAddress identificationCardAddress = getAddressByUsageCode(customerAddressSortedByAddressSeqId, "I");
        if (identificationCardAddress != null) {
            identificationCardAddress.setInternalTypeCode("I");
            if (StringUtils.isEmpty(identificationCardAddress.getUsageCode())) {
                identificationCardAddress.setUsageCode("I");
            }

            customerAddresses.add(identificationCardAddress);
        }
    }

    private void setDomicileAddress(List<CustomerAddress> customerAddressSortedByAddressSeqId, List<CustomerAddress> customerAddresses) {
        CustomerAddress domicileAddress = getAddressByUsageCode(customerAddressSortedByAddressSeqId, "N");
        if (domicileAddress != null) {
            domicileAddress.setInternalTypeCode("N");
            if (StringUtils.isEmpty(domicileAddress.getUsageCode())) {
                domicileAddress.setUsageCode("N");
            }
            domicileAddress.setZipCode(!"99999".equals(domicileAddress.getZipCode()) ? domicileAddress.getZipCode() : "");
            customerAddresses.add(domicileAddress);
        }
    }

    private void setOfficeAddress(List<CustomerAddress> customerAddressSortedByAddressSeqId, List<CustomerAddress> customerAddresses) {
        CustomerAddress officeAddress = getAddressByUsageCode(customerAddressSortedByAddressSeqId, "O");
        if (officeAddress != null) {
            officeAddress.setInternalTypeCode("O");
            if (StringUtils.isEmpty(officeAddress.getUsageCode())) {
                officeAddress.setUsageCode("O");
            }
            customerAddresses.add(officeAddress);
        }
    }

    private CustomerAddress getAddressByUsageCode(List<CustomerAddress> customerAddresses, String usageCode) {
        return customerAddresses.stream()
                .filter(it -> usageCode.equals(it.getUsageCode()))
                .findAny()
                .orElse(null);
    }

    private CustomerAddress getFirstCustomerAddress(List<CustomerAddress> customerAddresses) {
        return customerAddresses.stream()
                .filter(address -> "001".equals(address.getAddressSeqID()))
                .findAny()
                .orElse(null);
    }
}
