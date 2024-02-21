package th.co.scb.onboardingapp.service.cseretrieval;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.entapi.custinfo.CustProfileCustinfoApi;
import th.co.scb.entapi.custinfo.model.AcctBal;
import th.co.scb.entapi.custinfo.model.CustomerAccountsInfo;
import th.co.scb.entapi.deposits.AccountsDepositsApi;
import th.co.scb.entapi.deposits.model.ATMDebitCardInqRs;
import th.co.scb.entapi.deposits.model.RecordDetails;

import th.co.scb.indi.infrastructure.client.ApiException;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.DepositAccount;
import th.co.scb.onboardingapp.model.entity.AccountProductEntity;
import th.co.scb.onboardingapp.service.ProductService;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@CaseContinueExistingQualifier
@ForeignerCaseContinueExistingQualifier
public class CustomerAccountRetrievalPlugin extends BaseRetrievalPlugin<CustomerAccountsInfo> {

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private CustProfileCustinfoApi custProfileCustinfoApi;

    @Autowired
    private AccountsDepositsApi accountsDepositsApi;

    @Autowired
    private ProductService productService;

    @Override
    protected CompletableFuture<CustomerAccountsInfo> retrieveCase(CaseInfo caseInfo) {
        return custProfileCustinfoApi.listCustomerAccountsAsync(caseInfo.getReferenceId());
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, CustomerAccountsInfo customerAccountsInfo) {
        List<DepositAccount> customerAccounts = mappingHelper.mapAsList(Arrays.asList(customerAccountsInfo.getItems()), DepositAccount.class);

        customerAccounts = customerAccounts.stream()
                .filter(item -> "PRIIND".equalsIgnoreCase(item.getAccountRelationCode()))
                .sorted(getDepositAccountComparator())
                .collect(Collectors.toList());

        List<DepositAccount> depositAccounts = new ArrayList<>();
        Map<String, AccountProductEntity> accountProductMap = productService.getAccountProductMap();

        boolean hasBasicAccount = false;
        for (DepositAccount customerAccount : customerAccounts) {

            AcctBal accountBalance = getAccountBalance(customerAccount);
            setAccountBalance(customerAccount, accountBalance);

            AccountProductEntity product = accountProductMap.get(customerAccount.getAccountProdCode());
            if (product != null) {
                customerAccount.setProductCode(product.getProductCode());
            }
            customerAccount.setAcctBalList(null);
            customerAccount.setApplicationId(getApplicationId(customerAccount.getAccountType()));

            if (isBasicAccount(customerAccount)) {
                hasBasicAccount = true;
            }

            depositAccounts.add(customerAccount);
        }

        if (hasBasicAccount && "ONBD".equals(caseInfo.getCaseTypeID())) {
            getDebitCardDetailsForBasicAccount(depositAccounts);
        }

        caseInfo.getCustomerInfo().setCustomerAccounts(depositAccounts);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        if (ex instanceof ApiException && ((ApiException) ex).getStatusCode() == 404) {
            caseInfo.getCustomerInfo().setCustomerAccounts(new ArrayList<>());
        } else {
            super.handleError(caseInfo, ex);
        }
    }

    private void setAccountBalance(DepositAccount customerAccount, AcctBal accountBalance) {
        if (accountBalance == null) {
            customerAccount.setBalance(new BigDecimal(0));
        } else {
            customerAccount.setBalance(accountBalance.getAmt());
        }
    }

    @Nullable
    private AcctBal getAccountBalance(DepositAccount customerAccount) {
        return Arrays.stream(customerAccount.getAcctBalList())
                .filter(it -> "AVAILBALANCE".equalsIgnoreCase(it.getBalType()))
                .findAny()
                .orElse(null);
    }

    private Comparator<DepositAccount> getDepositAccountComparator() {
        return (a, b) -> {
            int scoreA = "IM".equals(a.getApplicationId()) ? 1 : -1;
            int scoreB = "IM".equals(b.getApplicationId()) ? 1 : -1;
            if (scoreA == scoreB) {
                return a.getAccountNumber().compareTo(b.getAccountNumber());
            } else {
                return scoreA - scoreB;
            }
        };
    }

    private void getDebitCardDetailsForBasicAccount(List<DepositAccount> customerAccounts) {
        List<RecordDetails> recordDetailsList = new ArrayList<>();
        List<String> accountNumbersRequest = new ArrayList<>();

        for (DepositAccount customerAccount : customerAccounts) {
            if (!isBasicAccount(customerAccount)) {
                continue;
            }
            accountNumbersRequest.add(customerAccount.getAccountNumber());
            if (accountNumbersRequest.size() >= 20) {
                ATMDebitCardInqRs res = findByAccountNumbers(accountNumbersRequest);
                if (res != null && res.getRecordDetails() != null) {
                    recordDetailsList.addAll(Arrays.asList(res.getRecordDetails()));
                }
                accountNumbersRequest = new ArrayList<>();
            }
        }
        if (!accountNumbersRequest.isEmpty()) {
            ATMDebitCardInqRs res = findByAccountNumbers(accountNumbersRequest);
            if (res != null && res.getRecordDetails() != null) {
                recordDetailsList.addAll(Arrays.asList(res.getRecordDetails()));
            }
        }

        if (!recordDetailsList.isEmpty()) {
            for (DepositAccount outputAccount : customerAccounts) {
                outputAccount.setDebitCards(recordDetailsList.stream().filter(r -> r.getAccountId().equals(outputAccount.getAccountNumber())).collect(Collectors.toList()));
            }
        }
    }

    private ATMDebitCardInqRs findByAccountNumbers(List<String> accountNumbersRequest) {
        try {
            return accountsDepositsApi.findByAccountNumbers(accountNumbersRequest.toArray(new String[0]), null, "0", "0");
        } catch (ApiException ex) {
            if (ex.getStatusCode() == 404) {
                return null;
            }
            throw new ApplicationException(ex);
        }
    }

    private boolean isBasicAccount(DepositAccount customerAccount) {
        return "BASV".equals(customerAccount.getProductCode()) || "BASN".equals(customerAccount.getProductCode());
    }

    private String getApplicationId(String accountType) {
        switch (accountType) {
            case "0 ":
            case "1":
            case "2":
                return "ST";
            case "3":
                return "IM";
            default:
                return null;
        }
    }
}