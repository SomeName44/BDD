package ru.netology.web.test;

import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferNegativeTest {

    LoginPage loginPage;
    DashboardPage dashboardPage;

    @Test
    void shouldInvalidAuthInfo() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getOtherAuthInfo(DataHelper.getAuthInfo());
        loginPage.invalidLogin(authInfo);
    }

    @Test
    void shouldInvalidVerificationCode() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var invalidVerificationCode = DataHelper.getOtherVerificationCodeFor(authInfo);
        verificationPage.invalidVerify(invalidVerificationCode);
    }

    @Test
    void shouldInvalidCard() {
        loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        var firstCard = getFirstCardNumber();
        var secondCard = getSecondCardNumber();
        var invalidCard = getThreeCardNumber();
        var firstCardBalance = dashboardPage.getCardBalance(firstCard);
        var secondCardBalance = dashboardPage.getCardBalance(secondCard);
        var amount = generateValidAmount(firstCardBalance);
        var transactionPage = dashboardPage.transferMoney(secondCard);
        transactionPage.invalidTransferOfMoney(String.valueOf(amount), invalidCard);
    }
}