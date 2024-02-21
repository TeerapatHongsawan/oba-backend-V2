package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OriginalExceptionDetail {

	private String originateFrom;
	
	private String onOperation;
	
	private String resultInCode;
	
	private String withDescription;
	
	private String requestUID;
	
	/**
	 * alternative to lombok builder, as modern browser such as intelliJ display parameter name as visual hint
	 * in the editor, this version might be enough to make it readable without using lombok builder
	 * 
	 * @param from
	 * @param on
	 * @param resultCode
	 * @param description
	 * @return
	 */
	public static OriginalExceptionDetail create(String from, String on, String resultCode, String description) {
		return OriginalExceptionDetail.builder()
				.originateFrom(from)
					.onOperation(on)
						.resultInCode(resultCode)
							.withDescription(description)
							.build();
	}
	
}
