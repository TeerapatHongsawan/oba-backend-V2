package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.OrganizationEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganizationMapper implements RowMapper<OrganizationEntity> {
    @Override
    public OrganizationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrganizationEntity organization = new OrganizationEntity();
        organization.setOcCode(rs.getString("oc_code"));
        organization.setOcNameEn(rs.getString("oc_name_en"));
        organization.setOcNameTh(rs.getString("oc_name_th"));
        return organization;
    }
}
