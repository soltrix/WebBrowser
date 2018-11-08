package ru.geekbrains.webbrowser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // получаем представления UI
        final WebView webView = findViewById(R.id.browse);
        final TextView url = findViewById(R.id.url);
        Button ok = findViewById(R.id.ok);

        // Создадим объект класса делателя запросов и налету сделаем анонимный класс слушателя
        final OkHttpRequester requester = new OkHttpRequester(new OkHttpRequester.OnResponseCompleted() {

            // по окончании загрузки страницы вызовем этот метод, который и вставит текст в WebView
            @Override
            public void onCompleted(String content) {
                webView.loadData(content, "text/html; charset=utf-8", "utf-8");
            }
        });

        // при нажатии на кнопку отправим запрос
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // делаем запрос
                requester.run(url.getText().toString());
            }
        });
    }
}
