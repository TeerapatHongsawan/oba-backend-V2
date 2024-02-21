package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.model.entity.RedListEntity;
import th.co.scb.onboardingapp.repository.RedListJpaRepository;

import java.util.List;

@Slf4j
@Service
public class RedListService {

    @Autowired
    private RedListJpaRepository redListRepository;

    public List<RedListEntity> getRedList(String caseId) {
        return redListRepository.findByCaseId(caseId);
    }

    public void insertRedList(RedListEntity redListEntity) {
        try {
            redListRepository.save(redListEntity);
        } catch (Exception ignored) {
            log.info("Insert Red List Fail.");
        }
    }

    public void deleteRedList(String caseId) {
        int rowEffect = redListRepository.deleteByCaseId(caseId);
        log.info("Delete RED_LIST {} rows", rowEffect);
    }
}
