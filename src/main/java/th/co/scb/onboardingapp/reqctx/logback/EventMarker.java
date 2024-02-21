package th.co.scb.onboardingapp.reqctx.logback;

import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.marker.LogstashMarker;

import java.io.IOException;

public class EventMarker extends LogstashMarker {

    private static final long serialVersionUID = 1L;

    private String eventName;

    public EventMarker(String eventName) {
        super("event");
        this.eventName = eventName;
    }

    
    public static EventMarker markEvent(String eventName) {
        return new EventMarker(eventName);
    }

    @Override
    public void writeTo(JsonGenerator generator) throws IOException {
        if (this.eventName != null) {
            generator.writeStringField("eventName", this.eventName);
        }
    }
    
}
