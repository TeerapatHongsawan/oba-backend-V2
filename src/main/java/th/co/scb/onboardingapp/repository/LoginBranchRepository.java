package th.co.scb.onboardingapp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.GeneralParameterEntity;
import th.co.scb.onboardingapp.model.entity.LoginBranchEntity;
import th.co.scb.onboardingapp.repository.mapper.LoginBranchMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@Repository
@Slf4j
public class LoginBranchRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public Optional<LoginBranchEntity> findByEmployeeIdAndBranchId(String employeeId, String branchId) {
        log.debug("login_branch employeeId:{}, branchId:{}", employeeId, branchId);
        String sql = "select * from login_branch where employee_id = :employeeId and branch_id = :branchId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("branchId", branchId);
        LoginBranchEntity loginBranchEntity = postgresJdbcTemplate.queryForObject(sql, params, new LoginBranchMapper());
        return Optional.of(loginBranchEntity);
    }
}
