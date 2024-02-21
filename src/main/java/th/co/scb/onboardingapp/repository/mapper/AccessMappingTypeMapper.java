package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.AccessMappingTypeEntity;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessMappingTypeMapper implements RowMapper<AccessMappingTypeEntity> {
    @Override
    public AccessMappingTypeEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccessMappingTypeEntity accessMappingTypeEntity = new AccessMappingTypeEntity();
        accessMappingTypeEntity.setType(rs.getString("type"));
        accessMappingTypeEntity.setLoginActive(rs.getBoolean("login_active"));
        accessMappingTypeEntity.setFunctions(rs.getString("functions"));
        return accessMappingTypeEntity;
    }
}
