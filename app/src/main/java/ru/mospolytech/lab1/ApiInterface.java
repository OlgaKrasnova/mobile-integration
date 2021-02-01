package ru.mospolytech.lab1;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

// с помощью библиотки Retrofit мы превращаем HTTP API в интерфейс Java
// С помощью декораторов @ мы превращаем запросы в понятный Java язык
// Похоже на паттерн Наблюдатель
public interface ApiInterface {

    // Запрос на получение списка всех товаров
    @GET("api/v1/products")
    // Observable генерирует некое событие
    // Observable<ProductList> - означает, что Observable будет предоставлять данные типа ProductList,
    // т.е. каждое событие Next, которое он будет генерировать, будет приходить с объектом типа ProductList.
    // если пришло событие Completed, значит мы можем выключить ProgressBar и уведомить пользователя, что поиск завершен
    // если пришло событие Error, то уведомляем пользователя, что поиск был прерван с ошибкой
    // Observable - оболочка RxJava, которая позволяет работать с асинхронностью
    Observable<ProductsList> productlist(@Query("search") String search);

    // запрос на получение конкретного товара по id
    @GET("api/v1/product/{id}")
    Observable<ProductObject> product(@Path("id") String id);
}
