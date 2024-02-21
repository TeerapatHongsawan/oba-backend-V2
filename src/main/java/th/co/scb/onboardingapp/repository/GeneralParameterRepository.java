package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.BranchEntity;
import th.co.scb.onboardingapp.model.entity.GeneralParameterEntity;
import th.co.scb.onboardingapp.repository.mapper.GeneralParameterMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class GeneralParameterRepository{

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public Optional<GeneralParameterEntity> findById(String key) {
        String sql = "select * from general_param where type = :type";
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", key);
        GeneralParameterEntity generalParameter = postgresJdbcTemplate.queryForObject(sql, params, new GeneralParameterMapper());
        return Optional.of(generalParameter);
    }
}
