package th.co.scb.onboardingapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Currencies {
    private String indi_Prod_Code;
    private String currencyCode;
    private String currencyDesc;
    private String curr_Name_En;
    private String curr_Name_Th;
    private String resident_Type;
}
