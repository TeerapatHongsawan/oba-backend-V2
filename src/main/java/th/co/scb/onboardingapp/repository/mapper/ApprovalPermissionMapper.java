package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.AuthorizedLevel;
import th.co.scb.onboardingapp.model.entity.ApprovalPermissionEntity;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApprovalPermissionMapper implements RowMapper<ApprovalPermissionEntity> {
    @Override
    public ApprovalPermissionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ApprovalPermissionEntity approvalPermissionEntity = new ApprovalPermissionEntity();
        approvalPermissionEntity.setApprovalBranchId(rs.getString("appl_branch_id"));
        approvalPermissionEntity.setEmployeeId(rs.getString("employee_id"));
        approvalPermissionEntity.setAuthorizedLevel(Enum.valueOf(AuthorizedLevel.class, rs.getString("auth_level")));
        return approvalPermissionEntity;
    }
}
