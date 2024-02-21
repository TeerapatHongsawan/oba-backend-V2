package th.co.scb.onboardingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.DebitCardProductEntity;


import java.util.List;

@Repository
public interface DebitCardJpaRepository extends JpaRepository<DebitCardProductEntity, String> {

    List<DebitCardProductEntity> findAllByOrderByDefaultOrderAsc();
}
