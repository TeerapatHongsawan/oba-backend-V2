package th.co.scb.onboardingapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import th.co.scb.onboardingapp.config.security.ObaAuthentication;
import th.co.scb.onboardingapp.exception.*;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.*;
import th.co.scb.onboardingapp.service.api.SessionApiService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ObaAuthenticationProvider {

    @Autowired
    private LdapValidator ldapValidator;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SessionApiService sessionApiService;

    public List<DataItem> getEmployeeBranch(String username, String password) {
        ldapValidator.validate(username, password);

        List<OrganizationEntity> branches = employeeService.getBranches(username);


        return branches.stream()
                .map(it -> {
                    DataItem dataItem = new DataItem();
                    dataItem.setCode(it.getOcCode());
                    dataItem.setText(it.getOcNameTh());
                    return dataItem;
                })
                .collect(Collectors.toList());
    }

    public void initialSessionToThirdApp(LoginRequest loginRequest, ObaAuthentication auth, TokenInfo token){
        TokenInitialSessionRequest tokenModel = new TokenInitialSessionRequest();
        tokenModel.setIpAddr(loginRequest.getDeviceIp());
        tokenModel.setToken(token.getAccessToken());
        String json = "";
        try{
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            json = ow.writeValueAsString(tokenModel);
        } catch (JsonProcessingException e){
            throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
        }
        InitSessionRequest initSessionRequest = new InitSessionRequest();
        initSessionRequest.setUsername(loginRequest.getUsername());
        initSessionRequest.setToken(json);
        if(!auth.getRoles().isEmpty()){
            boolean isInitialSessionToIOnboard = sessionApiService.initialSessionToIOnboard(initSessionRequest);
            if(!isInitialSessionToIOnboard){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
        if(auth.getStb() != null){
            boolean isInitialSessionToStartBiz = sessionApiService.initialSessionToStartBiz(initSessionRequest);
            if(!isInitialSessionToStartBiz){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
    }

    public void validateSessionToThirdApp(String userName, ObaAuthentication auth, TokenInfo token){
        InitSessionRequest initSessionRequest = new InitSessionRequest();
        initSessionRequest.setUsername(userName);
        initSessionRequest.setToken(token.getAccessToken());
        if(!auth.getRoles().isEmpty()){
            boolean isInitialSessionToIOnboard = sessionApiService.validateSessionToIOnboard(initSessionRequest);
            if(!isInitialSessionToIOnboard){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
        if(auth.getStb() != null){
            boolean isInitialSessionToStartBiz = sessionApiService.validateSessionToStartBiz(initSessionRequest);
            if(!isInitialSessionToStartBiz){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
    }

    public void logoutSessionToThirdApp(String userName, ObaAuthentication auth){
        InitSessionRequest initSessionRequest = new InitSessionRequest();
        initSessionRequest.setUsername(userName);
        initSessionRequest.setToken("");
        if(!auth.getRoles().isEmpty()){
            boolean isInitialSessionToIOnboard = sessionApiService.logoutSessionToIOnboard(initSessionRequest);
            if(!isInitialSessionToIOnboard){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
        if(auth.getStb() != null){
            boolean isInitialSessionToStartBiz = sessionApiService.logoutSessionToStartBiz(initSessionRequest);
            if(!isInitialSessionToStartBiz){
                throw new UnauthorizedException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
            }
        }
    }

    public List<DataLoginSession> getLoginSession(String username, String appName) {
        List<LoginSessionEntity> multiLoginList = employeeService.getLoginSession(username, appName);

        return multiLoginList.stream()
                .map(it -> {
                    DataLoginSession dataLoginSession = new DataLoginSession();
                    dataLoginSession.setEmployeeId(it.getEmployeeId());
                    dataLoginSession.setLastActivityTime(it.getLastActivityTime());
                    dataLoginSession.setDeviceId(it.getDeviceId());
                    dataLoginSession.setStatus(it.getStatus());
                    dataLoginSession.setAppName(it.getAppName());
                    dataLoginSession.setToken(it.getToken());
                    return dataLoginSession;
                })
                .collect(Collectors.toList());
    }

    public ObaAuthentication authenticate(String username, String loginToBranchId, String appName, StartbizUserProfilesDetail stb) {
    	// key methods inside this method would throw UnauthorizedException in case it fail to authenticate.
    	
    	// check if username exists in EMPLOYEE table
        throwNotExistingEmployee(username);
        
        Set<String> resolvedLoginRoles = resolveLoginRoles(username, loginToBranchId, appName);

        return new ObaAuthentication(username, resolvedLoginRoles, loginToBranchId, null, appName, stb);
    }

    @Autowired
    private BranchMasterDataService branchMasterDataService;    
    
    @Autowired
    private Environment environment;    
    
	private Set<String> resolveLoginRoles(String username, String loginToBranchId, String appName) {
		// load LoginBranch from LOGIN_BRANCH table, prepare LoginBranchInfo
        LoginBranchInfo loginBranchInfo = this.getLoginBranchInfo(username, loginToBranchId);

        // combination of... 
        // case role ("maker","checker") can be both at the same time
        // user type ("branch","wealth","wealth_specialist","dsa","private","ssme","bulk") only one at a time
        Set<String> loginBranchRoles = loginBranchInfo.getRoles();

        loginBranchRoles.forEach(role -> log.debug("User has role:-> {}" , role));
        
        // IOVSIP: iProfile only allow for branch user
		ejectNonBranchUserLoginToIPro(username, appName, loginBranchRoles);        
        
        // list functions from loginBranch's roles
		// e.g. payment_cash, payment_atm, payment_account_deduct, investment, travel_card
        Set<String> functionsFromLoginBranchRoles = this.getFunctionsByTypes(loginBranchRoles);
        BranchEntity loginToBranch = this.branchMasterDataService.branchMap().get(loginToBranchId);
        // assume "oc" first
        // all non branch user will have "oc" as channelType
        String channelType = "oc";
        // use specific channelType if loginBranch exists
        if (loginToBranch != null) {        	
        	channelType = loginToBranch.getChannelType();
        }
        
        // list functions from branch's channel
        Set<String> functionsFromBranchChannel = this.getFunctionsByBranchChannel(channelType);

        // find functions common between branchChannel and loginBranchRoles 
        Set<String> resolvedFunctions = Sets.intersection(functionsFromBranchChannel, functionsFromLoginBranchRoles);

        Set<String> resolvedRoles = new HashSet<>(Sets.union(loginBranchRoles, resolvedFunctions));
        
        // user must have non empty roles
        ejectEmptyRoleUser(username, resolvedRoles);        
        
        boolean shouldGrantFacialFunction = this.shouldGrantFacialFunction(loginToBranchId, loginBranchRoles);               
		if (shouldGrantFacialFunction) {
        	resolvedRoles.add("facial");
        }
                
        // grant hack role if hack present in environment
        if (environment.acceptsProfiles(Profiles.of("hack"))) {
            resolvedRoles.add("hack");
        }
                    
		return resolvedRoles;
	}

	private void ejectEmptyRoleUser(String username, Set<String> resolvedRoles) {
        if (resolvedRoles.isEmpty()) {
        	UnauthorizedException unauthorizedException = new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        	OriginalExceptionDetail originalExceptionDetail = OriginalExceptionDetail.builder()
        			.originateFrom(ExceptionOrigins.INDI.code)
        				.onOperation("roles.isEmpty")
        					.resultInCode("role_is_empty")
        						.withDescription(String.format(" %s don't have any role", username))
        							.build();
        	unauthorizedException.setOriginalExceptionDetail(originalExceptionDetail);
        	throw unauthorizedException;
        }
	}

	private void ejectNonBranchUserLoginToIPro(String username, String appName, Set<String> loginBranchRoles) {

        log.debug("username:{} appName:{}", username, appName);
        loginBranchRoles.forEach(role -> log.debug("loginBranchRoles: -> {}" , role));

		if("IPRO".equalsIgnoreCase(appName) && !loginBranchRoles.contains("branch")){
        	UnauthorizedException unauthorizedException = new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        	OriginalExceptionDetail originalExceptionDetail = OriginalExceptionDetail.create(ExceptionOrigins.INDI.code, "iprofile.roleCheck", "not_a_branch_staff", String.format("user %s is not a branch staff - no branch role - can not use iprofile", username));
        	unauthorizedException.setOriginalExceptionDetail(originalExceptionDetail);            
            throw unauthorizedException;
        }
	}

    @Autowired
    private GeneralParamService generalParamService;	
	
	private boolean shouldGrantFacialFunction(String loginToBranchId, Set<String> loginBranchRoles) {        
        // check if user's roles is in not allow facial role list
        Set<String> facialNotAllowRoles = this.generalParamService.getFaceNotAllowRole();

        boolean inFacialNotAllowRoles = facialNotAllowRoles.stream()
        	.anyMatch(loginBranchRoles::contains);
        
        // check if loginToBranchId is in not allow facial branch list
        Set<String> facialNotAllowlBranches = this.generalParamService.getFaceNotAllowBranch();
        boolean inFacialNotAllowBranches = facialNotAllowlBranches.contains(loginToBranchId);

        // grant facial role if not in both ban list
        return !inFacialNotAllowBranches && !inFacialNotAllowRoles;
	}

	private LoginBranchInfo getLoginBranchInfo(String username, String loginBranchId) {
		LoginBranchInfo loginBranchInfo = this.employeeService.getLoginBranch(username, loginBranchId).orElse(null);
        if (loginBranchInfo == null) {
        	UnauthorizedException unauthorizedException = new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        	OriginalExceptionDetail originalExceptionDetail = OriginalExceptionDetail.builder()
        			.originateFrom(ExceptionOrigins.INDI.code)
        				.onOperation("EmployeeService.getLoginBranch")
        					.resultInCode("not_found")
        						.withDescription(String.format("(user,branch) (%s,%s) is not found in table %s", username, loginBranchId, "LOGIN_BRANCH"))
        							.build();
        	unauthorizedException.setOriginalExceptionDetail(originalExceptionDetail);
        	throw unauthorizedException;
        }
		return loginBranchInfo;
	}

	private void throwNotExistingEmployee(String username) {
		EmployeeEntity employee = employeeService.getEmployee(username).orElse(null);

        log.debug("employee for username {} -> {}", username, employee);

        if (employee == null) {
        	UnauthorizedException unauthorizedException = new UnauthorizedException(ErrorCodes.B00013.name(), ErrorCodes.B00013.getMessage());
            OriginalExceptionDetail originalExceptionDetail = OriginalExceptionDetail.builder()
            	.originateFrom(ExceptionOrigins.INDI.code)
            		.onOperation("employeeService.getEmployee")
            			.resultInCode("not_found")
            				.withDescription(String.format("%s is not found in table %s", username, "EMPLOYEE"))
            					.build();
            unauthorizedException.setOriginalExceptionDetail(originalExceptionDetail);
        	throw unauthorizedException;
        }
	}

    @Autowired
    private AccessMappingService accessMappingService;	
	
    private Set<String> getFunctionsByTypes(Set<String> types) {
        List<AccessMappingTypeEntity> accessMappingTypes = types.stream()
        	.map(type -> accessMappingService.getFunctionByType(type).orElse(null))
        		.filter(Objects::nonNull)
        			.collect(Collectors.toList());
        
        boolean anyLoginActive = accessMappingTypes.stream()
        	.anyMatch(AccessMappingTypeEntity::isLoginActive);
        if (!anyLoginActive) {
        	throw new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        }
        Set<String> result = (Set<String>)accessMappingTypes.stream()
                .flatMap(accessMappingType -> {
                    try {
                        return (Stream<String>)new ObjectMapper().readValue(accessMappingType.getFunctions(), List.class).stream();
                    } catch (JsonProcessingException e) {
                        throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());
                    }
                })
                .collect(Collectors.toSet());
        return result;
    }

    private Set<String> getFunctionsByBranchChannel(String channelType) {
        if (StringUtils.isEmpty(channelType)) {
        	// throw data exception
            throw new UnauthorizedException(ErrorCodes.S00001.name(), "Data setup error. Branch has empty ChanelType");
        }
        //Get LoginActive
        AccessMappingChannelEntity accessMappingChannel = accessMappingService.getFunctionByChannel(channelType).orElse(null);
        if (accessMappingChannel == null) {
        	// throw data exception
            throw new UnauthorizedException(ErrorCodes.S00001.name(), "Data setup error. AccessMappingChannel for channel " + channelType);
        }
        if (!accessMappingChannel.isLoginActive()) {
            throw new UnauthorizedException(ErrorCodes.B00016.name(), ErrorCodes.B00016.getMessage());
        }               
        //Get Roles
        Set<String> functionsByChannel = new HashSet<>();
        if (accessMappingChannel != null && accessMappingChannel.getFunctions() != null) {
            try {
                functionsByChannel.addAll(new ObjectMapper().readValue(accessMappingChannel.getFunctions(), List.class));
            } catch (JsonProcessingException e) {
                throw new InternalErrorException(ErrorCodes.S00001.name(), ErrorCodes.S00001.getMessage());            }
        }
        return functionsByChannel;
    }
    
}
