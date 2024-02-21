package th.co.scb.onboardingapp.model.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;



import java.io.IOException;
import java.util.Set;


@Data
public class AccessMappingTypeEntity {

    /**
     *
     * ("branch","wealth","wealth_specialist","dsa","private","ssme","bulk")
     *
     */
    private String type;

    /**
     *
     * ["investment", "travel_card", "payment_account_deduct", "payment_cash", "payment_atm", "all_saving"]
     *
     */
    //@JsonSerialize(using = StringToSetSerializer.class)
    private String functions;
    private boolean loginActive;
}
