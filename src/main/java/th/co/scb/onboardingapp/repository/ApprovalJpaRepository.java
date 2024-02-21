package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.ApprovalStatus;
import th.co.scb.onboardingapp.model.ApprovalType;
import th.co.scb.onboardingapp.model.entity.ApprovalEntity;

import java.util.Date;
import java.util.List;


@Repository
public interface ApprovalJpaRepository extends JpaRepository<ApprovalEntity, String> {

    List<ApprovalEntity> findByApprovalStatusAndTypeAndSellerIdNotAndApprovalBranchIdStartingWithAndCreatedDateAfter(
            ApprovalStatus approvalStatus,
            ApprovalType type,
            String sellerId,
            String approvalBranchId,
            Date after);
}
