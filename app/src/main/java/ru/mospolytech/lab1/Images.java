package ru.mospolytech.lab1;

import com.google.gson.annotations.SerializedName;

// Сериализатор для получения объекта изображения от ответа и разделение их на определенные поля
public class Images {
    @SerializedName("url")
    String url;
}
