package th.co.scb.onboardingapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import th.co.scb.entapi.EntApiConfig;
import th.co.scb.entapi.account_loan.AccountsLoansRevolvingApi;
import th.co.scb.entapi.accounts_cards_authentication_pinblock.AccountsCardsV2Api;
import th.co.scb.entapi.accounts_creditcards_composite.AccountsCreditcardsCompositeGWApi;
import th.co.scb.entapi.accounts_insurances.AccountsInsurancesApi;
import th.co.scb.entapi.accounts_mutualfund_oef.AccountsMutualFundsApi;
import th.co.scb.entapi.alerts.ServicingAlertsRegistrationApi;
import th.co.scb.entapi.bgcards.AccountsWalletMultiCurrencyApi;
import th.co.scb.entapi.bioauth.SupportUtilBioAuthV2Api;
import th.co.scb.entapi.bpo.ServicingBranchPaymentOrderApi;
import th.co.scb.entapi.composite.AccountsCreditcardsCompositeApi;
import th.co.scb.entapi.composite.AccountsDebitcardsCompositeApi;
import th.co.scb.entapi.composite_account_deposit.CompositeAccountsDepositsApi;
import th.co.scb.entapi.csent_customer_consent.CustomerConsentsApi;
import th.co.scb.entapi.csent_customer_consent.CustomerConsentsV2Api;
import th.co.scb.entapi.custinfo.CustProfileCustinfoApi;
import th.co.scb.entapi.custinfo.CustomerProfileCustinfoApi;
import th.co.scb.entapi.customer_authentication_preauth.CustomerAuthenticationPreAuthApi;
import th.co.scb.entapi.customer_consents.CustomerConsentApi;
import th.co.scb.entapi.debitcards.AccountsCardsDebitcardsApi;
import th.co.scb.entapi.deposits.AccountsDepositsApi;
import th.co.scb.entapi.deposits.AccountsDepositsMessageServiceApi;
import th.co.scb.entapi.deposits.AccountsDepositsV2Api;
import th.co.scb.entapi.detica.ExternalAgencyDETICAApi;
import th.co.scb.entapi.docs_vchan.DocumentManagementVChannelApi;
import th.co.scb.entapi.docs_websig.WebSignatureApi;
import th.co.scb.entapi.documentum.EDSApi;
import th.co.scb.entapi.easyNet.CustomerServicesRegistrationApi;
import th.co.scb.entapi.emv_customer_inquiryredlist.EmvCustomerApi;
import th.co.scb.entapi.external_agency_dopa.ExternalAgencyDOPAApi;
import th.co.scb.entapi.fatca.ExternalAgencyFATCAApi;
import th.co.scb.entapi.indikyc.CustProfileIndiKYCApi;
import th.co.scb.entapi.individuals.CustProfileIndividualsApi;
import th.co.scb.entapi.individuals.CustProfileIndividualsApiV2;
import th.co.scb.entapi.individuals.CustomerProfileIndividualsV2Api;
import th.co.scb.entapi.individuals.CustomerProfileIndividualsV3Api;
import th.co.scb.entapi.mutualfunds.*;
import th.co.scb.entapi.notification_v3.SendNotificationV3Api;
import th.co.scb.entapi.otp.TxnAuthOTPApi;
import th.co.scb.entapi.ref_common.ReferenceDataCommonGeneralApi;
import th.co.scb.entapi.ref_postals.ReferenceDataThaiPostalsApi;
import th.co.scb.entapi.referencedata_databases_reference_bank_code.ReferenceDataCommonDatabaseApi;
import th.co.scb.entapi.referencedata_databases_reference_country.ReferenceDataDatabaseCommonApi;
import th.co.scb.entapi.sales_services.CustomerServicesApi;
import th.co.scb.entapi.sales_services.CustomerServicesV2Api;
import th.co.scb.entapi.securities.AccountsSecuritiesApi;
import th.co.scb.entapi.support_utility_facematching.SupportUtilityFaceMatchingApi;
import th.co.scb.entapi.swagger_export_externalcards_pinblockActivation.AccountsCardsExternalCardsApi;
import th.co.scb.entapi.watchlist.ExternalAgencyWATCHLISTApi;
import th.co.scb.fatca.FatcaApi;


@Configuration
public class EntApiServiceConfig {

