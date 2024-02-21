package th.co.scb.onboardingapp.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import th.co.scb.onboardingapp.model.entity.AccessMappingChannelEntity;
import th.co.scb.onboardingapp.model.entity.BranchEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BranchMapper implements RowMapper<BranchEntity> {
    @Override
    public BranchEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setBranchId(rs.getString("branch_id"));
        branchEntity.setBookingBranch(rs.getBoolean("is_booking_branch"));
        branchEntity.setOwnBranchOnly(rs.getBoolean("own_branch_only"));
        branchEntity.setChannelType(rs.getString("channel_type"));
        branchEntity.setNameEn(rs.getString("name_en"));
        branchEntity.setNameThai(rs.getString("name_th"));
        branchEntity.setRegionCode(rs.getString("region_code"));
        return branchEntity;
    }
}
