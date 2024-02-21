package th.co.scb.onboardingapp.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.openunirest.http.Unirest;
import jakarta.annotation.PostConstruct;
import kotlin.jvm.internal.Intrinsics;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UnirestConfig {

    @NotNull
    private final ObjectMapper objectMapper;
    private final int socketTimeout;

    public UnirestConfig(@NotNull ObjectMapper objectMapper, @Value("${unirest.socket-timeout}") int socketTimeout) {
        super();
        Intrinsics.checkNotNullParameter(objectMapper, "objectMapper");
        this.objectMapper = objectMapper;
        this.socketTimeout = socketTimeout;
    }

    @PostConstruct
    public void init() {
        Unirest.setTimeouts(10000, this.socketTimeout);
        Unirest.setObjectMapper((io.github.openunirest.http.ObjectMapper)(new io.github.openunirest.http.ObjectMapper() {
            @NotNull
            @SneakyThrows
            public String writeValue(@Nullable Object value) {
                String var10000 = th.co.scb.onboardingapp.config.UnirestConfig.this.objectMapper.writeValueAsString(value);
                Intrinsics.checkNotNullExpressionValue(var10000, "objectMapper.writeValueAsString(value)");
                return var10000;
            }

            @SneakyThrows
            public Object readValue(@NotNull String json, @NotNull Class cls) {
                Intrinsics.checkNotNullParameter(json, "json");
                Intrinsics.checkNotNullParameter(cls, "cls");
                return UnirestConfig.this.objectMapper.readValue(json, cls);
            }
        }));
    }


}
