package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import th.co.scb.entapi.accounts_creditcards_composite.AccountsCreditcardsCompositeGWApi;
import th.co.scb.entapi.accounts_creditcards_composite.model.ReferenceNumberConvertRequestV3;
import th.co.scb.entapi.accounts_creditcards_composite.model.ReferenceNumberConvertResponseV3;
import th.co.scb.onboardingapp.exception.ConflictException;
import th.co.scb.onboardingapp.helper.IndiHelper;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.enums.CaseErrorCodes;
import th.co.scb.onboardingapp.repository.CaseJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Service
public class CaseApprovedService {

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CaseJpaRepository caseRepository;

    @Autowired
    private ApplicationContextService applicationContextService;

    @Autowired
    private AccountsCreditcardsCompositeGWApi accountsCreditcardsCompositeGWApi;

    public static final String SCB_BANK_CODE = "014";

    public static final String REF_CARD_NUMBER_TYPE = "R";
    private static final String CARD_NO_HACK = "9999999999999999";
    private static final String APPROVE_NAME = "A00000";

    public List<CaseItem> getApprovedCaseList(String empId, String branchId) {
        List<CaseInfo> caseInfoList = mappingHelper.mapAsList(
                caseRepository.getApprovedCaseList(empId, "ONBD", branchId), CaseInfo.class);
        List<CaseItem> caseItems = new ArrayList<>();
        for (CaseInfo caseInfo : caseInfoList) {
            CaseItem caseItem = new CaseItem();
            caseItem.setCaseId(caseInfo.getCaseId());
            caseItem.setCompletedDate(caseInfo.getCompletedDate());
            CustomerInfo custInfo = caseInfo.getCustomerInfo();
            caseItem.setDocNo(custInfo.getDocNo());
            caseItem.setDocType(custInfo.getDocType());
            if ("P7".equalsIgnoreCase(caseItem.getDocType()) || "P8".equalsIgnoreCase(caseItem.getDocType())) {
                caseItem.setCustNameEN(custInfo.getCardInfo().getEngFirstName() + (!StringUtils.isEmpty(custInfo.getCardInfo().getEngMiddleName()) ? " " + custInfo.getCardInfo().getEngMiddleName() + " " : " ") + custInfo.getCardInfo().getEngLastName());
                caseItem.setCustNameTH((!StringUtils.isEmpty(custInfo.getThaiFirstName()) ? custInfo.getThaiFirstName() + " " : "") + (!StringUtils.isEmpty(custInfo.getThaiMiddleName()) ? custInfo.getThaiMiddleName() + " " : "") + custInfo.getThaiLastName());
            } else {
                caseItem.setCustNameEN(custInfo.getCardInfo().getEngFirstName() + " " + custInfo.getCardInfo().getEngLastName());
                caseItem.setCustNameTH(custInfo.getThaiFirstName() + " " + custInfo.getThaiLastName());
            }
            caseItem.setAccountShortName(IndiHelper.getOrElse(() -> custInfo.getAccount().getAccountShortName()).orElse(""));
            caseItem.setAccountLongName(IndiHelper.getOrElse(() -> custInfo.getAccount().getAccountLongName()).orElse(""));
            caseItem.setOriginalAccountLongName(getOriginalAccountLongName(custInfo));


            caseItem.setFasteasy(custInfo.getFasteasy());
            if (custInfo.getDebitCardService() != null && !"ECed".equalsIgnoreCase(custInfo.getDebitCardService().getStatus())) {
                caseItem.setDebitCardService(custInfo.getDebitCardService());
            }
            if (custInfo.getTravelCard() != null && !"ECed".equalsIgnoreCase(custInfo.getTravelCard().getStatus())) {
                caseItem.setTravelCard(custInfo.getTravelCard());
            }

            ApprovalInfo apprInfo = caseInfo.getApprovalInfo().stream()
                    .filter(it -> "E_SUBMIT".equalsIgnoreCase(it.getFunctionCode())).findAny().orElse(null);
            if (apprInfo != null) {
                caseItem.setApprovalName(APPROVE_NAME);
                String approvalId = apprInfo.getApprovalDetails().get(0).getChecker();
                if (!APPROVE_NAME.equals(approvalId)) {
                    EmployeeEntity approver = employeeService.getEmployee(approvalId).orElse(null);
                    if (approver != null) {
                        caseItem.setApprovalName(approver.getFirstNameThai() + " " + approver.getLastNameThai());
                    }
                }
            }
            List<ProductInfo> productInfoList = caseInfo.getProductInfo().stream()
                    .filter(it -> !"ECed".equalsIgnoreCase(it.getStatus())).collect(Collectors.toList());
            if (!productInfoList.isEmpty()) {
                List<ProductItem> productItems = new ArrayList<>();
                for (ProductInfo productInfo : productInfoList) {
                    ProductItem productItem = mappingHelper.map(productInfo, ProductItem.class);
                    if ("EPASSBOOK".equals(productInfo.getProductType())) {
                        productItem.setProductTypeDesc("(E Passbook)");
                    } else {
                        productItem.setProductTypeDesc("");
                    }
                    productItems.add(productItem);

                    // check for Chaiyo case and set CasaDetails for retry send data to mobius
                    AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
                    ChaiyoLoanDetail chaiyoLoanDetail = additionalInfo.getChaiyoLoanDetail();

                    if (chaiyoLoanDetail != null) {
                        CasaDetails casaDetails = getCasaDetailFromProductItemAnChaiyoLoanDetail(productItem, chaiyoLoanDetail, caseInfo.getCaseId(), caseInfo.getAppFormNo(), caseItem.getAccountLongName());
                        caseItem.setCasaDetailsLoanType(chaiyoLoanDetail.getLoanType());
                        caseItem.setCasaDetails(casaDetails);
                    }
                }
                caseItem.setProductList(productItems);
            }

            if (caseInfo.getAdditionalInfo().getChaiyoLoanDetail() != null) {
                AdditionalCaseInfo additionalInfo = caseInfo.getAdditionalInfo();
                ChaiyoLoanDetail chaiyoLoanDetail = additionalInfo.getChaiyoLoanDetail();
                if (caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType().equalsIgnoreCase("reissue") || caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType().equalsIgnoreCase("newcard")) {
                    String cardRefNo = caseInfo.getCustomerInfo().getDebitCardService().getDebitCard().getCardReferenceNo();
                    CasaDetails casaDetails = getCasaDetailChaiyoReIssueType(chaiyoLoanDetail, cardRefNo);
                    Boolean isUpdateCard = caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanDetails().getCardUpdateStatus();
                    caseItem.setIsUpdateChaiyoCardSuccess(isUpdateCard);
                    caseItem.setAcceptConsentPopup(caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanDetails().getAcceptConsentPopup());
                    caseItem.setCasaDetailsLoanType(chaiyoLoanDetail.getLoanType());
                    caseItem.setCasaDetails(casaDetails);
                } else if (caseInfo.getAdditionalInfo().getChaiyoLoanDetail().getLoanType().equalsIgnoreCase("truck")) {
                    caseItem.setCasaDetailsLoanType(chaiyoLoanDetail.getLoanType());
                    if (caseItem.getCasaDetails().getCaseId() == null || "".equalsIgnoreCase(caseItem.getCasaDetails().getCaseId())) {
                        caseItem.getCasaDetails().setCaseId(caseInfo.getCaseId());
                    }
                }
            }

            Investment investment = custInfo.getInvestment();

            if (investment != null) {
                MutualFund mutualFund = investment.getMutualFund();
                if (ofNullable(mutualFund).filter(o -> !"ECed".equalsIgnoreCase(o.getStatus())).isPresent()) {
                    caseItem.setMutualFund(investment.getMutualFund());
                    caseItem.setDividendTaxDeduct(investment.isDividendTaxDeduct());
                }

                Omnibus omnibus = investment.getOmnibus();
                if (ofNullable(omnibus).filter(o -> !"ECed".equalsIgnoreCase(o.getStatus())).isPresent()) {
                    List<MutualFundAccount> mutualFundAccounts = investment.getMutualFundAccounts();
                    MutualFundAccount mutualFundAccount = mutualFundAccounts.stream().filter(m -> m.getClientNumber().equals(omnibus.getClientNumber())).findFirst().orElse(null);
                    caseItem.setOmnibusReferenceAccount(mutualFundAccount);
                    caseItem.setOmnibus(omnibus);
                }

                caseItem.setSecurities(investment.getSecurities());
            }
            caseItems.add(caseItem);
        }

        return caseItems;
    }


