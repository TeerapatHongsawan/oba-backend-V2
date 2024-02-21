package th.co.scb.onboardingapp.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.repository.mapper.AccessMappingTypeMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class AccessMappingTypeRepository {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    private NamedParameterJdbcTemplate postgresJdbcTemplate;

    public List<AccessMappingTypeEntity> findAll() {
        String sql = "select * from access_mapping_type";
        List<AccessMappingTypeEntity> accessMappingTypeEntityList = postgresJdbcTemplate.getJdbcTemplate().query(sql, new AccessMappingTypeMapper());
        return accessMappingTypeEntityList;
    }
    public Optional<AccessMappingTypeEntity> findById(String type) {
        try {
            log.debug("access_mapping_type : type :{}", type);
            String sql = "select * from access_mapping_type where type = :type";
            HashMap<String, Object> params = new HashMap<>();
            params.put("type", type);
            AccessMappingTypeEntity accessMappingTypeEntity = postgresJdbcTemplate.queryForObject(sql, params, new AccessMappingTypeMapper());
            return Optional.of(accessMappingTypeEntity);
        }catch (RuntimeException ex){
            log.error(ex.getMessage());
            return Optional.empty();
        }
    }
}
