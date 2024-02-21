package th.co.scb.onboardingapp.service.cseretrieval;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.IdentificationsItem;
import th.co.scb.scbeasy.ScbEasyApi;
import th.co.scb.scbeasy.model.GetChannelStatusRequest;
import th.co.scb.scbeasy.model.GetChannelStatusResponse;
import th.co.scb.scbeasy.model.Requestor;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@CaseContinueExistingQualifier
@ForeignerCaseContinueExistingQualifier
public class EasyAppRetrievalPlugin extends BaseRetrievalPlugin<GetChannelStatusResponse> {
    private static final String EASYAPP_STATUS_NOT_AVAILABLE = "N";
    private static final String EASYAPP_STATUS_AVAILABLE = "Y";
    private static final String EASYAPP_STATUS_SYSTEM_ERROR = "U";

    @Autowired
    private ScbEasyApi easyAppApi;

    @Override
    protected CompletableFuture<GetChannelStatusResponse> retrieveCase(CaseInfo caseInfo) {
        GetChannelStatusRequest getEasyAppStatusRequest = new GetChannelStatusRequest();
        Requestor requestor = new Requestor();
        requestor.setBranchId(caseInfo.getBranchId());
        requestor.setUserId(caseInfo.getEmployeeId());
        getEasyAppStatusRequest.setRequestor(requestor);

        return easyAppApi.getChannelStatusAsync(getEasyAppStatusRequest, ObaHelper.formatRmId(caseInfo.getReferenceId()));
    }

    @Override
    protected void updateCase(CaseInfo caseInfo, GetChannelStatusResponse result) {
        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("easyApp_register");
        switch (result.getStatus().getCode()) {
            case 1000:
            case 1020:
                identificationsItem.setIdenStatus(EASYAPP_STATUS_NOT_AVAILABLE);
                log.info("EasyApp Status : NO");
                break;
            case 1899:
            case 1999:
                identificationsItem.setIdenStatus(EASYAPP_STATUS_SYSTEM_ERROR);
                log.info("EasyApp Status : ERROR");
                break;
            default:
                identificationsItem.setIdenStatus(EASYAPP_STATUS_AVAILABLE);
                log.info("EasyApp Status : YES");
                break;
        }

        caseInfo.getCustomerInfo().getIdentifications().add(identificationsItem);
    }

    @Override
    protected void handleError(CaseInfo caseInfo, Throwable ex) {
        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenType("easyApp_register");
        identificationsItem.setIdenStatus(EASYAPP_STATUS_SYSTEM_ERROR);
        caseInfo.getCustomerInfo().getIdentifications().add(identificationsItem);
        log.error("EasyApp ERROR", ex);
    }
}