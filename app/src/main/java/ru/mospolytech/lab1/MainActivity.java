package ru.mospolytech.lab1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity  {

    EditText textSearch;
    RecyclerView recyclerView; // компонент пользовательского интерфейса, который позволяет создавать прокручиваемый список
    ListAdapter adapter;
    List<ProductDetail> list; // Список товаров
    ApiInterface api; // создаем апи
    private CompositeDisposable disposables; // объект для создания одноразовых предметов путем упаковки других типов
    // потом для всех функций, добавленных в disposables, после завершения мы сможем вызвать метод dispose()

    @Override
    // В методе onCreate() производится первоначальная настройка глобального состояния
    // Инициализируем переменные, связываем с элементами на макете и т.д.
    // Метод onCreate() принимает объект Bundle, содержащий состояние пользовательского интерфейса,
    // сохранённое в последнем вызове обработчика onSaveInstanceState
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = new ArrayList<>();
        adapter = new ListAdapter(this, list);
        recyclerView = findViewById(R.id.list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textSearch = findViewById(R.id.textSearch); // строка поиска
        api = ApiConfiguration.getApi(); // заполнение апи, чтобы мы могли этот апи вызвать
        disposables = new CompositeDisposable();
        this.onClick(this.recyclerView);

        // Получаем список всех товаров, когда зашли в пришложение
        disposables.add(api.productlist(textSearch.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((productsList) -> {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    findViewById(R.id.list).setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(productsList.all);
                    adapter.notifyDataSetChanged();
                }, (error) -> {
                    Toast.makeText(this, "При поиске возникла ошибка:\n" + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    findViewById(R.id.list).setVisibility(View.VISIBLE);

                }));
    }

    // Функция поиска (при нажатии на кнопку)
    public void onClick(View view){
        if (textSearch.getText().toString().isEmpty()){ // если пустая поисковая строка
            list.clear(); // очищаем список товаров
        } else { // иначе
            disposables.add(api.productlist(textSearch.getText().toString())
                    .subscribeOn(Schedulers.io()) // указываем тот поток, где должно происходить обращение к серверу
                    // в нашем случае - обрабатываем работу с сервером (вызов сервера) в потоке io (предназначен для ввода-вывода)
                    .observeOn(AndroidSchedulers.mainThread()) // результат обрабатываем в основном потоке, где интерфейс находится
                    .subscribe((productsList) -> { // обработка полученных результатов, если всё прошло корректно
                        findViewById(R.id.progressBar).setVisibility(View.GONE); // скрываем спиннер загрузки
                        findViewById(R.id.list).setVisibility(View.VISIBLE); // показываем элемент со списком товаров
                        list.clear(); // очищаем элемент list
                        list.addAll(productsList.all); // загружаем все товары в элемент списка
                        adapter.notifyDataSetChanged(); // сообщаем адаптеру, что данные обновились
                    }, (error) -> { // если возникла ошибка при вызове сервера
                        Toast.makeText(this, "При поиске возникла ошибка:\n" + error.getMessage(),
                                Toast.LENGTH_LONG).show(); // Показываем ошибку
                        findViewById(R.id.progressBar).setVisibility(View.GONE); // скрыть спиннер загрузки
                        findViewById(R.id.list).setVisibility(View.VISIBLE); // сделать список видимым
                    }));
            // показываем уведомление о результатах
            Toast.makeText(this, "Показаны результаты поиска: " + textSearch.getText().toString(), Toast.LENGTH_SHORT).show();
        }
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
