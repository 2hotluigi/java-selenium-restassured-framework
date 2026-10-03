package com.luisabrego.automation.steps.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.luisabrego.automation.context.TestContext;
import com.luisabrego.automation.pages.AccountStatusPage;
import com.luisabrego.automation.pages.HomePage;
import com.luisabrego.automation.pages.LoginPage;
import com.luisabrego.automation.pages.SignupPage;
import com.luisabrego.automation.utils.TestDataFactory;
import com.luisabrego.automation.utils.UserData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Registration, login and logout through the UI. */
public class AccountSteps {

    private final TestContext context;

    public AccountSteps(TestContext context) {
        this.context = context;
    }

    @Given("I am on the home page")
    public void iAmOnTheHomePage() {
        assertThat(homePage().open().isLoaded()).as("home page loaded").isTrue();
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage().open();
    }

    @When("I start the signup with a new user")
    public void iStartTheSignupWithANewUser() {
        UserData user = TestDataFactory.newUser();
        context.setUser(user);
        context.registerForCleanUp(user);
        homePage().goToSignupLogin().startSignup(user.name(), user.email());
    }

    @When("I start the signup with the registered user's email")
    public void iStartTheSignupWithTheRegisteredUsersEmail() {
        loginPage().startSignup("Another User", context.getUser().email());
    }

    @When("I fill in the account information")
    public void iFillInTheAccountInformation() {
        new SignupPage(context.getDriver()).fillAccountInformation(context.getUser());
    }

    @When("I submit the account creation form")
    public void iSubmitTheAccountCreationForm() {
        new SignupPage(context.getDriver()).createAccount();
    }

    @Then("I should see the {string} message")
    public void iShouldSeeTheMessage(String expected) {
        assertThat(accountStatusPage().heading()).isEqualToIgnoringCase(expected);
    }

    @When("I continue to the home page")
    public void iContinueToTheHomePage() {
        accountStatusPage().continueToHome();
    }

    @Then("I should be logged in")
    public void iShouldBeLoggedIn() {
        assertThat(homePage().loggedInUsername()).isEqualTo(context.getUser().name());
    }

    @When("I delete my account")
    public void iDeleteMyAccount() {
        homePage().deleteAccount();
    }

    @When("I log in with the registered user's credentials")
    public void iLogInWithTheRegisteredUsersCredentials() {
        UserData user = context.getUser();
        loginPage().loginAs(user.email(), user.password());
    }

    @When("I log in with the registered user's email and password {string}")
    public void iLogInWithTheRegisteredUsersEmailAndPassword(String password) {
        loginPage().loginAs(context.getUser().email(), password);
    }

    @When("I log in with email {string} and password {string}")
    public void iLogInWithEmailAndPassword(String email, String password) {
        loginPage().loginAs(email, password);
    }

    @Then("I should see the login error {string}")
    public void iShouldSeeTheLoginError(String expected) {
        assertThat(loginPage().loginError()).isEqualTo(expected);
    }

    @Then("I should see the signup error {string}")
    public void iShouldSeeTheSignupError(String expected) {
        assertThat(loginPage().signupError()).isEqualTo(expected);
    }

    @When("I log out")
    public void iLogOut() {
        homePage().logout();
    }

    @Then("I should be on the login page")
    public void iShouldBeOnTheLoginPage() {
        assertThat(loginPage().isLoaded()).as("login page loaded").isTrue();
    }

    private HomePage homePage() {
        return new HomePage(context.getDriver());
    }

    private LoginPage loginPage() {
        return new LoginPage(context.getDriver());
    }

    private AccountStatusPage accountStatusPage() {
        return new AccountStatusPage(context.getDriver());
    }
}
