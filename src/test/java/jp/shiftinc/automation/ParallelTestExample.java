package jp.shiftinc.automation;

import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.stream;

class ParallelTestExample extends ExampleTest{
    private final Logger logger = LoggerFactory.getLogger(ParallelTestExample.class);

    @ParameterizedTest(name = "allow popup when download => {0}")
    @ValueSource(booleans = {false})
    @Override
    void canDownloadCorrectFile(boolean allowPopup) throws IOException, InterruptedException {
        super.canDownloadCorrectFile(allowPopup);
    }

    @Test
    @Override
    @Disabled
    void canClickElement() throws IOException {
    }

    @TestFactory
    @Disabled
    Stream<DynamicTest> streamTest() {
        // Input data
        Integer array[] = { 1, 2, 3 };
        Iterator<Integer> inputGenerator = Arrays.asList(array).iterator();

        // Display names
        Function<Integer, String> displayNameGenerator = (
                input) -> "Data input:" + input;

        // Test executor
        ThrowingConsumer<Integer> testExecutor = (input) -> {
            logger.info("Input {}", input);
            assertTrue(input % 2 == 0);
//            String IE_ZIP = "IEDriverServer_Win32_3.150.1.zip";
//            String REFER_PATH = "src/test/resources/" + IE_ZIP;
//
//            setUp(false);
//
//            // store original file MD5
//            assertMD5(new File(REFER_PATH), false);
//
//            // access url
//            driver.get(TEST_URL);
//            attachFileToReport(driver.getScreenshotAs(OutputType.FILE), "img01","image/png","png");
//
//            // download file
//            WebElement elm = driver.findElement(By.cssSelector("a[href*=\"IEDriverServer_Win32_3\"]"));
//            elm.click();
//            Thread.sleep(5000);
//
//            // get file by using selenoid api
//            URL url = new URL(String.format("%s/download/%s/%s", baseUrl, sessionId, IE_ZIP));
//            File download = downloadFile(url);
//            attachFileToReport(download, "IEDriverServer", "application/octet-stream", "exe");
//
//            // assert file by MD5
//            assertMD5(download, true);
        };

        // Returns a stream of dynamic tests
        return stream(inputGenerator, displayNameGenerator, testExecutor);
    }
}
