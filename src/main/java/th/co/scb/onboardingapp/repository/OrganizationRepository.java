package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.LoginBranchEntity;
import th.co.scb.onboardingapp.model.entity.OrganizationEntity;
import th.co.scb.onboardingapp.repository.mapper.OrganizationMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class OrganizationRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public List<OrganizationEntity> findByEmployeeId(String employeeId) {
        String sql = "select b.*\n" +
                "       from ORGANIZATION b, LOGIN_BRANCH lb\n" +
                "       where b.OC_CODE = lb.BRANCH_ID and\n" +
                "       lb.EMPLOYEE_ID = :employeeId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("employeeId", employeeId);
        List<OrganizationEntity> organizationEntityList = postgresJdbcTemplate.query(sql, params, new OrganizationMapper());
        return organizationEntityList;
    }
    public Optional<OrganizationEntity> findById(String ocCode) {
        String sql = "select * from organization where oc_code = :ocCode";
        HashMap<String, Object> params = new HashMap<>();
        params.put("ocCode", ocCode);
        OrganizationEntity organizationEntity = postgresJdbcTemplate.queryForObject(sql, params, new OrganizationMapper());
        return Optional.of(organizationEntity);
    }
    public List<OrganizationEntity> findAll() {
        String sql = "select * from organization";
        List<OrganizationEntity> organizationEntityList = postgresJdbcTemplate.getJdbcTemplate().query(sql, new OrganizationMapper());
        return organizationEntityList;
    }
}
