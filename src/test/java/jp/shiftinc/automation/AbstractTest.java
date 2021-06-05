package jp.shiftinc.automation;

import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
@ExtendWith(SelenoidTestWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class AbstractTest<ScenarioTest> {
    private static final Logger logger = LoggerFactory.getLogger(ParallelTestExample.class);
    private TestFixture fixture;
    private RemoteWebDriver driver;

    @SuppressWarnings("unchecked")
    @BeforeAll
    void setUpClass() throws MalformedURLException {
        String clazz = ((ScenarioTest)this).getClass().getCanonicalName();
        logger.info("this is " + clazz);
        fixture = new TestFixture();
        fixture.baseUrl = System.getProperty("selenoid.base.url");
        fixture.nodeUrl = new URL(fixture.baseUrl + System.getProperty("selenoid.path"));
    }

    @BeforeEach
    void setUpTest() {
        driver = fixture.driver;
    }

    @SuppressWarnings("SameParameterValue")
    void setUp(Boolean allowPopup) {
        fixture.videoName = String.format("%s.mp4", RandomStringUtils.randomAlphanumeric(10));
        driver = new RemoteWebDriver(fixture.nodeUrl,setChromeOption(allowPopup));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        fixture.sessionId = driver.getSessionId().toString();
    }

    ChromeOptions setChromeOption(Boolean allowPopup) {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory", "/home/selenium/Downloads");
        prefs.put("download.prompt_for_download", allowPopup);
        options.setExperimentalOption("prefs", prefs);
        return options.merge(setCapabilities());
    }

    DesiredCapabilities setCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName("chrome");
        capabilities.setCapability("enableVideo", true);
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableLog", true);
        capabilities.setCapability("videoName", fixture.videoName);
        return capabilities;
    }

    @Story("Example for download file")
    @Test
    void canDownloadCorrectFile() throws IOException, InterruptedException {
        String IE_ZIP = "IEDriverServer_Win32_3.150.1.zip";
        String REFER_PATH = "src/test/resources/" + IE_ZIP;
        setUp(false);

        // store original file MD5
        assertMD5(new File(REFER_PATH), false);

        // access url
        driver.get(fixture.TEST_URL);
        attachFileToReport(driver.getScreenshotAs(OutputType.FILE), "img01","image/png","png");

        // download file
        WebElement elm = driver.findElement(By.cssSelector("a[href*=\"IEDriverServer_Win32_3\"]"));
        elm.click();
        Thread.sleep(5000);

        // get file by using selenoid api
        URL url = new URL(String.format("%s/download/%s/%s", fixture.baseUrl, fixture.sessionId, IE_ZIP));
        File download = downloadFile(url);
        attachFileToReport(download, "IEDriverServer", "application/octet-stream", "exe");

        // assert file by MD5
        assertMD5(download, true);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    void downloadVideo() throws IOException, InterruptedException {
        final String URL_FORMAT = "%s/video/%s";
        Thread.sleep(2000);
        URL url = new URL(String.format(URL_FORMAT, fixture.baseUrl, fixture.videoName));
        attachFileToReport(downloadFile(url), "video", "video/mp4", "mp4");
    }

    void downloadLog() throws IOException, InterruptedException {
        final String URL_FORMAT = "%s/logs/%s.log";
        Thread.sleep(100);
        URL url = new URL(String.format(URL_FORMAT, fixture.baseUrl, fixture.sessionId));
        attachFileToReport(downloadFile(url), "browser-log-file", "text/plain", "log");
    }

    void attachFileToReport(File file, String name, String mimeType, String ext) throws IOException {
        Allure.addAttachment(
                name,
                mimeType,
                new ByteArrayInputStream(Files.readAllBytes(Paths.get(file.getAbsolutePath()))),
                ext
        );
    }

    void assertMD5(File file, Boolean compare) throws IOException {
        if (compare) {
            assertEquals(fixture.md5, DigestUtils.md5Hex(new FileInputStream(file)));
        } else {
            fixture.md5 = DigestUtils.md5Hex(new FileInputStream(file));
        }
    }

    File downloadFile(URL url) throws IOException {
        String path = url.getPath();
        String downloadFile = "build/tmp/" + path.substring(path.lastIndexOf("/") + 1);
        try (DataInputStream in = new DataInputStream(url.openStream()) ;
            DataOutputStream out = new DataOutputStream(new FileOutputStream(downloadFile))) {
                byte[] buf = new byte[8192];
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.flush();
            } catch (Exception e) {
            throw e;
        }
        return new File(downloadFile);
    }
}