    @Bean
    @ConfigurationProperties(prefix = "ent-api")
    public EntApiConfig entApiConfig(ObjectMapper objectMapper) {
        EntApiConfig result = new EntApiConfig();
        result.setObjectMapper(objectMapper);
        return result;
    }

    @Bean
    public AccountsDepositsMessageServiceApi accountsDepositsMessageServiceApi(EntApiConfig entApiConfig) {
        return new AccountsDepositsMessageServiceApi(entApiConfig);
    }

    @Bean
    public ReferenceDataCommonGeneralApi referenceDataCommonGeneralApi(EntApiConfig entApiConfig) {
        return new ReferenceDataCommonGeneralApi(entApiConfig);
    }

    @Bean
    public ReferenceDataThaiPostalsApi referenceDataThaiPostalsApi(EntApiConfig entApiConfig) {
        return new ReferenceDataThaiPostalsApi(entApiConfig);
    }

    @Bean
    public ServicingAlertsRegistrationApi servicingAlertsRegistrationApi(EntApiConfig entApiConfig) {
        return new ServicingAlertsRegistrationApi(entApiConfig);
    }

    @Bean
    public AccountsDepositsV2Api accountsDepositsV2Api(EntApiConfig entApiConfig) {
        return new AccountsDepositsV2Api(entApiConfig);
    }

    @Bean
    public AccountsDepositsApi accountsDepositsApi(EntApiConfig entApiConfig) {
        return new AccountsDepositsApi(entApiConfig);
    }

    @Bean
    public AccountsCardsDebitcardsApi accountsCardsDebitcardsApi(EntApiConfig entApiConfig) {
        return new AccountsCardsDebitcardsApi(entApiConfig);
    }

    @Bean
    public ExternalAgencyFATCAApi externalAgencyFATCAApi(EntApiConfig entApiConfig) {
        return new ExternalAgencyFATCAApi(entApiConfig);
    }

    @Bean
    public FatcaApi fatcaApi(EntApiConfig entApiConfig) {
        return new FatcaApi(entApiConfig);
    }

    @Bean
    public CustProfileIndividualsApi custProfileIndividualsApi(EntApiConfig entApiConfig) {
        return new CustProfileIndividualsApi(entApiConfig);
    }

    @Bean
    public CustProfileIndividualsApiV2 custProfileIndividualsApiV2(EntApiConfig entApiConfig) {
        return new CustProfileIndividualsApiV2(entApiConfig);
    }

    @Bean
    public CustomerConsentsV2Api customerConsentApiV2(EntApiConfig entApiConfig) {
        return new CustomerConsentsV2Api(entApiConfig);
    }

    @Bean
    public CustomerConsentApi customerConsentApi(EntApiConfig entApiConfig) {
        return new CustomerConsentApi(entApiConfig);
    }

    @Bean
    public CustomerMutualFundsApi customerMutualFundApi(EntApiConfig entApiConfig) {
        return new CustomerMutualFundsApi(entApiConfig);
    }

    @Bean
    public AccountsInsurancesApi accountsInsurancesApi(EntApiConfig entApiConfig) {
        return new AccountsInsurancesApi(entApiConfig);
    }

    @Bean
    public th.co.scb.entapi.docs_vchan.AccountsInsurancesApi accountsInsurancesBeneficiariesApi(EntApiConfig entApiConfig) {
        return new th.co.scb.entapi.docs_vchan.AccountsInsurancesApi(entApiConfig);
    }

    @Bean
    public DocumentManagementVChannelApi documentManagementVChannelApi(EntApiConfig entApiConfig) {
        return new DocumentManagementVChannelApi(entApiConfig);
    }

    @Bean
    public AccountMutualFundsApi accountMutualFundsApi(EntApiConfig entApiConfig) {
        return new AccountMutualFundsApi(entApiConfig);
    }

    @Bean
    public AccountsMutualFundsApi accountsMutualFundsApi(EntApiConfig entApiConfig) {
        return new AccountsMutualFundsApi(entApiConfig);
    }

    @Bean
    public SendNotificationV3Api sendNotificationV3Api(EntApiConfig entApiConfig) {
        return new SendNotificationV3Api(entApiConfig);
    }

    @Bean
    public ServicingBranchPaymentOrderApi servicingBranchPaymentOrderApi(EntApiConfig entApiConfig) {
        return new ServicingBranchPaymentOrderApi(entApiConfig);
    }

    @Bean
    public CustomerServicesApi customerServicesApi(EntApiConfig entApiConfig) {
        return new CustomerServicesApi(entApiConfig);
    }

