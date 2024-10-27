package ru.yandex.endpoints.httpclients;

import ru.yandex.apitests.objects.requestobjects.Courier;
import io.restassured.response.Response;
import ru.yandex.ServerAPIUrls;

public class CourierHTTPClient extends BaseHTTPClient {
    public Response createCourier(Courier courier) {
        return doPostRequest(
                ServerAPIUrls.SERVER_NAME + ServerAPIUrls.CREATE_COURIER,
                courier,
                "application/json"
        );
    }

    public Response loginCourier(Courier courier) {
        return doPostRequest(
                ServerAPIUrls.SERVER_NAME + ServerAPIUrls.LOGIN_COURIER,
                courier,
                "application/json"
        );

    }

    public Response deleteCourier(Integer idCourier) {
        return doDeleteRequest(ServerAPIUrls.SERVER_NAME + ServerAPIUrls.DELETE_COURIER + idCourier);
    }
}
