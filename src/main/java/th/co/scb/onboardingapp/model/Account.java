package th.co.scb.onboardingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Account {

	@JsonProperty("accountShortName")
	private String accountShortName;

	@JsonProperty("accountAddress")
	private CustomerAddress accountAddress;

	@JsonProperty("accountLongName")
	private String accountLongName;

	@JsonProperty("accountLongNameFCD")
	private String accountLongNameFCD;
	
	@JsonProperty("accountAddressText")
	private String accountAddressText;
}