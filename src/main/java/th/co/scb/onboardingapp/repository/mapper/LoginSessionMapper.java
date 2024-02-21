package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.LoginSessionEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginSessionMapper implements RowMapper<LoginSessionEntity> {
    @Override
    public LoginSessionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoginSessionEntity loginSessionEntity = new LoginSessionEntity();
        loginSessionEntity.setEmployeeId(rs.getString("employee_id"));
        loginSessionEntity.setLastActivityTime(rs.getObject("last_activity_time", LocalDateTime.class));
        loginSessionEntity.setDeviceId(rs.getString("device_id"));
        loginSessionEntity.setStatus(rs.getString("status"));
        loginSessionEntity.setAppName(rs.getString("app_name"));
        loginSessionEntity.setToken(rs.getString("token"));
        return loginSessionEntity;
    }
}
