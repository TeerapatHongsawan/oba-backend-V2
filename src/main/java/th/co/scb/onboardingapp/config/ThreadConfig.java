package th.co.scb.onboardingapp.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
public class ThreadConfig {

    @Bean("fixedThreadExecutor")
    public Executor fixedThreadExecutor() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(processors);
        executor.setMaxPoolSize(processors);
        executor.setTaskDecorator(it -> {
            final Map<String, String> map = MDC.getCopyOfContextMap();
            return () -> {
                if (map != null) {
                    MDC.setContextMap(map);
                } else {
                    MDC.clear();
                }

                it.run();
            };
        });
        executor.initialize();
        return executor;
    }
}
