package tests.uiapi;

import io.qameta.allure.Feature;
import io.qameta.allure.Flaky;
import io.qameta.allure.Owner;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.TestBase;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;

@Feature("Reviews")
public class ReviewsUiApiTests extends TestBase {

    @Test
    @Owner("denor1999")
    @DisplayName("[API + UI] Successful send review for club")
    @Flaky
    public void successfulSendReviewTest() {
        LoginBodyModel loginData = new LoginBodyModel(testData.loginUsername, testData.loginPassword);

        SuccessfulLoginResponseModel loginResponse = step("Send login request and check status (200)", () ->
                api.auth.login(loginData));

        String localStorage = """
                {
                  "accessToken": "%s",
                  "refreshToken": "%s",
                  "isAuthenticated": true
                }
                """.formatted(
                        loginResponse.access(),
                        loginResponse.refresh()
        );

        step("Authorize in UI via local storage", () -> {
            open("/favicon.ico");
            localStorage().setItem("book_club_auth", localStorage);
            open("/");
        });

        step("Open club and fill review form", () -> {
            $$(".filter-option").findBy(text("Мои клубы")).click();
            $$(".club-card").findBy(text("Chief Metrics Coordinator"))
                    .$(".open-btn").click();
            $(".add-review-btn").click();
            $("#assessment").setValue("5");
            $("#readPages").setValue("25");
            $("#review").setValue("A very nice book!");
            $(".save-btn").click();
        });

        step("Check created review is displayed", () ->
                $$(".review-content").shouldHave(texts("A very nice book!")));
    }
}