    @Bean
    public CustomerServicesRegistrationApi customerServicesRegistrationApi(EntApiConfig entApiConfig) {
        return new CustomerServicesRegistrationApi(entApiConfig);
    }

    @Bean
    public AccountsSecuritiesApi accountsSecuritiesApi(EntApiConfig entApiConfig) {
        return new AccountsSecuritiesApi(entApiConfig);
    }

    @Bean
    public CustProfileCustinfoApi custProfileCustinfoApi(EntApiConfig entApiConfig) {
        return new CustProfileCustinfoApi(entApiConfig);
    }

    @Bean
    public AccountsDebitcardsCompositeApi accountsDebitcardsCompositeApi(EntApiConfig entApiConfig) {
        return new AccountsDebitcardsCompositeApi(entApiConfig);
    }

    @Bean
    public AccountsCreditcardsCompositeApi accountsCreditcardsCompositeApi(EntApiConfig entApiConfig) {
        return new AccountsCreditcardsCompositeApi(entApiConfig);
    }

    @Bean
    public WebSignatureApi webSignatureApi(EntApiConfig entApiConfig) {
        return new WebSignatureApi(entApiConfig);
    }

    @Bean
    public EDSApi eDSApi(EntApiConfig entApiConfig) {
        return new EDSApi(entApiConfig);
    }

    @Bean
    public CustomerProfileCustinfoApi customerProfileCustinfoApi(EntApiConfig entApiConfig) {
        return new CustomerProfileCustinfoApi(entApiConfig);
    }

    @Bean
    public AccountsWalletMultiCurrencyApi accountsWalletMultiCurrencyApi(EntApiConfig entApiConfig) {
        return new AccountsWalletMultiCurrencyApi(entApiConfig);
    }

    @Bean
    public ExternalAgencyDETICAApi externalAgencyDETICAApi(EntApiConfig entApiConfig) {
        return new ExternalAgencyDETICAApi(entApiConfig);
    }

    @Bean
    public CustomerServicesV2Api customerServicesV2Api(EntApiConfig entApiConfig) {
        return new CustomerServicesV2Api(entApiConfig);
    }

    @Bean
    public ExternalAgencyWATCHLISTApi externalAgencyWATCHLISTApi(EntApiConfig entApiConfig) {
        return new ExternalAgencyWATCHLISTApi(entApiConfig);
    }

    @Bean
    public CustomerProfileIndividualsV3Api customerProfileIndividualsV3Api(EntApiConfig entApiConfig) {
        return new CustomerProfileIndividualsV3Api(entApiConfig);
    }

    @Bean
    public ExternalAgencyDOPAApi externalAgencyDOPAApi(EntApiConfig entApiConfig) {
        return new ExternalAgencyDOPAApi(entApiConfig);
    }

    @Bean
    public AccountsCreditcardsCompositeGWApi accountsCreditcardsCompositeGWApi(EntApiConfig entApiConfig) {
        return new AccountsCreditcardsCompositeGWApi(entApiConfig);
    }

    @Bean
    public RefdataMutualFundsV2Api refdataMutualFundsV2Api(EntApiConfig entApiConfig) {
        return new RefdataMutualFundsV2Api(entApiConfig);
    }

    @Bean
    public SupportUtilBioAuthV2Api supportUtilBioAuthV2Api(EntApiConfig entApiConfig) {
        return new SupportUtilBioAuthV2Api(entApiConfig);
    }

    @Bean
    public SupportUtilityFaceMatchingApi supportUtilityFaceMatchingApi(EntApiConfig entApiConfig) {
        return new SupportUtilityFaceMatchingApi(entApiConfig);
    }

    @Bean
    public CustProfileIndiKYCApi custProfileIndiKYCApi(EntApiConfig entApiConfig) {
        return new CustProfileIndiKYCApi(entApiConfig);
    }

    @Bean
    public TxnAuthOTPApi txnAuthOTPApi(EntApiConfig entApiConfig) {
        return new TxnAuthOTPApi(entApiConfig);
    }

    @Bean
    public CustomerConsentsApi customerConsentsApi(EntApiConfig entApiConfig) {
        return new CustomerConsentsApi(entApiConfig);
    }

    @Bean
    public MutualfundsCustomerProfilesApi mutualfundsCustomerProfilesApi(EntApiConfig entApiConfig) {
        return new MutualfundsCustomerProfilesApi(entApiConfig);
    }

