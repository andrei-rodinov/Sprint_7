package ru.yandex.endpoints.httpclients;

import io.restassured.response.Response;
import ru.yandex.apitests.objects.requestobjects.Order;
import ru.yandex.ServerAPIUrls;

import java.util.HashMap;
import java.util.Map;

public class OrdersHTTPClient extends BaseHTTPClient {
    public Response createOrder(Order order) {
        return doPostRequest(
                ServerAPIUrls.SERVER_NAME + ServerAPIUrls.CREATE_ORDER,
                order,
                "application/json"
        );
    }

    public Response deleteOrder(Integer trackId) {
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("track", trackId);

        return doPutRequest(
                ServerAPIUrls.SERVER_NAME + ServerAPIUrls.DELETE_ORDER,
                queryParams
        );
    }

    public Response getOrdersList() {
        return doGetRequest(ServerAPIUrls.SERVER_NAME + ServerAPIUrls.GET_ORDERS_LIST);
    }

}
