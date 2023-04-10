package ru.mishenkoil.sd.rxjava.reactive_mongo_driver;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.Success;
import org.bson.Document;
import rx.Observable;
import static ru.mishenkoil.sd.rxjava.reactive_mongo_driver.Variables.*;


public class ReactiveMongoDriver {
    public static MongoClient client = createMongoClient();

    private static MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }

    private static MongoCollection<Document> getUsersCollection() {
        return client
                .getDatabase(DATABASE)
                .getCollection(USERS_COLLECTION);
    }

    private static MongoCollection<Document> getProductsCollection() {
        return client
                .getDatabase(DATABASE)
                .getCollection(PRODUCTS_COLLECTION);
    }

    public static Observable<Success> createUser(User user) {
        return getUsersCollection().insertOne(user.getDocument());
    }

    public static Observable<Success> createProduct(Product product) {
        return getProductsCollection().insertOne(product.getDocument());
    }

    public static Observable<String> getUsers() {
        return getUsersCollection()
                .find()
                .toObservable()
                .map(d -> new User(d).toString())
                .reduce((u1, u2) -> u1 + ", " + u2);
    }

    public static Observable<String> getProducts(Integer id) {
        return getUsersCollection()
                .find()
                .toObservable()
                .map(User::new)
                .filter(user -> user.getId() == id)
                .firstOrDefault(new User(-1, null, null, USD))
                .flatMap(user -> getProductsCollection()
                        .find().toObservable()
                        .map(document -> new Product(document).toString(user.getCurrency()))
                )
                .reduce((product1, product2) -> product1 + ", " + product2);
    }
}