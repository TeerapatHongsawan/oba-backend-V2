package th.co.scb.onboardingapp.helper;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.Map;

public class ObaHelper {

    private static int rmIdLength = 30;

    private ObaHelper() {
    }

    public static String formatRmId(String rmId) {
        if (rmId.length() < rmIdLength) {
            String prefix = "0014";
            return String.format("%s%26s", prefix, rmId).replace(" ", "0");
        }
        return rmId;
    }

    public static HttpHeaders getHttpHeaders(String apiSecret, String sourceSystem, String apikey, String acceptLanguage, Map<String,String> otherHeader){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apisecret", apiSecret);
        headers.set("resourceOwnerID", sourceSystem);
        headers.set("sourceSystem", sourceSystem);
        headers.set("apikey", apikey);
        headers.set("requestUID", UUIDHelper.generateUUID());
        if(StringUtils.hasText(acceptLanguage)) {
            headers.set("acceptLanguage", acceptLanguage);
        }
        // Add other headers from the map
        if (otherHeader != null) {
            for (Map.Entry<String, String> entry : otherHeader.entrySet()) {
                if (entry.getValue() != null) {
                    headers.set(entry.getKey(), entry.getValue());
                }
            }
        }
        return headers;
    }

    public static HttpHeaders httpServletResponseToHttpHeaders(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();

        // Extract headers from HttpServletResponse
        Collection<String> headerNames = response.getHeaderNames();
        if (!CollectionUtils.isEmpty(headerNames)) {
            for (String headerName : headerNames) {
                Collection<String> headerValues = response.getHeaders(headerName);
                for (String headerValue : headerValues) {
                    headers.add(headerName, headerValue);
                }
            }
        }
        return headers;
    }
}
