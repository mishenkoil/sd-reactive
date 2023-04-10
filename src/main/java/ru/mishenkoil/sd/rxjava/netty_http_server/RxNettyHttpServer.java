package ru.mishenkoil.sd.rxjava.netty_http_server;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;
import java.util.*;
import ru.mishenkoil.sd.rxjava.reactive_mongo_driver.Product;
import ru.mishenkoil.sd.rxjava.reactive_mongo_driver.ReactiveMongoDriver;
import ru.mishenkoil.sd.rxjava.reactive_mongo_driver.User;
import static ru.mishenkoil.sd.rxjava.reactive_mongo_driver.Variables.*;


public class RxNettyHttpServer {
    public static void main(final String[] args) {
        HttpServer
                .newServer(PORT)
                .start((req, resp) -> {
                    Observable<String> response;
                    String name = req.getDecodedPath().substring(1);
                    Map<String, List<String>> queryParam = req.getQueryParameters();

                    switch (name) {
                        case "create_user" -> {
                            response = createUser(queryParam);
                            resp.setStatus(HttpResponseStatus.OK);
                        }
                        case "get_users" -> {
                            response = getUsers();
                            resp.setStatus(HttpResponseStatus.OK);
                        }
                        case "create_product" -> {
                            response = createProduct(queryParam);
                            resp.setStatus(HttpResponseStatus.OK);
                        }
                        case "get_products" -> {
                            response = getProducts(queryParam);
                            resp.setStatus(HttpResponseStatus.OK);
                        }
                        default -> {
                            response = Observable.just("Error: Wrong request");
                            resp.setStatus(HttpResponseStatus.BAD_REQUEST);
                        }
                    }
                    return resp.writeString(response);
                })
                .awaitShutdown();
    }

    public static Observable<String> createUser(Map<String, List<String>> queryParam) {
        try {
            int id = Integer.parseInt(queryParam.get(ID_FIELD).get(0));
            String name = queryParam.get(NAME_FIELD).get(0);
            String login = queryParam.get(LOGIN_FIELD).get(0);
            String currency = queryParam.get(CURRENCY_FIELD).get(0);
            return ReactiveMongoDriver.createUser(new User(id, name, login, currency)).map(Object::toString);
        } catch (NullPointerException error) {
            return Observable.just("Error: Incorrect registration params");
        }
    }

    public static Observable<String> getUsers() {
        return ReactiveMongoDriver.getUsers().map(Object::toString);
    }

    public static Observable<String> createProduct(Map<String, List<String>> queryParam) {
        try {
            int id = Integer.parseInt(queryParam.get(ID_FIELD).get(0));
            String name = queryParam.get(NAME_FIELD).get(0);

            String eur = queryParam.get(EUR).get(0);
            String rub = queryParam.get(RUB).get(0);
            String usd = queryParam.get(USD).get(0);
            return ReactiveMongoDriver.createProduct(new Product(id, name, rub, usd, eur)).map(Object::toString);
        } catch (NullPointerException error) {
            return Observable.just("Error: Incorrect create product params");
        }
    }

    public static Observable<String> getProducts(Map<String, List<String>> queryParam) {
        try {
            Integer id = Integer.valueOf(queryParam.get(ID_FIELD).get(0));
            return ReactiveMongoDriver.getProducts(id).map(Object::toString);
        } catch (NullPointerException error) {
            return Observable.just("Error: Incorrect get products params");
        }
    }
}