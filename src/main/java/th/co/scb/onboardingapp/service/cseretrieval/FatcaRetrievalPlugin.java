package th.co.scb.onboardingapp.service.cseretrieval;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.fatca.model.ValidateFatcaCountryTaxsItem;
import th.co.scb.fatca.model.ValidateFatcaRequest;
import th.co.scb.fatca.model.ValidateFatcaResponse;
import th.co.scb.fatca.model.ValidateFatcaSearchResponseDetailsItem;
import th.co.scb.onboardingapp.exception.ApplicationException;
import th.co.scb.onboardingapp.helper.ForeignerHelper;
import th.co.scb.onboardingapp.model.ApprovalInfo;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CountryTax;
import th.co.scb.onboardingapp.model.Crs;
import th.co.scb.onboardingapp.model.Fatca;
import th.co.scb.onboardingapp.model.IdenDetail;
import th.co.scb.onboardingapp.model.IdentificationsItem;
import th.co.scb.onboardingapp.service.FatcaApiService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.defaultString;

@Slf4j
@Service
@CaseCreateExistingQualifier
@ForeignerCaseCreateExistingQualifier
public class FatcaRetrievalPlugin extends BaseRetrievalPlugin<ValidateFatcaResponse> {

//    @Autowired
//    FatcaApi fatcaApi;

    @Autowired
    FatcaApiService fatcaApiService;

    @Override
    protected CompletableFuture<ValidateFatcaResponse> retrieveCase(CaseInfo caseInfo) {

        ValidateFatcaRequest validateFatcaRequest = new ValidateFatcaRequest();
        validateFatcaRequest.setIdnumber(caseInfo.getCustomerInfo().getDocNo());
        validateFatcaRequest.setIdtype(caseInfo.getCustomerInfo().getDocType());
        validateFatcaRequest.setCustRmNo(caseInfo.getReferenceId());
        validateFatcaRequest.setUserid("OBIN");

        if (ForeignerHelper.isForeigner(caseInfo)) {
            if (!StringUtils.isEmpty(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo())) {
                validateFatcaRequest.setIdnumber(caseInfo.getCustomerInfo().getCardInfo().getOldDocNo());
            } else {
                validateFatcaRequest.setIdnumber(caseInfo.getCustomerInfo().getDocNo());
            }
        }

//        return fatcaApi.validateFatcaAsync(validateFatcaRequest);
        return fatcaApiService.validateFatcaAsync(validateFatcaRequest);
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, ValidateFatcaResponse validateFatcaResponse) {

        IdentificationsItem fatcaIdentificationsItem = new IdentificationsItem();
        fatcaIdentificationsItem.setIdenType("fatca");

        IdenDetail idenDetail = new IdenDetail();
        Fatca fatca = new Fatca();

        if ((validateFatcaResponse.getSearchResponseDetails() == null || validateFatcaResponse.getSearchResponseDetails().isEmpty()) &&
                (validateFatcaResponse.getStatus() != null)) {
            throw new ApplicationException("call validate FATCA success with errCode: " + validateFatcaResponse.getStatus().getErrCode());
        }

        String fatcaStatus = Objects.requireNonNull(validateFatcaResponse.getSearchResponseDetails()).get(0).getFATCAstatus();

        if ("Y".equalsIgnoreCase(fatcaStatus)) {
            fatcaIdentificationsItem.setIdenStatus("filled");
            log.info("Fatca Status : Filled");
        } else {
            fatcaIdentificationsItem.setIdenStatus("nofilled");
            log.info("Fatca Status : NoFilled");
        }

        fatca.setFatcaStatus(fatcaStatus);

        int lastItem = validateFatcaResponse.getSearchResponseDetails().size() - 1;
        ValidateFatcaSearchResponseDetailsItem responseDetails = validateFatcaResponse.getSearchResponseDetails().get(lastItem);

        idenDetail.setFatca(fatca);

        Crs crs = new Crs();
        crs.setCrsStatus(defaultString(responseDetails.getCRS()));
        crs.setCityOfBirth(defaultString(responseDetails.getCityOfBirth()));
        crs.setCountryOfBirth(defaultString(responseDetails.getCountryOfBirth()));

        List<CountryTax> countryTaxList = new ArrayList<>();
        if (nonNull(responseDetails.getCountryTaxs())) {
            for (ValidateFatcaCountryTaxsItem responseCountryTax : responseDetails.getCountryTaxs()) {
                CountryTax countryTax = new CountryTax();
                countryTax.setCountryCode(defaultString(responseCountryTax.getCountryCode()));
                countryTax.setForeignTIN(defaultString(responseCountryTax.getForeignTIN()));
                countryTax.setReason(defaultString(responseCountryTax.getReason()));
                countryTax.setRemark(defaultString(responseCountryTax.getRemark()));
                countryTaxList.add(countryTax);
            }
        }
        crs.setCountryTaxs(countryTaxList);
        IdentificationsItem identificationsItemCrs = new IdentificationsItem();
        identificationsItemCrs.setIdenType("crs");

        IdenDetail idenDetailCrs = new IdenDetail();
        idenDetailCrs.setCrs(crs);
        identificationsItemCrs.setIdenDetail(idenDetailCrs);

        fatcaIdentificationsItem.setIdenDetail(idenDetail);

        List<IdentificationsItem> identifications = caseInfo.getCustomerInfo().getIdentifications();
        identifications.add(fatcaIdentificationsItem);
        identifications.add(identificationsItemCrs);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        log.info("Fatca Connection Failed : " + "Require approval");
        ApprovalInfo approvalInfo = new ApprovalInfo();
        approvalInfo.setFunctionCode("FATCA");
        approvalInfo.setApprovalStatus("P");
        caseInfo.getApprovalInfo().add(approvalInfo);
    }
}