    private String getOriginalAccountLongName(CustomerInfo customerInfo) {
        String fullTitileName = defaultString(customerInfo.getThaiFullTitle());
        String thaiFirstName = defaultString(customerInfo.getThaiFirstName());
        String thaiLastName = defaultString(customerInfo.getThaiLastName());
        return fullTitileName + " " + thaiFirstName + " " + thaiLastName;
    }

    private CasaDetails getCasaDetailFromProductItemAnChaiyoLoanDetail(ProductItem productItem, ChaiyoLoanDetail chaiyoLoanDetail, String caseId, String appFormNo, String accountLongName) {
        AdditionalServices additionalServices = productItem.getAdditionalServices();
        CasaDetails casaDetails = new CasaDetails();
        ReferenceNumberConvertRequestV3 referenceNumberConvertRequestV3 = new ReferenceNumberConvertRequestV3();


        if (chaiyoLoanDetail != null) {
            LoanDetails loanDetails = chaiyoLoanDetail.getLoanDetails();
            casaDetails.setTransactionId(loanDetails.getTransactionId());
            casaDetails.setCitizenId(loanDetails.getCitizenId());
            casaDetails.setProductId(loanDetails.getProductId());
            casaDetails.setCaseId(caseId);
            casaDetails.setApplicationNo(appFormNo);
            casaDetails.setCasaAccNo(productItem.getAccountNumber());
            casaDetails.setCasaAccName(accountLongName);
            casaDetails.setBankCode(SCB_BANK_CODE);
            casaDetails.setAppId(loanDetails.getAppId());

            if (chaiyoLoanDetail.getLoanType().equalsIgnoreCase("normal")) {
                List<DebitCard> cards = additionalServices.getCards();
                if (cards != null) {
                    for (DebitCard card : cards) {
                        if (card.getProductId().equalsIgnoreCase("28")) {
                            casaDetails.setChaiyoCardRef(card.getCardReferenceNo());
                            referenceNumberConvertRequestV3.setResolvedReference(card.getCardReferenceNo());
                            referenceNumberConvertRequestV3.setCardNumberType(REF_CARD_NUMBER_TYPE);
                            boolean isNonProd = applicationContextService.isNonProduction();
                            try {
                                if (isNonProd && CARD_NO_HACK.equalsIgnoreCase(card.getCardReferenceNo())) {
                                    casaDetails.setChaiyoCardNo(CARD_NO_HACK);
                                } else {
                                    ReferenceNumberConvertResponseV3 convertCardRes = accountsCreditcardsCompositeGWApi.convertCardV3(referenceNumberConvertRequestV3);
                                    casaDetails.setChaiyoCardNo(convertCardRes.getResolvedReference());
                                }
                            } catch (Exception ex) {
                                throw new ConflictException(CaseErrorCodes.CHAIYO_CONVERT_CARD_NO_FAILED.name(),
                                        CaseErrorCodes.CHAIYO_CONVERT_CARD_NO_FAILED.getMessage());
                            }
                        }
                    }
                }
            }
            if ("N".equalsIgnoreCase(loanDetails.getAcceptConsentPopup())) {
                casaDetails = getCasaDetailsWhenNonAcceptChaiyoConsent(casaDetails, chaiyoLoanDetail.getLoanType());
            }
        }
        return casaDetails;
    }

    private CasaDetails getCasaDetailChaiyoReIssueType(ChaiyoLoanDetail chaiyoLoanDetail, String cardRefNo) {
        CasaDetails casaDetails = new CasaDetails();
        LoanDetails loanDetails = chaiyoLoanDetail.getLoanDetails();
        casaDetails.setRevolvingAccountNo(loanDetails.getRevolvingAccountNo());
        casaDetails.setChaiyoCardRef(cardRefNo);
        return casaDetails;
    }

    private CasaDetails getCasaDetailsWhenNonAcceptChaiyoConsent(CasaDetails casaDetails, String chaiyoFlowType) {
        casaDetails.setCaseId("");
        casaDetails.setApplicationNo(null);
        casaDetails.setCasaAccNo(null);
        casaDetails.setCasaAccName(null);
        casaDetails.setBankCode(null);
        if (chaiyoFlowType.equalsIgnoreCase("normal")) {
            casaDetails.setChaiyoCardNo(null);
            casaDetails.setChaiyoCardRef(null);
        }
        return casaDetails;
    }
}
