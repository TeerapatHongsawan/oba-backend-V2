package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.StartbizUserProfilesDetail;
import th.co.scb.onboardingapp.model.entity.OrganizationEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class StartbizUserProfilesMapper implements RowMapper<StartbizUserProfilesDetail> {
    @Override
    public StartbizUserProfilesDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        StartbizUserProfilesDetail startbizUserProfilesDetail = new StartbizUserProfilesDetail();
        startbizUserProfilesDetail.setStaffID(rs.getString("STAFF_ID"));

        startbizUserProfilesDetail.setUserRole(rs.getString("USERROLE"));

        startbizUserProfilesDetail.setFirstNameTH(rs.getString("FIRSTNAME_TH"));

        startbizUserProfilesDetail.setLastNameTH(rs.getString("LASTNAME_TH"));

        startbizUserProfilesDetail.setFirstNameEN(rs.getString("FIRSTNAME_EN"));

        startbizUserProfilesDetail.setLastNameEN(rs.getString("LASTNAME_EN"));

        startbizUserProfilesDetail.setBranchCode(rs.getString("BRANCH_CODE"));

        startbizUserProfilesDetail.setBranchNameTH(rs.getString("BRANCH_NAME_TH"));

        startbizUserProfilesDetail.setUserPosition(rs.getString("USER_POSITION"));

        startbizUserProfilesDetail.setOcCode(rs.getString("OC_CODE"));

        startbizUserProfilesDetail.setManagerID(rs.getString("MANAGER_ID"));

        startbizUserProfilesDetail.setEmail(rs.getString("EMAIL"));

        startbizUserProfilesDetail.setCorpCode(rs.getString("CORP_CODE"));

        startbizUserProfilesDetail.setPrefixTH(rs.getString("PREFIX_TH"));

        startbizUserProfilesDetail.setSegCode(rs.getString("SEG_CODE"));

        startbizUserProfilesDetail.setPrefixEN(rs.getString("PREFIX_EN"));

        startbizUserProfilesDetail.setSourceSys(rs.getString("SRC_SYS"));

        startbizUserProfilesDetail.setAccountOwner(rs.getBoolean("IS_AC_OWN"));

        startbizUserProfilesDetail.setAccountOwnerManager(rs.getBoolean("IS_AC_OWN_MGR"));

        startbizUserProfilesDetail.setOcOwner(rs.getBoolean("IS_OC_OWN"));

        startbizUserProfilesDetail.setManager(rs.getBoolean("IS_MGR"));

        startbizUserProfilesDetail.setKycOnly(rs.getBoolean("IS_KYC_ONLY"));

        startbizUserProfilesDetail.setCorpTTL(rs.getString("CORP_TTL"));

        startbizUserProfilesDetail.setJobCode(rs.getString("JOB_CODE"));

        startbizUserProfilesDetail.setMultiRole(rs.getString("MULTI_ROLE"));

        startbizUserProfilesDetail.setOicList(rs.getString("OIC_LIST"));

        startbizUserProfilesDetail.setTimeStampCreated(rs.getTime("TIMESTAMP_CREATED"));
        return startbizUserProfilesDetail;
    }
}
