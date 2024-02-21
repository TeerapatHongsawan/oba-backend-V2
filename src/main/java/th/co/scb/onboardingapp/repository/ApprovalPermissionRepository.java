package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.AuthorizedLevel;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.model.entity.ApprovalPermissionEntity;
import th.co.scb.onboardingapp.repository.mapper.ApprovalPermissionMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class ApprovalPermissionRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public List<ApprovalPermissionEntity> findByEmployeeId(String employeeId) {
        String sql = "select * from approval_permission where employee_id = :employeeId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        List<ApprovalPermissionEntity> approvalPermissionEntityList = postgresJdbcTemplate.query(sql, params, new ApprovalPermissionMapper());
        return approvalPermissionEntityList;
    }
}
