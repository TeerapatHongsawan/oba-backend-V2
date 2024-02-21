package th.co.scb.onboardingapp.service;

import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.mapper.CaseMapper;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.ProductInfo;
import th.co.scb.onboardingapp.model.entity.CaseEntity;
import th.co.scb.onboardingapp.repository.CaseJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class CaseLibraryService {

    @Autowired
    private CaseJpaRepository caseRepository;

    @Autowired
    private MappingHelper mappingHelper;

    private CaseMapper caseMapper;
    public Optional<CaseInfo> getCaseByAppForm(String appFormNo) {
        CaseEntity cse = caseRepository.findByAppFormNo(appFormNo).orElse(null);

        if (cse == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(mappingHelper.map(cse, CaseInfo.class));

    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public CaseInfo updateCase(String caseId, Consumer<CaseInfo> action) {
        CaseEntity cse = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));
        CaseInfo caseInfo = mappingHelper.map(cse, CaseInfo.class);
        action.accept(caseInfo);
        CaseEntity caseToSave = mappingHelper.copy(caseInfo, cse);
        caseToSave.setSavedDate(LocalDateTime.now());
        caseRepository.save(caseToSave);
        return caseInfo;
    }

    public CaseInfo getCase(String caseId) {

        CaseEntity cse = caseRepository.findByCaseId(caseId)
                .orElseThrow(() -> new NotFoundException("Case"));
        CaseInfo result = this.caseMapper.INSTANCE.convertToCaseInfo(cse);

        return result;
    }
    public Optional<CustomerInfo> getCustomerInfo(String caseId) {
        String customerInfo = caseRepository.getCustomerInfo(caseId);
        if (customerInfo == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(mappingHelper.convertToObject(customerInfo, CustomerInfo.class));
    }
}
