package th.co.scb.onboardingapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StaticPathService {

    @Value("${indi.static.appform.path}")
    private String appFormFolder;

    @Value("${indi.static.fonts.path}")
    private String fontsFolder;

    private Path appFormPath;

    private Path jasperPath;

    private Path fontsPath;

    @PostConstruct
    private void resolvePath() throws IOException {
        this.appFormPath = getPath(appFormFolder);
        this.jasperPath = this.appFormPath.resolve("jasper");
        this.fontsPath = getPath(fontsFolder);
    }

    private Path getPath(String p) throws IOException {
        if (p.startsWith("classpath:")) {
            String path = p.split(":")[1];
            File file = new ClassPathResource(path + "/resolve.txt").getFile();
            return file.toPath().getParent();
        }

        return Paths.get(p);
    }

    public Path getAppFormPath() {
        return appFormPath;
    }

    public Path getJasperPath() {
        return jasperPath;
    }

    public Path getFontsPath() {
        return fontsPath;
    }
}
