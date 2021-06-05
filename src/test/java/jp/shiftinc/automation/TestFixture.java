package jp.shiftinc.automation;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class TestFixture {
    final String TEST_URL = "https://www.selenium.dev/downloads/";
    RemoteWebDriver driver;
    String baseUrl;
    URL nodeUrl;
    String videoName;
    String sessionId;
    String md5;
}
