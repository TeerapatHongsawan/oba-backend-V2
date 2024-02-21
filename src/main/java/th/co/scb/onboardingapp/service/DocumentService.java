package th.co.scb.onboardingapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.co.scb.onboardingapp.exception.NotFoundException;
import th.co.scb.onboardingapp.helper.MappingHelper;
import th.co.scb.onboardingapp.model.DmsInfo;
import th.co.scb.onboardingapp.model.entity.DocumentBlobEntity;
import th.co.scb.onboardingapp.repository.DocumentBlobJpaRepository;
import th.co.scb.onboardingapp.repository.DocumentStageJpaRepository;


import java.util.List;

@Slf4j
@Service
public class DocumentService {

    @Autowired
    private DocumentStageJpaRepository documentStageRepository;


    @Autowired
    private DocumentBlobJpaRepository documentBlobRepository;

    @Autowired
    private CaseLibraryService caseService;

    @Autowired
    private MappingHelper mappingHelper;



    public DocumentBlobEntity saveDocument(DocumentBlobEntity documentBlob) {
        return documentBlobRepository.save(documentBlob);
    }

    public String nextVal(String branchId) {
        return documentStageRepository.nextVal(branchId);
    }


}
