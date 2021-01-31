package ru.mospolytech.lab1;

import com.google.gson.annotations.SerializedName;

// Сериализаторы для получения объекта от ответа и разделение их на определенные поля
public class ProductObject {
    @SerializedName("data")
    ProductDetail all;
}
