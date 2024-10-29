package ru.yandex.apitests.order;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import ru.yandex.endpoints.operators.OrdersAPIOperators;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
@Link(url = "https://qa-scooter.praktikum-services.ru/docs/#api-Orders-CreateOrder", name = "#api-Orders-CreateOrder")
@Tag("create-order")
@Epic("Sprint 7. Проект Самокат")
@Feature("Тестирование API создания заказа")
@DisplayName("Тест 3. Создание заказа")
public class CreateOrderTests extends OrdersAPIOperators {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private String rentTime;
    private String deliveryDate;
    private String comment;
    private final List<String> scooterColor;
    private Integer trackId;

    public CreateOrderTests(List<String> scooterColor) {
        this.scooterColor = scooterColor;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] initParamsForTest() {
        return new Object[][] {
                {List.of()}, // не выбран
                {List.of("BLACK")}, // выбран черный
                {List.of("GREY")}, // выбран серый
                {List.of("BLACK", "GREY")}, // выбраны оба
        };
    }

    @Before
    @Step("Подготовка тестовых данных")
    public void prepareTestData() {
        this.firstName = "Иван";
        this.lastName = "Иванов";
        this.address = "Москва, Туристская, д. 23";
        this.phone = "+7 (987) 123-45-67";
        this.rentTime = "2";
        this.deliveryDate = "2024-10-25";
        this.comment = "Не звонить";
    }

    @After
    @Step("Очистка данных после теста")
    public void clearAfterTests() {
        if (trackId == null) return;

        deleteOrder(trackId);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Тест API создания заказа. Ожидаемый результат - заказ создан, возвращается track")
    public void createOrderIsSuccess() {
        Allure.parameter("Цвет самоката", scooterColor);

        Response response = createOrder(firstName, lastName, address, phone, rentTime, deliveryDate, comment, scooterColor);
        checkStatusCode(response, 201);
        checkResponseParamNotNull(response, "track");

        this.trackId = getTrack(response);
    }

}
