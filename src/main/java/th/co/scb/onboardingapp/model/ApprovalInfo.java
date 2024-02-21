package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class ApprovalInfo {

	@JsonProperty("approvalDetails")
	private List<ApprovalDetailsItem> approvalDetails;

	@JsonProperty("functionCode")
	private String functionCode;

	@JsonProperty("referenceNo")
	private String referenceNo;

	@JsonProperty("approvalType")
	private String approvalType;

	@JsonProperty("referenceType")
	private String referenceType;

	@JsonProperty("specialText")
	private String specialText;

	@JsonProperty("requestReason")
	private String requestReason;

	private String approvalStatus;

	private String approvalId;

	@JsonProperty("accountNameThaiOld")
	private String accountNameThaiOld;

	@JsonProperty("accountNameThaiNew")
	private String accountNameThaiNew;

	@JsonProperty("accountNameEngOld")
	private String accountNameEngOld;

	@JsonProperty("accountNameEngNew")
	private String accountNameEngNew;

	@JsonProperty("customerEmail")
	private String customerEmail;

	@JsonProperty("customerNationality")
	private String customerNationality;
}