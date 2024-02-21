package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.detica.model.DeticaValidationRequest;
import th.co.scb.entapi.detica.model.DeticaValidationResponse;
import th.co.scb.facetech.FaceTechApi;
import th.co.scb.facetech.model.FaceVerificationData;
import th.co.scb.facetech.model.FaceVerificationRequest;
import th.co.scb.facetech.model.FaceVerificationResponse;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.ImageResizer;
import th.co.scb.onboardingapp.helper.IndiHelper;
import th.co.scb.onboardingapp.helper.ObaHelper;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.DocumentBlobEntity;
import th.co.scb.onboardingapp.repository.DocumentBlobJpaRepository;
import th.co.scb.vaultFaceTech.model.LoginResponse;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FaceCompareService {

    @Autowired
    private FaceTechApi faceTechApi;

    @Autowired
    private CaseLibraryService caseLibraryService;

    @Autowired
    private DocumentBlobJpaRepository documentBlobRepository;

    @Autowired
    private TJLogProcessor tjLogProcessor;

    @Autowired
    private FaceTechSecurityService faceTechSecurityService;

    @Autowired
    private FaceCompareMatchLevelService faceCompareMatchLevelService;

    @Autowired
    private FaceTechVaultService faceTechVaultService;

    @Autowired
    private ToggleService toggleService;

    @Autowired
    private GeneralParamService generalParamService;
    @Autowired
    FaceTechApiService faceTechApiService;
    @Autowired
    EntApiConfig entApiConfig;
    @Autowired
    RestTemplate restTemplate;
    @Value("${ent-api.mode:true}")
    boolean isEntApiMode;
    public FaceCompareResponse getFaceVerificationResponse(FaceCompareRequest data, ObaAuthentication auth) {
        DocumentBlobEntity docBlob = documentBlobRepository.findById(data.getUploadSessionId()).orElse(null);
        if (docBlob == null) {
            throw new NotFoundException("Document");
        }

        CaseInfo caseInfo = caseLibraryService.getCase(auth.getCaseId());

        return sendPhotoToFaceTech(caseInfo, docBlob, data);
    }

    private FaceCompareResponse sendPhotoToFaceTech(CaseInfo caseInfo, DocumentBlobEntity docBlob, FaceCompareRequest data) {

        if (nonNull(data.getHackFacialScore())) {
            return resultForHackFacialScore(caseInfo, data.getHackFacialScore());
        }

        CustomerInfo custInfo = caseInfo.getCustomerInfo();
        String photo = custInfo.getPhoto().split(",\\s*", 2)[1];
        byte[] dipChipPhoto = Base64.getDecoder().decode(photo);

        FaceVerificationRequest faceVerificationRequest = getFaceVerificationRequest(docBlob, dipChipPhoto, data.getDeviceId());

        String rmId = nonNull(caseInfo.getReferenceId()) ? ObaHelper.formatRmId(caseInfo.getReferenceId()) : UUID.randomUUID().toString();
        FaceVerificationResponse response = faceTechApiService.faceCompareVerification(faceVerificationRequest, rmId);
        FaceVerificationData faceVerificationData = response.getData();

        List<FaceCompareMatchLevel> faceCompareMatchLevels = faceCompareMatchLevelService.faceCompareMatchLevels();
        FaceCompareMatchLevel faceCompareMatchLevel = getFaceCompareMatchLevel(faceVerificationData, faceCompareMatchLevels);

        ToggleConfig toggleConfigs = toggleService.toggles().stream()
                .filter(i -> "RESIZE_IMAGE_FACETEC".equalsIgnoreCase(i.getToggleName()))
                .findFirst().orElse(null);

        if (nonNull(toggleConfigs) && "Y".equals(toggleConfigs.getToggleValue())) {
            if (shouldResizeImage(faceVerificationData, faceCompareMatchLevel)) {
                response = resizeImageFaceCompare(docBlob, dipChipPhoto, rmId, response, data.getDeviceId());
            }
        }

        return result(caseInfo, response.getData(), faceCompareMatchLevels);
    }

    private FaceVerificationResponse resizeImageFaceCompare(DocumentBlobEntity docBlob, byte[] dipChipPhoto, String rmId, FaceVerificationResponse response, String deviceId) {
        try {
            GeneralParam generalParam = generalParamService.getResizeImageFaceTec();
            int newWidth = (int) generalParam.getValue().get("X-axis");
            int newHeight = ImageResizer.getNewHeight(dipChipPhoto, newWidth);

            byte[] resizeDipChipPhoto = ImageResizer.resizeImage(dipChipPhoto, newWidth, newHeight);

            FaceVerificationRequest faceVerificationRequest = getFaceVerificationRequest(docBlob, resizeDipChipPhoto, deviceId);
            response = faceTechApiService.faceCompareVerification(faceVerificationRequest, rmId);
        } catch (Exception ex) {
            log.info("Resize image failed. {}", ex.getMessage());
        }

        return response;
    }

    private FaceVerificationRequest getFaceVerificationRequest(DocumentBlobEntity docBlob, byte[] dipChipPhoto, String deviceId) {
        FaceVerificationRequest faceVerificationRequest = new FaceVerificationRequest();
        faceVerificationRequest.setUserAgent(deviceId);

        byte[] iv = faceTechSecurityService.generateIv();
        byte[] iv2 = faceTechSecurityService.generateIv();
        SecretKey secretKey = genKey();

        faceVerificationRequest.setImage1(faceTechSecurityService.encryptImage(secretKey, docBlob.getBinaryData(), iv));
        faceVerificationRequest.setImage1EncryptionIv(Base64.getEncoder().encodeToString(iv));

        faceVerificationRequest.setImage2(faceTechSecurityService.encryptImage(secretKey, dipChipPhoto, iv2));
        faceVerificationRequest.setImage2EncryptionIv(Base64.getEncoder().encodeToString(iv2));
        return faceVerificationRequest;
    }

    private FaceCompareResponse result(CaseInfo caseInfo, FaceVerificationData faceVerificationData, List<FaceCompareMatchLevel> faceCompareMatchLevels) {
        FaceCompareMatchLevel faceCompareMatchLevel = getFaceCompareMatchLevel(faceVerificationData, faceCompareMatchLevels);
        FaceCompareResponse result = new FaceCompareResponse();

        if (nonNull(faceCompareMatchLevel)) {
            boolean isPass = validateScore(faceCompareMatchLevel);
            tjLogProcessor.logFaceRecognitionTxn(caseInfo, faceCompareMatchLevel.getMatchLevel(), isPass);

            result.setResult(faceCompareMatchLevel.getMatchValue());
            result.setMatchLevel(faceCompareMatchLevel.getMatchLevel());
            result.setMatchScore(getMatchScore(faceVerificationData));
        }
        return result;
    }

    private FaceCompareResponse resultForHackFacialScore(CaseInfo caseInfo, String hackFacialScore) {
        String matchLevel = getMatchLevel(hackFacialScore);

        FaceCompareMatchLevel faceCompareMatchLevel = faceCompareMatchLevelService.mapFaceCompareMatchLevel(matchLevel);
        tjLogProcessor.logFaceRecognitionTxn(caseInfo, matchLevel, validateScore(faceCompareMatchLevel));

        FaceCompareResponse result = new FaceCompareResponse();
        result.setResult(faceCompareMatchLevel.getMatchValue());
        result.setMatchLevel(faceCompareMatchLevel.getMatchLevel());
        result.setMatchScore("");

        return result;
    }

    private String getMatchScore(FaceVerificationData faceVerificationData) {
        return nonNull(faceVerificationData) && nonNull(faceVerificationData.getMatchLevel()) ? faceVerificationData.getMatchScore() : "fail";
    }

    private boolean validateScore(FaceCompareMatchLevel faceCompareMatchLevel) {
        return "3".equalsIgnoreCase(faceCompareMatchLevel.getMatchValue());
    }

    private String getMatchLevel(String hackFacialScore) {
        List<FaceCompareMatchLevel> faceCompareMatchValue = faceCompareMatchLevelService.mapFaceCompareMatchValue(hackFacialScore);
        return faceCompareMatchValue.get(0).getMatchLevel();
    }

    private SecretKey genKey() {
        String faceTechKey = faceTechVaultService.getFaceTechKey();
        return faceTechSecurityService.convertStringToSecretKey(faceTechKey);
    }

    private FaceCompareMatchLevel getFaceCompareMatchLevel(FaceVerificationData faceVerificationData, List<FaceCompareMatchLevel> faceCompareMatchLevels) {
        String matchLevel = nonNull(faceVerificationData) && nonNull(faceVerificationData.getMatchLevel()) ? faceVerificationData.getMatchLevel().toString() : "fail";
        return faceCompareMatchLevels.stream()
                .filter(i -> matchLevel.equalsIgnoreCase(i.getMatchLevel()))
                .findFirst().orElse(null);
    }

    private boolean shouldResizeImage(FaceVerificationData faceVerificationData, FaceCompareMatchLevel faceCompareMatchLevel) {
        return nonNull(faceVerificationData) && nonNull(faceVerificationData.getMatchLevel()) && !validateScore(faceCompareMatchLevel);
    }





}
