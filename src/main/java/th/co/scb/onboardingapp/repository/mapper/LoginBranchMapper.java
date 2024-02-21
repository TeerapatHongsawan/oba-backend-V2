package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.LoginBranchEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginBranchMapper implements RowMapper<LoginBranchEntity> {
    @Override
    public LoginBranchEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoginBranchEntity loginBranchEntity = new LoginBranchEntity();
        loginBranchEntity.setBranchId(rs.getString("branch_id"));
        loginBranchEntity.setEmployeeId(rs.getString("employee_id"));
        loginBranchEntity.setRoles(rs.getString("roles"));
        loginBranchEntity.setApprovalBranchId(rs.getString("appl_branch_id"));
        return loginBranchEntity;
    }
}
