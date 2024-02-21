package th.co.scb.onboardingapp.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import th.co.scb.entapi.deposits.model.GenDepAcctRequest;
import th.co.scb.entapi.otp.model.GenerationResponseDataModel;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponse;
import th.co.scb.entapi.sales_services.model.PromptPayLookupResponseV2;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MockModel {
    public AccessMappingTypeEntity accessMappingTypeEntity(){
        AccessMappingTypeEntity accessMappingTypeEntity = new AccessMappingTypeEntity();
        accessMappingTypeEntity.setType("branch");
        accessMappingTypeEntity.setFunctions("[\"investment\", \"travel_card\", \"payment_account_deduct\", \"payment_cash\", \"payment_atm\"]");
        accessMappingTypeEntity.setLoginActive(true);
        return accessMappingTypeEntity;
    }

    public AccessMappingChannelEntity accessMappingChannelEntity(){
        AccessMappingChannelEntity accessMappingChannelEntity = new AccessMappingChannelEntity();
        accessMappingChannelEntity.setChannel("2");
        accessMappingChannelEntity.setFunctions("[\"investment\", \"travel_card\", \"payment_account_deduct\", \"payment_cash\", \"payment_atm\"]");
        accessMappingChannelEntity.setLoginActive(true);
        return accessMappingChannelEntity;
    }

    public BranchEntity branchEntity(){
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setBranchId("0");
        branchEntity.setNameEn("s99999");
        branchEntity.setNameThai("เทส");
        branchEntity.setRegionCode("0");
        branchEntity.setChannelType("2");
        branchEntity.setBookingBranch(false);
        branchEntity.setOwnBranchOnly(false);
        return branchEntity;
    }

    public List<DataItem> getEmployeeBranch(){
        List<DataItem> result = new ArrayList<>();
        DataItem dataItem = new DataItem();
        dataItem.setText("เทส");
        dataItem.setCode("0");
        result.add(dataItem);
        return result;
    }

    public List<DataLoginSession> getDataLoginSessionList(){
        List<DataLoginSession> result = new ArrayList<>();
        DataLoginSession dataLoginSession = new DataLoginSession();
        dataLoginSession.setEmployeeId("s99999");
        dataLoginSession.setLastActivityTime(getLastActivityTime("2024-01-05 11:24:12"));
        dataLoginSession.setDeviceId("web");
        dataLoginSession.setStatus("active");
        dataLoginSession.setAppName("ONBD");
        dataLoginSession.setToken("192.168.1.103");
        result.add(dataLoginSession);
        return result;
    }
    public ObaAuthentication getObaAuthentication(){
        Set<String> resolvedLoginRoles = new HashSet<>();
        resolvedLoginRoles.add("payment_cash");
        resolvedLoginRoles.add("payment_atm");
        resolvedLoginRoles.add("payment_account_deduct");
        resolvedLoginRoles.add("investment");
        resolvedLoginRoles.add("branch");
        resolvedLoginRoles.add("travel_card");
        resolvedLoginRoles.add("facial");

        ObaAuthentication result = new ObaAuthentication("s99999", resolvedLoginRoles, "0",null, "ONBD", getStartbizUserProfilesDetail());
        return result;
    }

    public StartbizUserProfilesDetail getStartbizUserProfilesDetail(){
        StartbizUserProfilesDetail stb = new StartbizUserProfilesDetail();
        stb.setStaffID("s99999");

        stb.setUserRole("SUPPT");

        stb.setFirstNameTH("สาธิตา");

        stb.setLastNameTH("จันทรเจิด");

        stb.setFirstNameEN("Satitar");

        stb.setLastNameEN("Chantarachoet");

        stb.setBranchCode("7579");

        stb.setBranchNameTH("Digital Change & Knowledge Management Division");

        stb.setUserPosition("Operations Specialist");

        stb.setOcCode("7569");

        stb.setManagerID("s28282");

        stb.setEmail("satitar.chantarachoet@scbtechx.io");

        stb.setCorpCode("CG26");

        stb.setPrefixTH("นางสาว");

        stb.setSegCode("SUPPT");

        stb.setPrefixEN("Miss");

        stb.setSourceSys("Dummy");

        stb.setAccountOwner(false);

        stb.setAccountOwnerManager(false);

        stb.setOcOwner(false);

        stb.setManager(false);

        stb.setKycOnly(false);

        stb.setCorpTTL("Officer 1");

        stb.setJobCode("90003");

        stb.setMultiRole("[\\\"SUPPT\\\"]");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        String dateInString = "10:55:46";
        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
        }
        stb.setTimeStampCreated(date);
        return stb;
    }

    public List<BranchEntity> branchEntityList(){
        List<BranchEntity> branchEntityList = new ArrayList<>();
        branchEntityList.add(branchEntity());
        return branchEntityList;
    }

    public EmployeeEntity getemployeeEntity(){
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId("s99999");
        employeeEntity.setFirstNameEn("test");
        employeeEntity.setLastNameEn("test");
        employeeEntity.setFirstNameThai("เทส");
        employeeEntity.setLastNameThai("เทส");
        employeeEntity.setLicenseId("0");
        employeeEntity.setOcCode("0");
        employeeEntity.setOcNameEn("test");
        employeeEntity.setOcNameTh("เทส");
        employeeEntity.setEmail("s99999@scb.co.th");
        employeeEntity.setPositionName("Maker");
        employeeEntity.setSamEmployeeId("0");
        employeeEntity.setSamFirstNameEn("test");
        employeeEntity.setSamLastNameEn("test");
        employeeEntity.setSamFirstNameThai("เทส");
        employeeEntity.setSamLastNameThai("เทส");
        employeeEntity.setSamOcCode("0");
        employeeEntity.setSamOcNameEn("test");
        employeeEntity.setSamOcNameTh("เทส");
        employeeEntity.setSamEmail("s99999@scb.co.th");
        return employeeEntity;
    }

    public LoginBranchEntity loginBranchEntity(){
        LoginBranchEntity loginBranchEntity = new LoginBranchEntity();
        loginBranchEntity.setBranchId("0");
        loginBranchEntity.setEmployeeId("s99999");
        loginBranchEntity.setApprovalBranchId("PRVT/");
        loginBranchEntity.setRoles("[\"branch\"]");
        return loginBranchEntity;
    }

    public LoginBranchInfo loginBranchInfo(){
        LoginBranchInfo loginBranchInfo = new LoginBranchInfo();
        loginBranchInfo.setEmployeeId("s99999");
        loginBranchInfo.setBranchId("0");
        loginBranchInfo.setApprovalBranchId("PRVT/");
        Set<String> role = new HashSet<>();
        role.add("branch");
        loginBranchInfo.setRoles(role);
        return loginBranchInfo;
    }

    public OrganizationEntity organizationEntity(){
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setOcNameEn("s99999");
        organizationEntity.setOcNameTh("เทส");
        organizationEntity.setOcCode("0");
        return organizationEntity;
    }

    public List<OrganizationEntity> organizationEntityList(){
        List<OrganizationEntity> organizationEntityList = new ArrayList<>();
        organizationEntityList.add(organizationEntity());
        return organizationEntityList;
    }

    public LoginSessionEntity loginSessionEntity(){
        LoginSessionEntity loginSessionEntity = new LoginSessionEntity();
        loginSessionEntity.setEmployeeId("s99999");
        loginSessionEntity.setLastActivityTime(getLastActivityTime("2024-01-05 11:24:12"));
        loginSessionEntity.setDeviceId("web");
        loginSessionEntity.setStatus("active");
        loginSessionEntity.setAppName("ONBD");
        loginSessionEntity.setToken("192.168.1.103");
        return loginSessionEntity;
    }

    public List<LoginSessionEntity> loginSessionEntityList(){
        List<LoginSessionEntity> loginSessionEntityList = new ArrayList<>();
        loginSessionEntityList.add(loginSessionEntity());
        return loginSessionEntityList;
    }
    public List<AuthorizedLevel> authorizedLevelList(){
        List<AuthorizedLevel> authorizedLevelList = new ArrayList<>();
        authorizedLevelList.add(AuthorizedLevel.SSC);
        return authorizedLevelList;
    }

    public GeneralParameterEntity generalParameterEntityGetFaceNotAllowBranch(){
        GeneralParameterEntity generalParameter = new GeneralParameterEntity();
        generalParameter.setValue("{\"list\": [\"\"]}");
        generalParameter.setType("FACE_NOT_ALLOW_BRANCH");
        generalParameter.setServerOnly(true);
        return generalParameter;
    }

    public Set<String> generalParameterGetFaceNotAllowBranch(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        List<String> listValue = new ArrayList<>();
        listValue.add("");
        value.put("list", listValue);
        generalParameter.setValue(value);
        generalParameter.setType("FACE_NOT_ALLOW_BRANCH");
        generalParameter.setServerOnly(true);
        return new HashSet<>(generalParameter.getValueList());
    }

    public GeneralParameterEntity generalParameterEntityGetFaceNotAllowRole(){
        GeneralParameterEntity generalParameter = new GeneralParameterEntity();
        generalParameter.setValue("{\"list\": [\"bulk\",\"dsa\"]}");
        generalParameter.setType("FACE_NOT_ALLOW_ROLE");
        generalParameter.setServerOnly(true);
        return generalParameter;
    }
    public Set<String> generalParameterGetFaceNotAllowRole(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        List<String> listValue = new ArrayList<>();
        listValue.add("bulk");
        listValue.add("dsa");
        value.put("list", listValue);
        generalParameter.setValue(value);
        generalParameter.setType("FACE_NOT_ALLOW_ROLE");
        generalParameter.setServerOnly(true);
        return new HashSet<>(generalParameter.getValueList());
    }
    public GeneralParameterEntity generalParameterEntityGetAllowLoginBranchGeneralParam(){
        GeneralParameterEntity generalParameter = new GeneralParameterEntity();
        generalParameter.setValue("{\"list\": [\"\"]}");
        generalParameter.setType("ALLOW_LOGIN_BRANCH");
        generalParameter.setServerOnly(true);
        return generalParameter;
    }

    public Set<String> generalParameterGetAllowLoginBranchGeneralParam(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        List<String> listValue = new ArrayList<>();
        listValue.add("");
        value.put("list", listValue);
        generalParameter.setValue(value);
        generalParameter.setType("ALLOW_LOGIN_BRANCH");
        generalParameter.setServerOnly(true);
        return new HashSet<>(generalParameter.getValueList());
    }

    public GeneralParameterEntity generalParameterEntityGetVerifyMultiLogin(){
        GeneralParameterEntity generalParameter = new GeneralParameterEntity();
        generalParameter.setValue("{\"isVerify\": true}");
        generalParameter.setType("VERIFY_MULTI_LOGIN");
        generalParameter.setServerOnly(false);
        return generalParameter;
    }

    public GeneralParam generalParameterGetVerifyMultiLogin(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        value.put("isVerify", true);
        generalParameter.setValue(value);
        generalParameter.setType("VERIFY_MULTI_LOGIN");
        generalParameter.setServerOnly(false);
        return generalParameter;
    }
    public LoginRequest loginRequest(){
        LoginRequest loginRequest = new LoginRequest("s99999", "scb1234!", "ONBD", "---SERVER-VERSION---", "web", "192.168.1.103", "test", "10.248.2.140", true);
        loginRequest.setSessionId("asdasdsad");
        loginRequest.setToken("10.248.2.140");
        loginRequest.setIsConfirmLoginDuplicateUser(true);
        return loginRequest;
    }

    public List<OrganizationEntity> getBranch(){
        List<OrganizationEntity> branches = new ArrayList<>();
        OrganizationEntity organization = new OrganizationEntity();
        organization.setOcCode("0");
        organization.setOcNameEn("s99999");
        organization.setOcNameTh("เทส");
        branches.add(organization);
        return branches;
    }

    public List<LoginSessionEntity> getMultiLoginList(){
        List<LoginSessionEntity> multiLoginList = new ArrayList<>();
        LoginSessionEntity loginSessionEntity = new LoginSessionEntity();
        loginSessionEntity.setEmployeeId("s99999");
        loginSessionEntity.setLastActivityTime(getLastActivityTime("2024-01-05 11:24:12"));
        loginSessionEntity.setDeviceId("web");
        loginSessionEntity.setStatus("active");
        loginSessionEntity.setAppName("ONBD");
        loginSessionEntity.setToken("192.168.1.103");
        multiLoginList.add(loginSessionEntity);
        return multiLoginList;
    }

    public LocalDateTime getLastActivityTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
        return dateTime;
    }
    public ValidateSessionAliveRequest getValidateSessionAliveRequest(){
        ValidateSessionAliveRequest   validateRequest = new ValidateSessionAliveRequest();
        validateRequest.setUsername("s99999");
        validateRequest.setDeviceId("ONBD");
        validateRequest.setDeviceId("web");
        return validateRequest;
    }

    public ObaAuthentication getObaAuthentication(String branchId,String caseId){

        ObaAuthentication result = new ObaAuthentication("s99999", getRole(), branchId,caseId, "ONBD", getStartbizUserProfilesDetail());
        return result;
    }

    public TokenInfo getToken(ObaAuthentication authentication){


        long expiresIn = 1200 * 1000;
        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("scopes", authentication.getRoles())
                .claim("case", authentication.getCaseId())
                .claim("branch", authentication.getBranchId())
                .claim("app", authentication.getAppName())
                .setExpiration(new Date(System.currentTimeMillis() + expiresIn))
                .signWith(SignatureAlgorithm.HS256, "gv625szx8UnAtcsXVup3Q2YaUFOPZdQyDPoIXSoy0kESXKrR+6hFJVUWONcAhXWC")
                .compact();

        return new TokenInfo(token, "Bearer", expiresIn);
    }
    public Set<String> getRole() {
        Set<String> resolvedLoginRoles = new HashSet<>();
        resolvedLoginRoles.add("payment_cash");
        resolvedLoginRoles.add("payment_atm");
        resolvedLoginRoles.add("payment_account_deduct");
        resolvedLoginRoles.add("investment");
        resolvedLoginRoles.add("branch");
        resolvedLoginRoles.add("travel_card");
        resolvedLoginRoles.add("facial");
        return resolvedLoginRoles;
    }

    public EmployeeMe getemployeeMe(){
        EmployeeMe employeeMe = new EmployeeMe();
        employeeMe.setEmployeeId("s99999");
        employeeMe.setFirstNameEn("test");
        employeeMe.setLastNameEn("test");
        employeeMe.setFirstNameThai("เทส");
        employeeMe.setLastNameThai("เทส");
        employeeMe.setOcCode("0");
        employeeMe.setOcName("test");
        employeeMe.setOcNameEn("s99999");
        employeeMe.setSamOcCode("0");
        employeeMe.setBranchId("0");
        employeeMe.setBranchNameThai("เทส");
        employeeMe.setBranchNameEn("s99999");
        employeeMe.setApprovalBranchId("PRVT/");
        employeeMe.setRoles(getRole());
        return employeeMe;
    }

    public EmployeeInfo getemployeeInfo() {
        EmployeeInfo empInfo = new EmployeeInfo();
        empInfo.setEmployeeId("s99999");
        empInfo.setFirstNameEn("test");
        empInfo.setLastNameEn("test");
        empInfo.setFirstNameThai("เทส");
        empInfo.setLastNameThai("เทส");
        return  empInfo;
    }

    public GeneralParam getAllowForeignerRoles(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        List<String> listValue = new ArrayList<>();
        listValue.add("branch");
        value.put("list", listValue);
        generalParameter.setValue(value);
        generalParameter.setType("ALLOW_FOREIGNER_ROLES");
        generalParameter.setServerOnly(false);
        generalParameter.getValueList().add("branch");
        return generalParameter;
    }
    public GeneralParam getForeignerAllowProducts(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        List<String> listValue = new ArrayList<>();
        listValue.add("ST02SE02");
        value.put("list", listValue);
        generalParameter.setValue(value);
        generalParameter.setType("FOREIGNER_ALLOW_PRODUCTS");
        generalParameter.setServerOnly(false);
        generalParameter.getValueList().add("ST02SE02");
        return generalParameter;
    }

    public GeneralParam getFrontEndGeneralParam(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
        value.put("manual_keyin", true);
        value.put("service_only", true);
        generalParameter.setValue(value);
        generalParameter.setType("FRONTEND");
        generalParameter.setServerOnly(false);
        return generalParameter;
    }

    public GeneralParam getPopupEmvRedlistEmail(){
        GeneralParam generalParameter = new GeneralParam();
        Map<String, Object> value = new HashMap<>();
//        value.put("manual_keyin", true);
//        value.put("service_only", true);
        generalParameter.setValue(value);
        generalParameter.setType("POPUP_EMV_REDLIST_EMAIL");
        generalParameter.setServerOnly(false);
        return generalParameter;
    }
    public FaceCompareResponse mockScore() {
        String matchLevel = "3";
        FaceCompareMatchLevel faceCompareMatchLevel = new FaceCompareMatchLevel();
        faceCompareMatchLevel.setMatchValue("2");
        faceCompareMatchLevel.setVendor("FaceTec");
        faceCompareMatchLevel.setMatchLevel(matchLevel);
        FaceCompareResponse result = new FaceCompareResponse();
        result.setResult(faceCompareMatchLevel.getMatchValue());
        result.setMatchLevel(faceCompareMatchLevel.getMatchLevel());
        result.setMatchScore("4");
        return  result;
    }

    public UploadRequest getDataUpload() {

        UploadRequest request = new UploadRequest();
        request.setDocObj("iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAFUlEQVR42mP8z8BQz0AEYBxVSF+FABJADveWkH6oAAAAAElFTkSuQmCC");
        request.setMimeType("image/png");
        return  request;
    }
    public GenDepAcctRequest getAccountRequest(){
        GenDepAcctRequest request = new GenDepAcctRequest();
        request.setAccountCurrency("764");
        request.setAccountType("2");
        request.setHomeBranch("0111");
        request.setProcessingBranch("0111");
        return request;
    }

    public PromptPayProxy getPromptPayProxy(){
        PromptPayProxy request = new PromptPayProxy();
        List<Proxy> proxies = new ArrayList<>();
        request.setProxies(proxies);
        request.getProxies().add(new Proxy( "2222213854241","CID"));
        request.getProxies().add(new Proxy( "0905555555","MOB"));
        return request;
    }

    public PromptPayLookupResponseV2 getPromptPayResponse(){
        PromptPayLookupResponseV2 response = new PromptPayLookupResponseV2();
        PromptPayLookupResponse item[] = new PromptPayLookupResponse[1];
        item[0] = new PromptPayLookupResponse();
        item[0].setProxyID("2222213854241");
        item[0].setProxyStatus("A");
        item[0].setProxyType("CID");
        item[0].setBankCode("SCB");
        response.setLookupResponses(item);
        return response;
    }

    public List<DebitCardInfo>  getDebitCardResponse(){
        List<DebitCardInfo> response = new ArrayList<>();
        DebitCardInfo item = new DebitCardInfo();
        item.setProductId("1");
        item.setProductType("DEBIT");
        item.setProductCardType("TH STD DEBIT");
        item.setStockCode("TH01");
        item.setProductNameTH("บัตรเดบิต เล็ทส์ เอสซีบี");
        item.setProductNameEN("LET'S SCB Debit Card");
        response.add(item);
        return response;
    }

    public List<WatchlistResponse>  getWatchlistFailResponse(){
        List<WatchlistResponse> response = new ArrayList<>();
        WatchlistResponse item = new WatchlistResponse();
        item.setResultCode("400");
        item.setResultReason("FAILURE");
        response.add(item);
        return response;
    }

    public List<WatchlistResponse>  getWatchlistSuccessResponse(){
        List<WatchlistResponse> response = new ArrayList<>();
        WatchlistResponse item = new WatchlistResponse();
        item.setResultCode("200");
        item.setResultReason("SUCCESS");
        response.add(item);
        return response;
    }

    public WatchlistRequest[]  getWatchlistRequest(){
        WatchlistRequest request[] = new WatchlistRequest[1];
        request[0] = new WatchlistRequest();
        request[0].setRelation(Relation.SELF);
        request[0].setThaiTitle("");
        request[0].setIdNumber("2222213854241");
        request[0].setThaiName("");
        request[0].setEngName("");

        return request;
    }

    public FaceCompareResponse getFaceCompareResponse(){
        FaceCompareResponse response = new FaceCompareResponse();
        response.setMatchLevel("3");
        response.setMatchScore("4");
        return response;
    }

    public GenerateOtpResponse getOTPResponse(){
        GenerateOtpResponse response = new GenerateOtpResponse();
        response.setReference("KM9X");
        response.setValidDuration(300);
        return response;
    }
    public GenerateOtpRequest getOTPRequest(){
        GenerateOtpRequest data = new GenerateOtpRequest();
        data.setOtpMobile("0933333333");
        data.setOtpType("ACCTOPEN");
        return data;
    }

    public GenerationResponseDataModel getOTPApiResponse(){
        GenerationResponseDataModel data = new GenerationResponseDataModel();
        data.setTokenUUID("J4enklp-Mz");
        data.setReference("TEST");
        data.setValidDuration(300);
        return data;
    }
    public CustomerInfo getCustomerInfo(){
        List<CustomerContactChannel> contactChannels = new ArrayList<>();
        CustomerContactChannel contactChannel = new CustomerContactChannel();
        contactChannel.setContactNumber("0864035009");
        contactChannel.setContactTypeCode("002");
        contactChannels.add(contactChannel);
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setContactChannels(contactChannels);
        return customerInfo;
    }

}
