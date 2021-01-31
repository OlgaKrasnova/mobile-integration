package ru.mospolytech.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProductActivity extends AppCompatActivity implements OnMapReadyCallback{
    TextView newsHeader;
    TextView newsText;
    TextView newsBody;
    TextView productCity;
    ImageView newsImageFull;
    ApiInterface api; // создаем апи
    private CompositeDisposable disposables; // объект для создания одноразовых предметов путем упаковки других типов
    // потом для всех функций, добавленных в disposables, после завершения мы сможем вызвать метод dispose()

    List<Images> listimg;
    private GoogleMap mMap;// Гугл карта для отображения местоположения

    @Override
    // В методе onCreate() производится первоначальная настройка глобального состояния
    // Инициализируем переменные, связываем с элементами на макете и т.д.
    // Метод onCreate() принимает объект Bundle, содержащий состояние пользовательского интерфейса,
    // сохранённое в последнем вызове обработчика onSaveInstanceState
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products); // установка макета
        newsHeader = findViewById(R.id.productHeader); // название товара
        newsText = findViewById(R.id.productText); // цена товара
        newsBody = findViewById(R.id.productBody); // описание
        productCity = findViewById(R.id.productCity); // город
        newsImageFull = findViewById(R.id.newsImageFull); // изображение
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map); // Гугл карта
        mapFragment.getMapAsync(this);
        api = ApiConfiguration.getApi();
        disposables = new CompositeDisposable(); // объект для создания одноразовых предметов путем упаковки других типов

        if (getIntent().getExtras() != null){
            disposables.add( // добавляем в disposables все Observable
                    api.product(getIntent().getStringExtra("id"))
                            .subscribeOn(Schedulers.io()) // указываем тот поток, где должно происходить образение к серверу
                            // в нашем случае - обрабатываем работу с сервером (вызов сервера) в потоке io (предназначен для ввода-вывода)
                            .observeOn(AndroidSchedulers.mainThread()) // результат выполнения обрабатываем в основном потоке, , где интерфейс находится
                            .subscribe( // обработка полученных результатов, если всё прошло корректно
                                    (product) -> {
                                        newsHeader.setText(product.all.name); // название товара
                                        newsText.setText(product.all.price/100 + " ₽"); // цена товара
                                        newsBody.setText(product.all.description); // описание товара
                                        listimg = new ArrayList<>(); //
                                        listimg.clear();

                                        Log.d(TAG, "onBindViewHolder: " + listimg.addAll(product.all.image_A));
                                        // Библиотека Glide предназначена для асинхронной подгрузки изображений из сети, ресурсов или файловой системы,
                                        // их кэширования и отображения.
                                        Glide.with(this).load(listimg.get(0).url + "").into(newsImageFull);

                                        // Город местонахождения товара
                                        productCity.setText("Город: " + product.all.location.description); //

                                        // Добавление местоположения маркера и установка камеры на него по пришедшим данным latitude и longitude
                                        LatLng sydney = new LatLng(product.all.location.latitude, product.all.location.longitude);
                                        mMap.addMarker(new MarkerOptions()
                                                .position(sydney)
                                                .title("Marker in Sydney"));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                                    },
                                    (error) -> {
                                        error.printStackTrace();
                                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
                                    }));
        }
    }

    // Гугл карта с отображением местоположения
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    // Когда экран уничтожается - удаляем все в disposables (т.е. они завершают свою работу)
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
