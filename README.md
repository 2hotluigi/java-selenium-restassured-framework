# Java Selenium + REST Assured BDD Framework

[![Tests](https://github.com/2hotluigi/java-selenium-restassured-framework/actions/workflows/tests.yml/badge.svg)](https://github.com/2hotluigi/java-selenium-restassured-framework/actions/workflows/tests.yml)
[![Allure Report](https://img.shields.io/badge/report-Allure-orange)](https://2hotluigi.github.io/java-selenium-restassured-framework/)

UI and API test automation framework for the public demo store
[automationexercise.com](https://automationexercise.com), built with **Java 17, Selenium WebDriver,
Cucumber (BDD), TestNG and REST Assured**, reported with **Allure** and running on **GitHub Actions**.

📊 **[Latest Allure report](https://2hotluigi.github.io/java-selenium-restassured-framework/)**

## What it shows

| Area | Implementation |
|------|----------------|
| UI testing | Selenium WebDriver 4, Page Object Model, explicit waits only (no `Thread.sleep`) |
| API testing | REST Assured clients, JSON Schema validation, Jackson models, positive and negative cases |
| BDD | Cucumber features in Gherkin, Scenario Outlines, data tables, tags (`@smoke`, `@regression`, `@ui`, `@api`) |
| Hybrid tests | UI scenarios create their test users through the API: faster and independent |
| Parallel execution | Scenarios run in parallel with TestNG; state is scenario-scoped via PicoContainer DI |
| Test data | Unique users generated with Datafaker, cleaned up automatically after each scenario |
| Cross-browser | Chrome, Edge and Firefox, local or on Selenium Grid (Docker) |
| Reporting | Allure with steps, API request/response attachments and screenshots on failure |
| CI/CD | GitHub Actions on every push and PR, weekly schedule, manual runs by tag, report published to GitHub Pages |

## Tech stack

Java 17 · Maven · Selenium 4 · Cucumber 7 · TestNG · REST Assured 5 · AssertJ · Jackson · Datafaker · Allure · GitHub Actions · Docker

## Project structure

```
src/test/java/com/luisabrego/automation
├── api/            REST Assured clients (ProductsApi, UserApi) and response models
├── config/         ConfigReader: config.properties < env variables < -D system properties
├── context/        TestContext shared between steps (driver, last response, test user)
├── driver/         DriverFactory: local / remote browsers, headless, ad blocking
├── hooks/          Screenshot on failure, browser shutdown, test data clean-up
├── pages/          Page Objects (BasePage holds the synchronized interactions)
├── runners/        TestNG + Cucumber runner with parallel scenarios
├── steps/          Step definitions (api, ui, common)
└── utils/          Test data factory
src/test/resources
├── features/       Gherkin features (api/, ui/)
├── schemas/        JSON Schemas for contract validation
└── config.properties
```

## Running the tests

Requirements: JDK 17+, Maven 3.9+ and Chrome (drivers are downloaded automatically by Selenium Manager).

```bash
# All scenarios
mvn test

# Only API or only UI
mvn test -Dcucumber.filter.tags="@api"
mvn test -Dcucumber.filter.tags="@ui"

# Smoke suite, headless, 4 parallel threads
mvn test -Dcucumber.filter.tags="@smoke" -Dheadless=true -Dthreads=4

# Another browser
mvn test -Dbrowser=firefox
mvn test -Dbrowser=edge
```

### Selenium Grid with Docker

```bash
docker compose up -d
mvn test -Dgrid.url=http://localhost:4444
```

### Allure report

```bash
mvn allure:serve
```

## Design notes

- **The API under test always returns HTTP 200.** The real status is in the `responseCode` field of the
  body, and the content type is `text/html`. The framework checks both and parses the body as JSON
  explicitly (`ApiResponses`).
- **Ads on the demo site** cover elements and open random pop-up pages. Chromium browsers resolve the
  ad domains to localhost, which removes that source of flakiness without touching the page.
- **API-first setup.** UI scenarios that need an existing account create it through the API and
  delete it in an `@After` hook, so every scenario is independent and can run in parallel.

## Author

**Luis Abrego**, QA Automation Engineer / SDET · [LinkedIn](https://www.linkedin.com/in/luis-abrego-h)
