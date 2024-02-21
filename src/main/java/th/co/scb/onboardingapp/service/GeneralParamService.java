package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.ErrorCodes;
import th.co.scb.onboardingapp.exception.InternalErrorException;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.model.GeneralParam;
import th.co.scb.onboardingapp.model.entity.GeneralParameterEntity;
import th.co.scb.onboardingapp.repository.GeneralParameterRepository;

import java.util.*;

@Service
public class GeneralParamService {

    @Autowired
    private GeneralParameterRepository generalParameterRepository;

    private GeneralParam getGeneralParam(String key) {
        GeneralParameterEntity generalParameter = generalParameterRepository.findById(key).orElse(null);
        GeneralParam generalParam = new GeneralParam();

        try {
            if (generalParameter == null) {
                throw new NotFoundException(key + " GENERAL PARAMETERS");
            }
            generalParam.setType(generalParameter.getType());
            generalParam.setValue(new ObjectMapper().readValue(generalParameter.getValue(), new TypeReference<Map<String, Object>>() {
            }));
            generalParam.setServerOnly(generalParameter.isServerOnly());
        }  catch (JsonProcessingException e) {
            throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
        }
        return generalParam;

//        return mappingHelper.map(generalParameter, GeneralParam.class);
    }

    @Cacheable("getThresholdFaceVender")
    public GeneralParam getFaceVender() {
        return getGeneralParam("FACE_VENDER");
    }
    @Cacheable("getThresholdFaceNotAllowBranch")
    public Set<String> getFaceNotAllowBranch() {
        GeneralParam param = getGeneralParam("FACE_NOT_ALLOW_BRANCH");
        return new HashSet<>(param.getValueList());
    }
    @Cacheable("getThresholdFaceNotAllowRole")
    public Set<String> getFaceNotAllowRole() {
        GeneralParam param = getGeneralParam("FACE_NOT_ALLOW_ROLE");
        return new HashSet<>(param.getValueList());
    }
    @Cacheable("getPilotBranchGeneralParam")
    public Set<String> getPilotBranchGeneralParam() {
        GeneralParam param = getGeneralParam("BRANCH");
        return new HashSet<>(param.getValueList());
    }
    @Cacheable("getThresholdScoreGeneralParam")
    public GeneralParam getThresholdScoreGeneralParam() {
        return getGeneralParam("FC_SCORE_TS");
    }

    @Cacheable("getThresholdScoreGeneralParamPA")
    public GeneralParam getThresholdScoreGeneralParamPA() {
        return getGeneralParam("FC_SCORE_TS_PA");
    }

    @Cacheable("getFrontEndGeneralParam")
    public GeneralParam getFrontEndGeneralParam() {
        return getGeneralParam("FRONTEND");
    }

    public GeneralParam getConvertEpassbookFlag() {
        return getGeneralParam("CONVERT_EPASS_FLAG");
    }

    @Cacheable("getAllowLoginBranchGeneralParam")
    public Set<String> getAllowLoginBranchGeneralParam() {
        GeneralParam param = getGeneralParam("ALLOW_LOGIN_BRANCH");
        List<String> values = param.getValueList() == null ? Collections.emptyList() : param.getValueList();
        return new HashSet<>(values);
    }

    @Cacheable("getVerifyMultiLogin")
    public GeneralParam getVerifyMultiLogin() {
        return getGeneralParam("VERIFY_MULTI_LOGIN");
    }

    @Cacheable("getConsent")
    public GeneralParam getConsent() {
        return getGeneralParam("CONSENT");
    }

    @Cacheable("getConvertEpassbook")
    public GeneralParam getConvertEpassbook() {
        return getGeneralParam("CONVERT_EPASSBOOK");
    }

    @Cacheable("getAllowForeignerRoles")
    public GeneralParam getAllowForeignerRoles() { return getGeneralParam("ALLOW_FOREIGNER_ROLES"); }

    @Cacheable("getForeignerAllowProducts")
    public GeneralParam getForeignerAllowProducts() { return getGeneralParam("FOREIGNER_ALLOW_PRODUCTS"); }

    @Cacheable("getResizeImageFaceTec")
    public GeneralParam getResizeImageFaceTec() {
        return getGeneralParam("RESIZE_IMAGE_FACETEC");
    }

    @Cacheable("getPopupEmvRedlistEmail")
    public GeneralParam getPopupEmvRedlistEmail() { return getGeneralParam("POPUP_EMV_REDLIST_EMAIL"); }
}
