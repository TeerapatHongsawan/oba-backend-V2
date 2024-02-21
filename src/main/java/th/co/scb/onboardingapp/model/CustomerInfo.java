package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import th.co.scb.entapi.individuals.model.PersonalMarketingInformation;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfo extends IndividualInfo {

    /**
     * Business type
     */
    @JsonProperty("occupationIsicDetail")
    private String occupationIsicDetail;

    /**
     * Store original customer info delta, if exists means value has been changed
     */
    @JsonProperty("legacy")
    private IndividualInfo legacy;

    /**
     * Y = Existing customer
     * N = New customer
     */
    @JsonProperty("isExistingCustomer")
    private String isExistingCustomer;

    @JsonProperty("caseId")
    private String caseId;

    /**
     * Home phone, Office phone, Mobile phone, Email
     */
    @JsonProperty("contactChannels")
    private List<CustomerContactChannel> contactChannels;

    /**
     * Adding mobile numbers verification method
     */
    @JsonProperty("addingMobileNoVerification")
    private String addingMobileNoVerification;

    /**
     * Extra info for marketing
     */
    @JsonProperty("marketingInfo")
    private PersonalMarketingInformation marketingInfo;

    /**
     * Photo in base64 in png format
     */
    @JsonProperty("photo")
    private String photo;

    /**
     * Flags for customer (reference to other systems)
     */
    @JsonProperty("identifications")
    private List<IdentificationsItem> identifications;

    /**
     * Opening passbook information
     */
    @JsonProperty("account")
    private Account account;

    /**
     * All about investment products
     */
    @Valid
    @JsonProperty("investment")
    private Investment investment;

    private int age;

    /**
     * Existing saving accounts
     */
    private List<DepositAccount> customerAccounts;

    /**
     * Opening Fast easy details
     */
    private FastEasy fasteasy;

    /**
     * Opening Debit card detail
     */
    private DebitCardService debitCardService;

    /**
     * Opening Travel card detail
     */
    private TravelCard travelCard;

    /**
     * Number used for OTP
     */
    @NotNull
    private String otpPhoneNumber;

    /**
     * Information about ID Card
     */
    private CardInfo cardInfo;

    /**
     * Watchlist information
     */
    private List<WatchlistResponse> watchlist;

    /**
     * Flags whether customer info is dirty
     */
    private boolean hasCIChanged;

    /**
     * Current NDID flag
     */
    private String personalEnrollType;

    /**
     * nationality for foreigner
     */
    private String customerNationality;

    /**
     * Channel to pay opening fee
     */
    private String paymentFundType; //04 - account deduct, 01 - cash, 06 - atm

    /**
     * In case of 04 - account deduct, specify account number
     */
    private String paymentAccountNumber;

    private List<LicenseInfo> licenseInfos;

    private ChangeSignature changeSignature;

    @JsonProperty("otpEmail")
    private List<OtpEmail> otpEmail;
}
