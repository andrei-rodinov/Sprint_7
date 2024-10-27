package ru.yandex.apitests.courier;

import io.qameta.allure.*;
import io.qameta.allure.junit4.*;
import io.restassured.response.Response;
import ru.yandex.endpoints.operators.CourierAPIOperators;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Link(url = "https://qa-scooter.praktikum-services.ru/docs/#api-Courier-Login", name = "#api-Courier-Login")
@Tag("login-courier")
@Epic("Sprint 7. Проект Самокат")
@Feature("Тестирование API входа курьера в систему")
@DisplayName("Тест 2. Вход курьера в систему")

public class LoginCourierTests extends CourierAPIOperators {
    private String login;
    private String password;
    private String firstName;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Английский алфавит для логина и имени
    private static final SecureRandom RANDOM = new SecureRandom(); // Генератор случайных чисел

    @Before
    @Step("Подготовка данных для тестирования")
    public void prepareTestData() {
        this.login = generateRandomString(10); // Логин - 10 случайных букв
        this.password = generateRandomNumberString(4); // Пароль - 4 случайные цифры в формате String
        this.firstName = generateRandomString(10); // Имя - 10 случайных букв

        createCourier(login, password, firstName);
    }
    private String generateRandomString(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(ALPHABET.length());
            result.append(ALPHABET.charAt(index));
        }
        return result.toString();
    }
    private String generateRandomNumberString(int length) {
        int number = ThreadLocalRandom.current().nextInt((int) Math.pow(10, length - 1), (int) Math.pow(10, length));
        return String.valueOf(number); // Преобразование числа в строку
    }

    @After
    @Step("Очистка данных после теста")
    public void clearAfterTests() {
        Integer idCourier = getIdCourier(loginCourier(login, password));
        if (idCourier == null) return;

        deleteCourier(idCourier);
    }

    @Test
    @DisplayName("Логин курьера в систему")
    @Description("Тест API входа курьера в систему. Ожидаемый результат - вход осуществлен, возвращается ID курьера")
    public void loginCourierIsSuccess() {
        Response response = loginCourier(login,password);

        checkStatusCode(response, 200);
        checkCourierIDNotNull(response);
    }

    @Test
    @DisplayName("Вход курьера в систему без данных (логин, пароль)")
    @Description("Тест API входа курьера без данных (логин, пароль). Ожидаемый результат - вход не осуществлен")
    public void loginCourierMissingAllParamsIsFailed() {
        Response response = loginCourier("", "");

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Вход курьера в систему без логина")
    @Description("Тест API входа курьера без логина. Ожидаемый результат - вход не осуществлен")
    public void loginCourierMissingLoginParamIsFailed() {
        Response response = loginCourier("", password);

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Вход курьера в систему без пароля")
    @Description("Тест API входа курьера без пароля. Ожидаемый результат - вход не осуществлен")
    public void loginCourierMissingPasswordParamIsFailed() {
        Response response = loginCourier(login, "");

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Вход курьера в систему с неверным логином")
    @Description("Тест API входа курьера с неверным логином. Ожидаемый результат - вход не осуществлен")
    public void loginCourierIncorrectLoginParamIsFailed() {
        Response response = loginCourier(login + "a", password);

        checkStatusCode(response, 404);
        checkMessage(response, "message", "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Вход курьера в систему с неверным паролем")
    @Description("Тест API входа курьера с неверным паролем. Ожидаемый результат - вход не осуществлен")
    public void loginCourierIncorrectPasswordParamIsFailed() {
        Response response = loginCourier(login, password + "1");

        checkStatusCode(response, 404);
        checkMessage(response, "message", "Учетная запись не найдена");
    }
}
