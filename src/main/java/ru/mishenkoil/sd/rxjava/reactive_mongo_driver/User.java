package ru.mishenkoil.sd.rxjava.reactive_mongo_driver;

import org.bson.Document;
import static ru.mishenkoil.sd.rxjava.reactive_mongo_driver.Variables.*;

public class User {
    private final int id;
    private final String name;
    public final String login;
    private final String currency;

    public User(Document doc) {
        this(doc.getInteger(ID_FIELD), doc.getString(NAME_FIELD), doc.getString(LOGIN_FIELD), doc.getString(CURRENCY_FIELD));
    }

    public User(int id, String name, String login, String currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = currency;
    }

    public Document getDocument() {
        return new Document(ID_FIELD, id).append(NAME_FIELD, name).append(LOGIN_FIELD, name).append(CURRENCY_FIELD, currency);
    }

    public int getId() {
        return this.id;
    }

    public String getCurrency() {
        return this.currency;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + "'" +
                ", login='" + login + "'" +
                ", currency='" + currency + "'" +
                "}";
    }
}