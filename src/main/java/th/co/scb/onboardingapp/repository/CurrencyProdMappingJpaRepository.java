package th.co.scb.onboardingapp.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import th.co.scb.onboardingapp.model.entity.CurrencyProdMappingEntity;

import java.util.List;

@Repository
public interface CurrencyProdMappingJpaRepository extends JpaRepository<CurrencyProdMappingEntity, String> {

    @Query(value = "SELECT curr_mapping.INDI_PROD_CODE , " +
            " curr_mapping.CURRENCYCODE, " +
            " curr_mapping.RESIDENT_TYPE, " +
            " curr_mapping.ACTIVE " +
            " FROM CURRENCY_CONFIG curr_config " +
            " INNER JOIN CURRENCY_PROD_MAPPING curr_mapping ON curr_config.CURRENCYCODE = curr_mapping.CURRENCYCODE " +
            " AND curr_config.ACTIVE = 'Y' " +
            " AND curr_mapping.ACTIVE = 'Y' " +
            " ORDER BY curr_mapping.INDI_PROD_CODE ASC,curr_config.CURRENCYCODE ASC", nativeQuery = true)
    List<CurrencyProdMappingEntity> findByCurrencyConfigAndCurrencyProdMappingActiveY();

}