    @Bean
    public CustomerProfilesInquiryApi customerProfilesInquiryApi(EntApiConfig entApiConfig) {
        return new CustomerProfilesInquiryApi(entApiConfig);
    }

    @Bean
    public MutualfundsClientProfilesApi mutualfundsClientProfilesApi(EntApiConfig entApiConfig) {
        return new MutualfundsClientProfilesApi(entApiConfig);
    }


    @Bean
    public MutualfundsOmnibusApi mutualfundsOmnibusApi(EntApiConfig entApiConfig) {
        return new MutualfundsOmnibusApi(entApiConfig);
    }

    @Bean
    public RefdataMutualFundsApi refdataMutualFundsApi(EntApiConfig entApiConfig) {
        return new RefdataMutualFundsApi(entApiConfig);
    }

    @Bean
    public MutualfundsRelativeCustomersApi mutualfundsRelativeCustomersApi(EntApiConfig entApiConfig) {
        return new MutualfundsRelativeCustomersApi(entApiConfig);
    }

    @Bean
    public MutualfundsDeleteClientProfilesApi mutualfundsDeleteClientProfilesApi(EntApiConfig entApiConfig) {
        return new MutualfundsDeleteClientProfilesApi(entApiConfig);
    }

    @Bean
    public AccountsLoansRevolvingApi accountsLoansRevolvingApi(EntApiConfig entApiConfig) {
        return new AccountsLoansRevolvingApi(entApiConfig);
    }

    @Bean
    public AccountsCardsExternalCardsApi accountsCardsExternalCardsApi(EntApiConfig entApiConfig) {
        return new AccountsCardsExternalCardsApi(entApiConfig);
    }

    @Bean
    public ReferenceDataCommonDatabaseApi referenceDataCommonDatabaseApi(EntApiConfig entApiConfig) {
        return new ReferenceDataCommonDatabaseApi(entApiConfig);
    }

    @Bean
    public ReferenceDataDatabaseCommonApi referenceDataDatabaseCommonApi(EntApiConfig entApiConfig) {
        return new ReferenceDataDatabaseCommonApi(entApiConfig);
    }

    @Bean
    public th.co.scb.entapi.securities_onboarding_aum.AccountsSecuritiesApi accountsSecuritiesAumApi(EntApiConfig entApiConfig) {
        return new th.co.scb.entapi.securities_onboarding_aum.AccountsSecuritiesApi(entApiConfig);
    }

    @Bean
    public CustomerProfileIndividualsV2Api customerProfileIndividualsV2Api(EntApiConfig entApiConfig) {
        return new CustomerProfileIndividualsV2Api(entApiConfig);
    }

    @Bean
    public CompositeAccountsDepositsApi compositeAccountsDepositsApi(EntApiConfig entApiConfig) {
        return new CompositeAccountsDepositsApi(entApiConfig);
    }

    @Bean
    public th.co.scb.entapi.emv_customer_generate_otp.EmvCustomerApi emvCustomerGenerateOtpApi(EntApiConfig entApiConfig) {
        return new th.co.scb.entapi.emv_customer_generate_otp.EmvCustomerApi(entApiConfig);
    }

    @Bean
    public th.co.scb.entapi.emv_customer_verify_otp.EmvCustomerApi emvCustomerVerifyOtpApi(EntApiConfig entApiConfig) {
        return new th.co.scb.entapi.emv_customer_verify_otp.EmvCustomerApi(entApiConfig);
    }

    @Bean
    public EmvCustomerApi emvCustomerInquiryRedListApi(EntApiConfig entApiConfig) {
        return new EmvCustomerApi(entApiConfig);
    }

    @Bean
    public CustomerAuthenticationPreAuthApi customerAuthenticationPreAuthApi(EntApiConfig entApiConfig) {
        return new CustomerAuthenticationPreAuthApi(entApiConfig);
    }

    @Bean
    public AccountsCardsV2Api accountsCardsV2Api(EntApiConfig entApiConfig) {
        return new AccountsCardsV2Api(entApiConfig);
    }

    @Bean
    public th.co.scb.entapi.accounts_cards_debitcards_activation.AccountsDebitcardsCompositeApi accountsDebitcardsCompositeApiV2(EntApiConfig entApiConfig) {
        return new th.co.scb.entapi.accounts_cards_debitcards_activation.AccountsDebitcardsCompositeApi(entApiConfig);
    }
}
