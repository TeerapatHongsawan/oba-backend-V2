package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Workflow{

	@JsonProperty("errorDesc")
	private String errorDesc;

	@JsonProperty("hardStop")
	private boolean hardStop;

	@JsonProperty("errorCode")
	private String errorCode;

	@JsonProperty("name")
	private String name;

	@JsonProperty("status")
	private String status;

	@JsonProperty("createdDate")
	private String createdDate;

	@JsonProperty("timeUsed")
	private String timeUsed;
}