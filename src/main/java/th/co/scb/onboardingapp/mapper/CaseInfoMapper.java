package th.co.scb.onboardingapp.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import th.co.scb.onboardingapp.model.AdditionalCaseInfo;
import th.co.scb.onboardingapp.model.ApprovalInfo;
import th.co.scb.onboardingapp.model.CaseInfo;
import th.co.scb.onboardingapp.model.CustomerInfo;
import th.co.scb.onboardingapp.model.DmsInfo;
import th.co.scb.onboardingapp.model.PaymentInfo;
import th.co.scb.onboardingapp.model.PortfolioInfo;
import th.co.scb.onboardingapp.model.ProductInfo;
import th.co.scb.onboardingapp.model.entity.CaseEntity;
import java.util.List;

@Mapper
public interface CaseInfoMapper {

    CaseInfoMapper INSTANCE = Mappers.getMapper(CaseInfoMapper.class);

    @Mapping(source = "caseEntity.caseId", target = "caseId")
    @Mapping(source = "caseEntity.branchId", target = "branchId")
    @Mapping(source = "caseEntity.bookingBranchId", target = "bookingBranchId")
    @Mapping(source = "caseEntity.ocCode", target = "ocCode")
    @Mapping(source = "caseEntity.employeeId", target = "employeeId")
    @Mapping(source = "caseEntity.referenceId", target = "referenceId")
    @Mapping(source = "caseEntity.createdDate", target = "createdDate")
    @Mapping(source = "caseEntity.savedDate", target = "savedDate")
    @Mapping(source = "caseEntity.fulfilmentDate", target = "fulfilmentDate")
    @Mapping(source = "caseEntity.completedDate", target = "completedDate")
    @Mapping(source = "caseEntity.caseTypeID", target = "caseTypeID")
    @Mapping(source = "caseEntity.caseStatus", target = "caseStatus")
    @Mapping(source = "caseEntity.reasonCode", target = "reasonCode")
    @Mapping(source = "caseEntity.appFormNo", target = "appFormNo")
    @Mapping(source = "caseEntity.customerInfo", target = "customerInfo" , qualifiedByName = "mapCustomerInfo")
    @Mapping(source = "caseEntity.productInfo", target = "productInfo", qualifiedByName = "mapProductInfo")
    @Mapping(source = "caseEntity.documentInfo", target = "documentInfo" , qualifiedByName = "mapDocumentInfo")
    @Mapping(source = "caseEntity.paymentInfo", target = "paymentInfo" , qualifiedByName = "mapPaymentInfo")
    @Mapping(source = "caseEntity.approvalInfo", target = "approvalInfo" , qualifiedByName = "mapApprovalInfo")
    @Mapping(source = "caseEntity.portfolioInfo", target = "portfolioInfo",   qualifiedByName = "mapPortfolioInfo")
    @Mapping(source = "caseEntity.additionalInfo", target = "additionalInfo" ,   qualifiedByName = "mapAdditionalInfo")
    CaseInfo map(CaseEntity caseEntity);

    @Named("mapCustomerInfo")
    default CustomerInfo mapCustomerInfo(String customerInfo) {
        return mapJsonToObject(customerInfo, CustomerInfo.class);
    }

    @Named("mapPortfolioInfo")
    default PortfolioInfo mapPortfolioInfo(String portfolioInfo) {
        return mapJsonToObject(portfolioInfo, PortfolioInfo.class);
    }

    @Named("mapAdditionalInfo")
    default AdditionalCaseInfo mapAdditionalInfo(String additionalInfo) {
        return mapJsonToObject(additionalInfo, AdditionalCaseInfo.class);
    }

    @Named("mapProductInfo")
    @SneakyThrows
    default List<ProductInfo> mapProductInfo(String productInfo) {
        return new ObjectMapper().readValue(productInfo, new TypeReference<List<ProductInfo>>() {});
    }

    @Named("mapDocumentInfo")
    @SneakyThrows
    default List<DmsInfo> mapDocumentInfo(String dmsInfo) {
        return new ObjectMapper().readValue(dmsInfo, new TypeReference<List<DmsInfo>>() {});
    }

    @Named("mapApprovalInfo")
    @SneakyThrows
    default List<ApprovalInfo> mapApprovalInfo(String approvalInfo) {
        return new ObjectMapper().readValue(approvalInfo, new TypeReference<List<ApprovalInfo>>() {});
    }

    @Named("mapPaymentInfo")
    @SneakyThrows
    default List<PaymentInfo> mapPaymentInfo(String paymentInfo) {
        return new ObjectMapper().readValue(paymentInfo, new TypeReference<List<PaymentInfo>>() {});
    }

    @SneakyThrows
    default <T> T mapJsonToObject(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, clazz);
    }

}
