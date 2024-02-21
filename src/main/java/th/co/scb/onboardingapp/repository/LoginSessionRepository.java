package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.LoginSessionEntity;
import th.co.scb.onboardingapp.repository.mapper.LoginSessionMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class LoginSessionRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public void insertDataLoginSession(String employeeId,String lastActivityTime,String deviceId,String status,String appName, String token) {
        String sql = "INSERT INTO LOGIN_SESSION (EMPLOYEE_ID, LAST_ACTIVITY_TIME, DEVICE_ID, STATUS, APP_NAME, TOKEN) \n" +
                " VALUES (:employeeId, :lastActivityTime, :deviceId, :status, :appName, :token)";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("lastActivityTime",  Timestamp.valueOf(lastActivityTime));
        params.put("deviceId", deviceId);
        params.put("status", status);
        params.put("appName", appName);
        params.put("token", token);
        postgresJdbcTemplate.update(sql, params);
    }
    public void updateDataLoginSession(String employeeId, String ldt, String deviceId, String appName) {
        String sql = "update LOGIN_SESSION \n" +
                "set LAST_ACTIVITY_TIME = :ldt , \n" +
                " DEVICE_ID = :deviceId \n" +
                "where EMPLOYEE_ID = :employeeId and APP_NAME = :appName";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("ldt", Timestamp.valueOf(ldt));
        params.put("deviceId", deviceId);
        params.put("appName", appName);
        postgresJdbcTemplate.update(sql, params);
    }
    public List<LoginSessionEntity> findByEmployeeId(String employeeId, String appName) {
        String sql = "select a.* from LOGIN_SESSION a where a.EMPLOYEE_ID = :employeeId  and a.APP_NAME = :appName";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("appName", appName);
        List<LoginSessionEntity> loginSessionEntities = postgresJdbcTemplate.query(sql, params, new LoginSessionMapper());
     return loginSessionEntities;
    }
    public Boolean deleteDataLoginSession(String employeeId, String deviceUUID, String appName){
        boolean isSuccess = false;
        String sql = "delete from LOGIN_SESSION where LOGIN_SESSION.EMPLOYEE_ID = :employeeId and LOGIN_SESSION.DEVICE_ID = :deviceUUID and LOGIN_SESSION.APP_NAME = :appName";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        params.put("deviceUUID", deviceUUID);
        params.put("appName", appName);
        int update = postgresJdbcTemplate.update(sql, params);
        if (update != 0) {
            isSuccess = true;
        }
        return isSuccess;
    }
}
