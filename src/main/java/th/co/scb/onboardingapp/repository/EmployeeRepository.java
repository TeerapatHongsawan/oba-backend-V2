package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.StartbizUserProfilesDetail;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.repository.mapper.EmployeeMapper;
import th.co.scb.onboardingapp.repository.mapper.StartbizUserProfilesMapper;

import java.util.*;

@Repository
public class EmployeeRepository{

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    @Autowired
    @Qualifier("startbizJdbcTemplate")
    private NamedParameterJdbcTemplate startBizJdbcTemplate;

    public Optional<StartbizUserProfilesDetail> findByIdStartBiz(String staffId) {
        String sql = "select * from USER_PROFILE where STAFF_ID = :staffId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("staffId", staffId);
        //StartbizUserProfilesDetail startbizUserProfilesDetail = startBizJdbcTemplate.queryForObject(sql, params, new StartbizUserProfilesMapper());
        //return Optional.of(startbizUserProfilesDetail);
        List<StartbizUserProfilesDetail> resultList = startBizJdbcTemplate.query(sql, params, new StartbizUserProfilesMapper());
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    public Optional<EmployeeEntity> findById(String employeeId) {
        String sql = "select * from employee where employee_id = :employeeId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        EmployeeEntity employeeEntity = postgresJdbcTemplate.queryForObject(sql, params, new EmployeeMapper());
        return Optional.of(employeeEntity);
    }

    public List<EmployeeEntity> findByBranchId(String branchId) {
        String sql = "select e.*\n" +
                "       from EMPLOYEE e, LOGIN_BRANCH lb\n" +
                "       where e.EMPLOYEE_ID = lb.EMPLOYEE_ID and\n" +
                "       lb.BRANCH_ID = :branchId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        List<EmployeeEntity> employeeEntityList = postgresJdbcTemplate.query(sql, params, new EmployeeMapper());
        return employeeEntityList;
    }
    public List<EmployeeEntity> findApprovers(String approvalBranchId, Set<String> levelSet) {
        String sql = "select e.*\n" +
                "       from EMPLOYEE e, APPROVAL_PERMISSION ap\n" +
                "       where e.EMPLOYEE_ID = ap.EMPLOYEE_ID and\n" +
                "       ap.APPL_BRANCH_ID = :branchId and\n" +
                "       ap.AUTH_LEVEL in :levels";
        HashMap<String, Object> params = new HashMap<>();
        params.put("branchId", approvalBranchId);
        params.put("levels", levelSet);
        List<EmployeeEntity> employeeEntityList = postgresJdbcTemplate.query(sql, params, new EmployeeMapper());
        return employeeEntityList;
    }
}
