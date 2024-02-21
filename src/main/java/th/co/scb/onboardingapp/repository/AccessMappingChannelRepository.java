package th.co.scb.onboardingapp.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.AccessMappingChannelEntity;
import th.co.scb.onboardingapp.repository.mapper.AccessMappingChannelMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

@Repository
public class AccessMappingChannelRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public Optional<AccessMappingChannelEntity> findById(String channel) {
        String sql = "select * from access_mapping_channel where channel = :channel";
        HashMap<String, Object> params = new HashMap<>();
        params.put("channel", channel);
        AccessMappingChannelEntity accessMappingChannelEntity = postgresJdbcTemplate.queryForObject(sql, params, new AccessMappingChannelMapper());
        return Optional.of(accessMappingChannelEntity);
    }
}
