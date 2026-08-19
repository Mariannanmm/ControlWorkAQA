# ControlWorkAQA
тести:
- Завдання 1 — UI (Selenide) для [demoblaze.com](https://www.demoblaze.com/)
- Завдання 2 — API (Rest Assured) для carsAPI (`http://bigbird.space/carsAPI/v2`)

---

## Стек
- Java 21, Maven
- UI: Selenide 7.17, Selenium 4.46, TestNG 7.12
- API: Rest Assured 6.0, Jackson 2, Lombok
- Звіти/логування: Allure 2.35 (allure-testng, allure-selenide, allure-rest-assured)

---

## Структура проєкту
```
src/main/java/org/
── config/            Task 1: TestBase (конфіг + Allure listener), PageTools (базові дії)
── helpers/           Constants, CustomConditions (UI), Specifications (API)
── pages/             Task 1: Page Objects (Singleton) + модель Item
── api/
    ── pojos/         Task 2: POJO-моделі — User, Car, CarPaginated, Token

src/main/resources/    logback.xml, allure.properties

src/test/java/tests/
── ui/                Task 1: CriticalTest, ExtendedTest
── api/               Task 2: AuthTest, CarsTest

docs/
── allure-maven-report/   згенерований Allure-звіт
── screenshots/           скріншоти звіту
```

---

## Завдання 1 — UI (demoblaze.com)

Critical (`tests.ui.CriticalTest`)
1. `checkHomeItemMatchesDetail` — товар з головної збігається з деталкою
2. `addProductToCart` — додавання товару в кошик
3. `placeOrder` — оформлення замовлення (Thank you for your purchase!)
4. `signUpNewUser` — реєстрація нового юзера
5. `loginExistingUser` — успішний логін

Extended (`tests.ui.ExtendedTest`)
1. `loginWithWrongPassword` — невірний пароль → `Wrong password.`
2. `loginWithNonexistentUser` — неіснуючий юзер → `User does not exist.`
3. `signUpExistingUser` — реєстрація існуючого → `This user already exist.`
4. `placeOrderWithoutCredentials` — порожня форма → `Please fill out Name and Creditcard.`
5. `removeProductFromCart` — видалення товару з кошика

Знайдений баг
`placeOrderWithEmptyCart_shouldBeBlocked` (вимкнений через `enabled = false`):
demoblaze дозволяє оформити замовлення з порожнім кошиком(«Thank you for your purchase!»,
`Amount: 0 USD`). Тест написаний на очікувану(правильну) поведінку й вимкнений, щоб
не «охороняти» дефект. Увімкнути, коли баг виправлять.

---

## Завдання 2 — API (carsAPI)

База: `http://bigbird.space/carsAPI/v2`

Патерни:
- POJO — серіалізація тіла (`.body(user)`) і десеріалізація відповіді (`.extract().as(User.class)`).
  `@JsonIgnoreProperties(ignoreUnknown = true)` — не падати на зайвих полях; `@JsonInclude(NON_NULL)` —
  не слати null-поля; `@JsonProperty` — мапінг snake_case - camelCase.
- RestAssured specifications — `requestSpecification(url)` (baseUri + JSON + фільтр `AllureRestAssured`),
  `responseSpecification(statusCode)`.
- Логування в Allure — фільтр `AllureRestAssured` плюс `.log().all()` у тестах для консолі.

### Тести
`tests.api.AuthTest`:
- `authFlow` — створити юзера (`POST /users`, 201) - отримати токен (`POST /auth`, 200, `access`)
    - перевірити `GET /auth/me` з `Authorization: Bearer <access>` (200)

`tests.api.CarsTest` (усі cars-ендпоінти потребують Bearer-токена, який готується в `@BeforeClass`):
- `getCarsList` — `GET /cars` - 200 (CarPaginated)
- `createCar` — `POST /cars` - 201
- `getCarById` — `GET /cars/{id}` - 200
- `updateCar` — `PUT /cars/{id}` - 200 (повне)
- `partialUpdateCar` — `PATCH /cars/{id}` - 200 (часткове)
- `deleteCar` — `DELETE /cars/{id}` - 204, потім `GET` - 404


## Як запустити

### Тести
bash
mvn clean test                   

### Allure-звіт
bash
mvn allure:serve       звіт у браузері 
mvn allure:report      звіт у target/site/allure-maven-plugin/

спочатку `mvn clean test` (згенерує результати), потім `mvn allure:serve`.
 Збережений звіт відкривати через `mvn allure:serve` — прямий `file://` не працює.

У звіті: 4 suites (`CriticalTest`, `ExtendedTest`, `AuthTest`, `CarsTest`);

Скріншоти звіту UI: `docs/screenshots/`

---

## Примітки
- Тести запускати звичайним Run, не Debug: агент відладчика IntelliJ інколи дає
  ClassCircularityError якщо запустити через Debug.
- Headless вмикається прапорцем `-Dheadless=true` (за замовчуванням браузер видимий).
