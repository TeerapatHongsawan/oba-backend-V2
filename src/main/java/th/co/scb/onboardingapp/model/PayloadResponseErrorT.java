package th.co.scb.onboardingapp.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import th.co.scb.onboardingapp.model.entity.ErrorDetail;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PayloadResponseErrorT {

    private ErrorDetail res;

    public static ResponseEntity<?> buildResponseError(String responseBody) {
        PayloadResponseErrorT resError = parseResponseBody(responseBody);

        int http = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(http).body(resError);
    }

    private static PayloadResponseErrorT parseResponseBody(String responseBody) {
        PayloadResponseErrorT resError = new PayloadResponseErrorT();
        ErrorDetail res = new ErrorDetail();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            JsonNode errorsNode = jsonNode.get("errors");

            if (errorsNode != null && errorsNode.isArray() && errorsNode.size() > 0) {
                JsonNode firstError = errorsNode.get(0);

                res.setCode(firstError.path("code").asText());
                res.setMessage(firstError.path("message").asText());
                res.setSeveritylevel(firstError.path("severitylevel").asText());
                res.setDescription(firstError.path("description").asText());
                res.setMoreInfo(firstError.path("moreInfo").asText());
                res.setOriginalErrorCode(firstError.path("originalErrorCode").asText());
                res.setOriginalErrorDesc(firstError.path("originalErrorDesc").asText());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        resError.setRes(res);
        return resError;
    }
}
