package jp.shiftinc.automation;

import io.qameta.allure.Allure;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
