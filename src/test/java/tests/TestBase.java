package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.App;
import helpers.AllureAttachments;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;

import static io.qameta.allure.Allure.step;

public class TestBase {
    @BeforeAll
    static void beforeAll() {

        RestAssured.baseURI = App.config.baseURI();
        Configuration.baseUrl = App.config.baseUrl();
        Configuration.browser = App.config.browser();
        Configuration.browserVersion = App.config.browserVersion();

        String selenoidUrl = App.config.selenoidUrl();
            if (selenoidUrl != null) {
                step("Remote web driver setup", () -> {
                Configuration.remote = "https://" + App.config.selenoidLogin() + ":" + App.config.selenoidPassword() + "@selenoid.autotests.cloud/wd/hub";
        });
      }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);
        Configuration.browserCapabilities = capabilities;
    }

    @BeforeEach
    public void beforeEach() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void afterEach() {
        AllureAttachments.screenshotAs("Screenshot");
        AllureAttachments.pageSource();
        AllureAttachments.browserConsoleLogs();
        AllureAttachments.addVideo();
        Selenide.closeWebDriver();
    }
}
