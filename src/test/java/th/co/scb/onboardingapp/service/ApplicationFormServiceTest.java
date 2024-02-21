package th.co.scb.onboardingapp.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.EmployeeEntity;
import th.co.scb.onboardingapp.utility.MockModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static th.co.scb.onboardingapp.model.common.AppFormDocumentType.P029_KYC_101;
import static th.co.scb.onboardingapp.model.common.AppFormRenderMode.CUSTOMER_PREVIEW_MODE;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@TestPropertySource("classpath:application-test.properties")
public class ApplicationFormServiceTest {

    @Autowired
    private ApplicationFormService applicationFormService;

    @Mock
    private EmployeeService employeeService;

    MockModel mockModel = new MockModel();

//    @InjectMocks
//    IamAdminService iamAdminService;

    @Test
    public void successGenForm() {
        List<AppForm> appFormsList = new ArrayList<>();
        AppForm appForms = new AppForm();
        appForms.setAccountNumber("testAccNo");
        appFormsList.add(appForms);

        IdentificationsItem identificationsItem = new IdentificationsItem();
        identificationsItem.setIdenStatus("Success");
        identificationsItem.setIdenType("m101_purpose");
        IdenDetail idenDetail = new IdenDetail();
        Purpose purpose = new Purpose();
        purpose.setAnswer1(Boolean.TRUE);
        purpose.setAnswer2(Boolean.TRUE);
        purpose.setAnswer3(Boolean.TRUE);
        idenDetail.setPurpose(purpose);
        identificationsItem.setIdenDetail(idenDetail);

        List<IdentificationsItem> identificationsItemsList = new ArrayList<>();
        identificationsItemsList.add(identificationsItem);

        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setIdentifications(identificationsItemsList);

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCaseStatus("Success");
        caseInfo.setCustomerInfo(customerInfo);
        caseInfo.setCaseId("test");
        caseInfo.setBranchId("test");
        caseInfo.setCreatedDate(LocalDateTime.now());

        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmployeeId("123");

        when(employeeService.getEmployee("test")).thenReturn(Optional.of(employeeEntity));
//        assertEquals(Optional.of(mockModel.accessMappingTypeEntity()), accessMappingService.getFunctionByType("branch"));
        List<AppForm> appForm = applicationFormService.generateAppForm(caseInfo, P029_KYC_101, CUSTOMER_PREVIEW_MODE, "test");
        assertTrue(!appForm.isEmpty());
    }
}
