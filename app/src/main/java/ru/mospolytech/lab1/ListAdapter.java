package ru.mospolytech.lab1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

// класс-адаптер, который содержит данные и связывает их со списком
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    Context context;
    List<ProductDetail> list; // список товаров
    List<Images> listimg; // список изображений

    // Конструктор адаптера
    public ListAdapter(Context context, List<ProductDetail> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    // Метод создает новый объект ViewHolder всякий раз, когда RecyclerView нуждается в этом.
    // Это тот момент, когда создаётся layout строки списка, передается объекту ViewHolder,
    // и каждый дочерний view-компонент может быть найден и сохранен
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    // Метод принимает объект ViewHolder и устанавливает необходимые данные
    // для соответствующей строки во view-компоненте
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductDetail news = list.get(position);
        listimg = new ArrayList<>();
        listimg.clear();
        holder.factIdText.setText(news.name);
        holder.sourceView.setText("Просмотров: "+ news.views);
        holder.dateNews.setText(news.price/100 + " ₽" );
        Log.d(TAG, "onBindViewHolder: " + listimg.addAll(news.image_A));
        Glide.with(context).load( listimg.get(0).url+ "").into(holder.factImage);

        // Оработка нажатия на элемент списка
        holder.item.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductActivity.class);
            intent.putExtra("id", news.id);
            context.startActivity(intent);
        });
    }

    @Override
    // Метод возвращает общее количество элементов списка.
    // Значения списка передаются с помощью конструктора.
    public int getItemCount() {
        return list.size();
    }

    // Для хранения данных в классе адаптера определяем статический класс ViewHolder,
    // который использует определенные в product_detail.xml элементы.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView factImage;
        TextView factIdText;
        TextView dateNews;
        TextView sourceView;
        LinearLayout item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            factImage = itemView.findViewById(R.id.newsImage);
            factIdText = itemView.findViewById(R.id.newsIdText);
            sourceView = itemView.findViewById(R.id.sourceView);
            dateNews = itemView.findViewById(R.id.dateNews);
            item = itemView.findViewById(R.id.item);
        }
    }
}
