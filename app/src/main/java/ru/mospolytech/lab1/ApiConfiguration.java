package ru.mospolytech.lab1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

// модуль для подключения к АПИ
public class ApiConfiguration {

    // адрес апи, к которому подключаемся
    public static final String BASE_URL = "https://api.youla.io/";

    private static ApiInterface api;
    // Реализация паттерна Одиночка (Singleton)
    // Суть паттерна в том, что мы создаем объект, который будет единственным
    // И откуда бы мы не обращались, мы будем обращаться к одному и тому же объекту
    private static ApiConfiguration mInstance; // создаем статичное поле

    // Функция для получения объекта класса. Либо создает класс (если он не создан)
    // либо возвращает существующий объект
    public static ApiConfiguration getInstance() {
        if (mInstance == null) {
            mInstance = new ApiConfiguration();
        }
        return mInstance;
    }
    // конец реализации паттерна Одиночка

    // Конструктор для работы с АПИ
    private ApiConfiguration(){ // делаем конструктур приватным для реализации паттерна Одиночка (см. выше), чтобы его нельзя было снаружи вызвать
        // Создание конвертера Гсон даты в определенный формат
        Gson gson = new GsonBuilder().setLenient().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
        // Создание экземпляра клиента OkHttp для http-вызовов
        OkHttpClient client = new OkHttpClient.Builder().build();

        // Создание экземляра интерфейса из библиотеки Retrofit для установки подключения, добавление конвертеров
        Retrofit retrofit = new Retrofit.Builder()
                .client(client) // клиент OkHttp
                .baseUrl(BASE_URL) // адрес сервера, к которому подключаемся
                .addConverterFactory(GsonConverterFactory.create(gson)) // добавление конвертера даты Gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // для асинхронности
                .build();
        // Превращение интерфейса API в вызываемый объект с помощью retrofit
        api = retrofit.create(ApiInterface.class);
    }

    // Получение АПИ или создание нового подключения, если оно не было создано
    // Тоже реализация паттерна Одиночка
    public static ApiInterface getApi(){
        if (api == null) {
            new ApiConfiguration();
        }
        return api;
    }
}


