package th.co.scb.onboardingapp.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import th.co.scb.onboardingapp.model.*;
import th.co.scb.onboardingapp.model.entity.CaseEntity;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

@Mapper
public interface CaseMapper {

    CaseMapper INSTANCE = Mappers.getMapper(CaseMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "documentInfo", source = "caseInfo.documentInfo")
    @Mapping(target = "customerInfo", source = "caseInfo.customerInfo")
    @Mapping(target = "productInfo", source = "caseInfo.productInfo")
    @Mapping(target = "paymentInfo", source = "caseInfo.paymentInfo")
    @Mapping(target = "approvalInfo", source = "caseInfo.approvalInfo")
    @Mapping(target = "portfolioInfo", source = "caseInfo.portfolioInfo")
    @Mapping(target = "additionalInfo", source = "caseInfo.additionalInfo")
    CaseEntity map(CaseInfo caseInfo);


    @Mapping(target = "documentInfo", source = "caseEntity.documentInfo")
    @Mapping(target = "customerInfo", source = "caseEntity.customerInfo")
    @Mapping(target = "productInfo", source = "caseEntity.productInfo")
    @Mapping(target = "paymentInfo", source = "caseEntity.paymentInfo")
    @Mapping(target = "approvalInfo", source = "caseEntity.approvalInfo")
    @Mapping(target = "portfolioInfo", source = "caseEntity.portfolioInfo")
    @Mapping(target = "additionalInfo", source = "caseEntity.additionalInfo")
    CaseInfo convertToCaseInfo(CaseEntity caseEntity);



    @SneakyThrows
    default String mapDocumentInfo(List<DmsInfo> documentInfo) {
        return objectMapper.writeValueAsString(documentInfo);
    }


    @SneakyThrows
    default String mapCustomerInfo(CustomerInfo customerInfo) {
        return objectMapper.writeValueAsString(customerInfo);
    }

    @SneakyThrows
    default String mapProductInfo(List<ProductInfo> productInfo) {
        return objectMapper.writeValueAsString(productInfo);
    }

    @SneakyThrows
    default String mapPaymentInfo(List<PaymentInfo> paymentInfo) {
        return objectMapper.writeValueAsString(paymentInfo);
    }

    @SneakyThrows
    default String mapApprovalInfo(List<ApprovalInfo> approvalInfo) {
        return objectMapper.writeValueAsString(approvalInfo);
    }

    @SneakyThrows
    default String mapPortfolioInfo(PortfolioInfo portfolioInfo) {
        return objectMapper.writeValueAsString(portfolioInfo);
    }

    @SneakyThrows
    default String mapAdditionalCaseInfo(AdditionalCaseInfo additionalCaseInfo) {
        return objectMapper.writeValueAsString(additionalCaseInfo);
    }
    @SneakyThrows
    default List<DmsInfo>  convertToDocumentInfo(String documentInfo)  {
        if(StringUtils.isNotBlank(documentInfo)) {
            return objectMapper.readValue(documentInfo, new TypeReference<List<DmsInfo>>() {});

        }
        return null;
    }


    @SneakyThrows
    default CustomerInfo convertToCustomerInfo(String customerInfo) {
        if(StringUtils.isNotBlank(customerInfo)) {
            return objectMapper.readValue(customerInfo,CustomerInfo.class);

        }
        return null;
    }

    @SneakyThrows
    default List<ProductInfo> convertToProductInfo(String productInfo) {
        if(StringUtils.isNotBlank(productInfo)) {
            return objectMapper.readValue(productInfo, new TypeReference<List<ProductInfo>>() {});
        }
        return null;
    }

    @SneakyThrows
    default List<PaymentInfo> convertToPaymentInfo(String paymentInfo) {
        if(StringUtils.isNotBlank(paymentInfo)) {
            return objectMapper.readValue(paymentInfo, new TypeReference<List<PaymentInfo>>() {});
        }
        return null;
    }

    @SneakyThrows
    default List<ApprovalInfo> convertToApprovalInfo(String approvalInfo) {
            if(StringUtils.isNotBlank(approvalInfo)) {
                return objectMapper.readValue(approvalInfo, new TypeReference<List<ApprovalInfo>>() {});
            }
            return null;
    }

    @SneakyThrows
    default PortfolioInfo convertToPortfolioInfo(String portfolioInfo) {
        if(StringUtils.isNotBlank(portfolioInfo)) {
            return objectMapper.readValue(portfolioInfo,PortfolioInfo.class);
        }
        return null;
    }

    @SneakyThrows
    default AdditionalCaseInfo convertToAdditionalCaseInfo(String additionalCaseInfo) {
        if(StringUtils.isNotBlank(additionalCaseInfo)) {
            return objectMapper.readValue(additionalCaseInfo,AdditionalCaseInfo.class);
        }
        return null;
    }
}
