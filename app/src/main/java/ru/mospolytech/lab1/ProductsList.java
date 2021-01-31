package ru.mospolytech.lab1;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// Сериализаторы для получения объекта от ответа и разделение их на определенные поля
public class ProductsList {
    @SerializedName("data")
    List<ProductDetail> all;
}
