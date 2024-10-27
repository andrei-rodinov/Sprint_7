package ru.yandex.apitests.courier;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import ru.yandex.endpoints.operators.CourierAPIOperators;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Link(url = "https://qa-scooter.praktikum-services.ru/docs/#api-Courier-CreateCourier", name = "#api-Courier-CreateCourier")
@Tag("create-courier")
@Epic("Sprint 7. Проект Самокат")
@Feature("Тестирование API создания курьера")
@DisplayName("Тест 1 - Создание курьера")
public class CreateCourierTests extends CourierAPIOperators {
    private String login;
    private String password;
    private String firstName;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Английский алфавит для логина и имени
    private static final SecureRandom RANDOM = new SecureRandom(); // Генератор случайных чисел

    public CreateCourierTests() {
    }

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        this.login = generateRandomString(10); // Логин - 10 случайных букв
        this.password = generateRandomNumberString(4); // Пароль - 4 случайные цифры в формате String
        this.firstName = generateRandomString(10); // Имя - 10 случайных букв
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
    public void cleanAfterTests() {
        if (!isCourierCreated()) return;

        Integer idCourier = getIdCourier(loginCourier(login, password));

        if (idCourier != null) {
            deleteCourier(idCourier);
        }

        setIsCreated(false);
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Тест API создания нового курьера. Ожидаемый результат - создан новый курьер")
    public void createNewCourierIsSuccess() {
        Response response = createCourier(login, password, firstName);
        setIsCreated(isCourierCreated(response, 201));

        checkStatusCode(response, 201);
        checkMessage(response, "ok", true);
    }

    @Test
    @DisplayName("Создание курьеров с одинаковыми данными")
    @Description("Тест возможности создания двух курьеров с одинаковыми данными. Ожидаемый результат - одинаковых курьеров создать нельзя")
    public void createSameCouriersIsFailed(){
        // Создание первого курьера
        Response response = createCourier(login, password, firstName);
        setIsCreated(isCourierCreated(response, 201));

        checkStatusCode(response, 201);
        checkMessage(response, "ok", true);

        // Создание второго курьера
        response = createCourier(login, password, firstName);

        checkStatusCode(response, 409);
        checkMessage(response, "message", "Этот логин уже используется. Попробуйте другой."); // ответ не из документации
    }


    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Тест API создания курьера без логина. Ожидаемый результат - курьера без логина создать нельзя")
    public void createCourierMissingLoginParamIsFailed() {
        Response response = createCourier("", password, firstName);
        setIsCreated(isCourierCreated(response, 201));

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Тест API создания курьера без пароля. Ожидаемый результат - курьера без пароля создать нельзя")
    public void createCourierMissingPasswordParamIsFailed() {
        Response response = createCourier(login, "", firstName);
        setIsCreated(isCourierCreated(response, 201));

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без данных (логина, пароля, имени)")
    @Description("Тест API создания курьера без данных. Ожидаемый результат - курьера без данных создать нельзя")
    public void createCourierMissingAllParamsIsFailed() {
        Response response = createCourier("", "", "");
        setIsCreated(isCourierCreated(response, 201));

        checkStatusCode(response, 400);
        checkMessage(response, "message", "Недостаточно данных для создания учетной записи");
    }
}