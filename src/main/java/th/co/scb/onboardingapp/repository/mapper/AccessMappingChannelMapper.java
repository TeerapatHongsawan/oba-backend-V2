package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.AccessMappingChannelEntity;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessMappingChannelMapper implements RowMapper<AccessMappingChannelEntity> {
    @Override
    public AccessMappingChannelEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AccessMappingChannelEntity accessMappingChannelEntity = new AccessMappingChannelEntity();
        accessMappingChannelEntity.setChannel(rs.getString("channel"));
        accessMappingChannelEntity.setLoginActive(rs.getBoolean("login_active"));
        accessMappingChannelEntity.setFunctions(rs.getString("functions"));
        return accessMappingChannelEntity;
    }
}
