package th.co.scb.onboardingapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import th.co.scb.onboardingapp.model.AccountTypeInfo;
import th.co.scb.onboardingapp.service.ProductApplicationService;
import th.co.scb.onboardingapp.utility.MockModel;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductApplicationService productApplicationService;

    MockModel mockModel = new MockModel();
    @Test
    public void testGetAccountsMatch() {

        List<AccountTypeInfo> response = new ArrayList<>();
        AccountTypeInfo item = new AccountTypeInfo();
        item.setAccounts(new ArrayList<>());
        item.setCode("ST2");
        item.setName("Savings Account");
        item.setNameEn("Savings Account");
        response.add(item);
        response.add(new AccountTypeInfo());

        List<AccountTypeInfo> expect = new ArrayList<>();
        AccountTypeInfo expectItem = new AccountTypeInfo();
        expectItem.setAccounts(new ArrayList<>());
        expectItem.setCode("ST2");
        expectItem.setName("Savings Account");
        expectItem.setNameEn("Savings Account");
        expect.add(expectItem);
        expect.add(new AccountTypeInfo());

        when(productApplicationService.getAccounts()).thenReturn(response);
        assertEquals(productController.accounts(),expect);
    }
    @Test
    public void testGetAccountsNotMatch() {

        List<AccountTypeInfo> response = new ArrayList<>();
        AccountTypeInfo item = new AccountTypeInfo();
        item.setAccounts(new ArrayList<>());
        item.setCode("ST2");
        item.setName("Savings Account");
        item.setNameEn("Savings Account");
        response.add(item);
        response.add(new AccountTypeInfo());

        List<AccountTypeInfo> expect = new ArrayList<>();
        AccountTypeInfo expectItem = new AccountTypeInfo();
        expectItem.setAccounts(new ArrayList<>());
        expectItem.setCode("ST1");
        expectItem.setName("Savings Account");
        expectItem.setNameEn("Savings Account");
        expect.add(expectItem);
        expect.add(new AccountTypeInfo());

        when(productApplicationService.getAccounts()).thenReturn(response);
        assertNotEquals(productController.accounts(),expect);
    }
    @Test
    public void debitCardsTest() {
        when(productApplicationService.getDebitCards(any())).thenReturn(mockModel.getDebitCardResponse());
        assertThat(productController.debitcards(mockModel.getObaAuthentication())).isNotNull();
    }

}