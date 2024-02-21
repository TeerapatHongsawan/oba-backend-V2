package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.AccountTypeEntity;

import java.util.List;

@Repository
public interface AccountTypeJpaRepository extends JpaRepository<AccountTypeEntity, Integer> {

    List<AccountTypeEntity> findAllByOrderByDefaultOrderAsc();
}
