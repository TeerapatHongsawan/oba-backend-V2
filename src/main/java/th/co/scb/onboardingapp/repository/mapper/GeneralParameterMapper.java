package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.model.entity.GeneralParameterEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GeneralParameterMapper implements RowMapper<GeneralParameterEntity> {
    @Override
    public GeneralParameterEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        GeneralParameterEntity generalParameter = new GeneralParameterEntity();
        generalParameter.setServerOnly(rs.getBoolean("server_only"));
        generalParameter.setType(rs.getString("type"));
        generalParameter.setValue(rs.getString("value"));
        return generalParameter;
    }
}
