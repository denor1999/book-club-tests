package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ClubPage {

    private final SelenideElement reviewButton = $(".add-review-btn");
    private final SelenideElement saveButton = $(".save-btn");
    private final SelenideElement assessment = $("#assessment");
    private final SelenideElement readPages = $("#readPages");
    private final SelenideElement review = $("#review");
    private final SelenideElement leaveButton = $(".leave-btn");
    private final SelenideElement error = $(".error");

    @Step("Click add review button")
    public ClubPage addReview() {
        reviewButton.click();

        return this;
    }

    @Step("Set assessment {assessmentValue}")
    public ClubPage setAssessment(String assessmentValue) {
        assessment.setValue(assessmentValue);

        return this;
    }

    @Step("Set read pages {pagesCount}")
    public ClubPage setReadPages(String pagesCount) {
        readPages.setValue(pagesCount);

        return this;
    }

    @Step("Set review text {reviewText}")
    public ClubPage addReviewText(String reviewText) {
        review.setValue(reviewText);

        return this;
    }

    @Step("Click save review button")
    public void sendReview() {
        saveButton.click();
    }

    @Step("Click leave club button")
    public void leaveClub() {
        leaveButton.click();
    }

    @Step("Check leave club error is visible")
    public void checkLeaveError() {
        error.shouldBe(visible);
    }
}
