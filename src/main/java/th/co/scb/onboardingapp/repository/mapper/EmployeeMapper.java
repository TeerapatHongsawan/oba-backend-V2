package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeMapper implements RowMapper<EmployeeEntity> {
    @Override
    public EmployeeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId(rs.getString("employee_id"));
        employeeEntity.setFirstNameEn(rs.getString("firstname_en"));
        employeeEntity.setLastNameEn(rs.getString("lastname_en"));
        employeeEntity.setFirstNameThai(rs.getString("firstname_th"));
        employeeEntity.setLastNameThai(rs.getString("lastname_th"));
        employeeEntity.setLicenseId(rs.getString("license_id"));
        employeeEntity.setOcCode(rs.getString("oc_code"));
        employeeEntity.setOcNameEn(rs.getString("oc_name"));
        employeeEntity.setOcNameTh(rs.getString("oc_name_th"));
        employeeEntity.setEmail(rs.getString("email"));
        employeeEntity.setPositionName(rs.getString("position_name"));
        employeeEntity.setSamEmployeeId(rs.getString("sam_employee_id"));
        employeeEntity.setSamFirstNameEn(rs.getString("sam_firstname_en"));
        employeeEntity.setSamLastNameEn(rs.getString("sam_lastname_en"));
        employeeEntity.setSamFirstNameThai(rs.getString("sam_firstname_th"));
        employeeEntity.setSamLastNameThai(rs.getString("sam_lastname_th"));
        employeeEntity.setSamOcCode(rs.getString("sam_oc_code"));
        employeeEntity.setSamOcNameEn(rs.getString("sam_oc_name"));
        employeeEntity.setSamOcNameTh(rs.getString("sam_oc_name_th"));
        employeeEntity.setSamEmail(rs.getString("sam_email"));
        return employeeEntity;
    }
}
