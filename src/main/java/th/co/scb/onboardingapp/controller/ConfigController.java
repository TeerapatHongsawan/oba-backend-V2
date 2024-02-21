package th.co.scb.onboardingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import th.co.scb.onboardingapp.model.ConfigResponse;

import java.net.InetAddress;

@RestController
public class ConfigController {

    @Value("${server.version}")
    private String serverVersion;

    private String hostAddress;
    private String hostName;

    @Autowired
    private Environment env;

    public ConfigController() {
        InetAddress host;
        try {
            host = InetAddress.getLocalHost();
        } catch (Exception e) {
            host = InetAddress.getLoopbackAddress();
        }
        hostAddress = host.getHostAddress();
        hostName = host.getHostName();
    }

    @GetMapping("/api/config")
    public ConfigResponse config() {
        ConfigResponse res = new ConfigResponse();
        res.setHostAddress(hostAddress);
        res.setHostName(hostName);
        res.setServerVersion(serverVersion);
        res.setActiveProfile(this.env.getActiveProfiles());
        return res;
    }
}

