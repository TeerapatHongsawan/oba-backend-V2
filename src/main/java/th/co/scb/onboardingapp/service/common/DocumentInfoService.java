package th.co.scb.onboardingapp.service.common;

import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.DmsInfo;
import th.co.scb.onboardingapp.model.common.DocTypeCode;
import th.co.scb.onboardingapp.model.common.SubmissionApplicationForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentInfoService {

    public List<DmsInfo> buildDmsInfoList(List<DmsInfo> dmsInfoList, List<SubmissionApplicationForm> submissionApplicationFormList) {
        Map<String, DmsInfo> map = dmsInfoList.stream()
                .collect(Collectors.toMap(dmsInfo -> dmsInfo.getAccountNumber() != null && !"".equalsIgnoreCase(dmsInfo.getAccountNumber().trim()) ?
                        dmsInfo.getDocType() + "|" + dmsInfo.getAccountNumber() : dmsInfo.getDocType(), item -> item));

        for (SubmissionApplicationForm submissionApplicationForm : submissionApplicationFormList) {
            DmsInfo dmsInfo = new DmsInfo();
            dmsInfo.setUploadSessionId(submissionApplicationForm.getUploadSessionId());
            dmsInfo.setDocType(getDocTypeCode(submissionApplicationForm.getDocType()));
            dmsInfo.setDocNameTH(submissionApplicationForm.getDocNameTH());
            dmsInfo.setAccountNumber(submissionApplicationForm.getAccountNumber());
            if (submissionApplicationForm.getAccountNumber() != null) {
                map.put(getDocTypeCode(submissionApplicationForm.getDocType()) + "|" + submissionApplicationForm.getAccountNumber(), dmsInfo);
            } else {
                map.put(getDocTypeCode(submissionApplicationForm.getDocType()), dmsInfo);
            }
        }

        return new ArrayList<>(map.values());
    }

    private static String getDocTypeCode(String newDocTypeCode) {
        String oldDocTypeCode = DocTypeCode.DOC_TYPE_CODE_MAPPER.get(newDocTypeCode);
        if (oldDocTypeCode == null) {
            return newDocTypeCode;
        } else {
            return oldDocTypeCode;
        }
    }
}