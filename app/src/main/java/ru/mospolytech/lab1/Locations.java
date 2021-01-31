package ru.mospolytech.lab1;

import com.google.gson.annotations.SerializedName;

// Сериализаторы для получения объекта от ответа и разделение их на определенные поля
public class Locations {
    // Параметр (значение) аннотации @SerializedName - это имя, которое будет использоваться
    // при сериализации и десериализации объектов
    @SerializedName("description") // как в JSON
    String description; // как в Java

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;
}
