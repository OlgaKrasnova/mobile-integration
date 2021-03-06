package ru.mospolytech.lab1;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

// Сериализаторы для получения объекта от ответа и разделение их на определенные поля
public class ProductDetail {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("price")
    int price;

    @SerializedName("images")
    List<Images> image_A;

    @SerializedName("location")
    Locations location;

    @SerializedName("views")
    String views;
}
