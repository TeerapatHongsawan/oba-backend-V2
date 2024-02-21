package th.co.scb.onboardingapp.service;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.AccountTypeInfo;
import th.co.scb.onboardingapp.model.DebitCardInfo;
import th.co.scb.onboardingapp.model.entity.AccountProductEntity;
import th.co.scb.onboardingapp.model.entity.AccountTypeEntity;

import org.jooq.lambda.tuple.Tuple2;
import th.co.scb.onboardingapp.model.entity.DebitCardProductEntity;
import th.co.scb.onboardingapp.repository.AccountProductJpaRepository;
import th.co.scb.onboardingapp.repository.AccountTypeJpaRepository;
import th.co.scb.onboardingapp.repository.DebitCardJpaRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private MappingHelper mappingHelper;

    @Autowired
    private AccountTypeJpaRepository accountTypeRepository;

    @Autowired
    private AccountProductJpaRepository accountProductDetailRepository;

    @Autowired
    private DebitCardJpaRepository debitCardRepository;
    @Cacheable("getProducts")
    public List<AccountTypeInfo> getProducts() {
        List<AccountTypeEntity> accountTypes = accountTypeRepository.findAllByOrderByDefaultOrderAsc();
        List<AccountTypeEntity> results = accountTypes.stream()
                .filter(group -> !group.getAccounts().isEmpty())
                .collect(Collectors.toList());

        log.debug("All AccountType size: {}", accountTypes.size());
        log.debug("Active AccountType size: {}", results.size());

        List<AccountTypeInfo> accountTypeInfos = mappingHelper.mapAsList(results, AccountTypeInfo.class);

        accountTypeInfos.stream()
                .flatMap(it -> it.getAccounts().stream())
                .map(it -> new Tuple2<>(it.getAccountTypeCode(), it.getProducts()))
                .forEach(it -> {
                    String accountTypeCode = it.v1;
                    it.v2.forEach(x -> x.setAccountType(accountTypeCode));
                });

        return accountTypeInfos;
    }
    @Cacheable("getAccountProductMap")
    public Map<String, AccountProductEntity> getAccountProductMap() {
        List<AccountProductEntity> all = accountProductDetailRepository.findAll();
        for (AccountProductEntity accountProduct : all) {
            if (accountProduct.getCbsProductCode() != null && accountProduct.getCbsProductCode().length() == 4) {
                accountProduct.setCbsProductCode(accountProduct.getCbsProductCode().substring(1));
            }
        }
        return all.stream()
                .filter(it -> !Strings.isNullOrEmpty(it.getCbsProductCode()))
                .collect(Collectors.toMap(AccountProductEntity::getCbsProductCode,
                        Function.identity(),
                        (t1, t2) -> t1));
    }

    public List<DebitCardInfo> getDebitCards() {
        List<DebitCardProductEntity> debitCards = debitCardRepository.findAllByOrderByDefaultOrderAsc();
        List<DebitCardProductEntity> results = debitCards.stream()
                .filter(this::isActiveDate)
                .collect(Collectors.toList());

        log.debug("All DebitCard size: {}", debitCards.size());
        log.debug("Active DebitCard size: {}", results.size());
        return mappingHelper.mapAsList(results, DebitCardInfo.class);
    }
    private boolean isActiveDate(DebitCardProductEntity card) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate activeDate = LocalDate.parse(card.getActiveDate(), formatter);
        return LocalDate.now().isAfter(activeDate);
    }

}
