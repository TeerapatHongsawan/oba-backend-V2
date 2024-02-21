package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApprovalDetailsItem {

	@JsonProperty("updatedTime")
	private String updatedTime;

	@JsonProperty("createdTime")
	private String createdTime;

	@JsonProperty("checker")
	private String checker;

	@JsonProperty("status")
	private String status;
}