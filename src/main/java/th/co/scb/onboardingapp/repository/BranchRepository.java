package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.model.entity.BranchEntity;
import th.co.scb.onboardingapp.repository.mapper.BranchMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class BranchRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public List<BranchEntity> findAll() {
        String sql = "select * from branch";
        List<BranchEntity> branchEntityList = postgresJdbcTemplate.getJdbcTemplate().query(sql, new BranchMapper());
        return branchEntityList;
    }
    public Optional<BranchEntity> findById(String branchId) {
        String sql = "select * from branch where branch_id = :branchId";
        HashMap<String, Object> params = new HashMap<>();
        params.put("branchId", branchId);
        BranchEntity branchEntity = postgresJdbcTemplate.queryForObject(sql, params, new BranchMapper());
        return Optional.of(branchEntity);
    }
}
