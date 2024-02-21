package th.co.scb.onboardingapp.service;

import com.google.common.base.Strings;
import com.google.common.net.InetAddresses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SecurityLogger {
    private String hostAddress;
    private String hostName;

    public SecurityLogger() {
        InetAddress host;
        try {
            host = InetAddress.getLocalHost();
        } catch (Exception e) {
            host = InetAddress.getLoopbackAddress();
        }
        hostAddress = host.getHostAddress();
        hostName = host.getHostName();
    }

    // should move to util later
    public static String logForgingSafeXForwarderForHttpHeader(String addresses) {
        String result = null;
        // need to parse in order to avoid LogForging security vulnerability [codescan]
        try {
            String[] addressStringArray = addresses.split(",");
            if (addressStringArray.length > 0) {
                InetAddresses.forString(addressStringArray[0].trim());
                result = addressStringArray[0].trim();
            }
        } catch (Exception e) {
            log.info("LogForging security vulnerability : {}", e.getMessage());
        }

        return result;
    }

    public void log(String appName, String username, String action, HttpServletRequest request, String deviceId, boolean success, String message) {
        List<String> list = new ArrayList<>();
        list.add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
        list.add(appName);
        list.add(action);
        String xForwardHeader = request.getHeader("X-FORWARDED-FOR");
        String addresses = this.logForgingSafeXForwarderForHttpHeader(xForwardHeader);

        if (Strings.isNullOrEmpty(addresses)) {
            addresses = request.getRemoteAddr();
        }
        list.add(addresses);
        list.add(deviceId);
        list.add(username);
        list.add("");
        list.add(hostAddress);
        list.add(hostName);
        list.add(username);
        list.add("");
        list.add(success ? "Success" : "Failure");
        list.add(message);
        list.add("");

        String text = list.stream()
                .map(Strings::nullToEmpty)
                .collect(Collectors.joining("|"));
        sendDataSocket(text);
    }

    @Value("${syslog.ipaddress}")
    private String ipaddress;
    @Value("${syslog.port}")
    private String port;
    @Value("${syslog.connection-timeout}")
    private String connectionTimeout;
    @Value("${syslog.read-timeout}")
    private String readTimeout;

    private void sendDataSocket(String text) {
        try (Socket socket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ipaddress, Integer.parseInt(port));
            socket.setSoTimeout(Integer.parseInt(readTimeout));
            socket.connect(socketAddress, Integer.parseInt(connectionTimeout));
            writeDataSocket(socket, text);
        } catch (IOException e) {
            log.error("error syslog " + ipaddress + ":" + e.toString());
        }
    }

    private void writeDataSocket(Socket socket, String text) {
        try (PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            writer.println(text);
            log.info("syslog by socket: " + text);
        } catch (IOException e) {
            log.error("error write syslog " + ipaddress + ":" + e.toString());
        }
    }

}

