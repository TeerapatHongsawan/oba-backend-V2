package th.co.scb.onboardingapp.helper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class StringToListDeSerializer extends JsonDeserializer<List> {

    public List deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String[] array = jsonParser.readValueAs(String[].class);
        return Arrays.asList(array);
    }
}