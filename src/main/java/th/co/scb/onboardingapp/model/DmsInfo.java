package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DmsInfo {

	@JsonProperty("multiPages")
	private String multiPages;

	@JsonProperty("generated")
	private String generated;

	@JsonProperty("docType")
	private String docType;

	@JsonProperty("LinkFile")
	private String linkFile;

	@JsonProperty("display")
	private String display;

	@JsonProperty("docRequired")
	private String docRequired;

	@JsonProperty("sortOrder")
	private int sortOrder;

	@JsonProperty("docNameTH")
	private String docNameTH;

	@JsonProperty("docNameEN")
	private String docNameEN;

	private String accountNumber;

	private String docStatus;

	private String uploadSessionId;
	private String checklist;
	private int count;
	private String type;
	private String docCode;
	private Boolean onLoad;
}