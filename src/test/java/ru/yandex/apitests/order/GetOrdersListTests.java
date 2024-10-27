package ru.yandex.apitests.order;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.Response;
import ru.yandex.endpoints.operators.OrdersAPIOperators;
import org.junit.Test;

@Link(url = "https://qa-scooter.praktikum-services.ru/docs/#api-Orders-CreateOrder", name = "#api-Orders-GetOrdersPageByPage")
@Tag("get-orders-list")
@Epic("Sprint 7. Проект Самокат")
@Feature("Тестирование API получения списка заказов")
@DisplayName("Тест 4. Получение списка заказов")
public class GetOrdersListTests extends OrdersAPIOperators {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Тест API получения списка заказов. Ожидаемый результат - возвращается список заказов")
    public void getOrderListWithoutParamsIsSuccess() {
        Response response = getOrdersList();

        checkStatusCode(response, 200);
        checkOrdersInResponse(response);
    }
}
