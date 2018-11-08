package ru.geekbrains.webbrowser;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static org.junit.Assert.fail;

// Здесь будем вызывать запросы
public class OkHttpRequester {
    // Интерфейс с обратным вызовом
    private OnResponseCompleted listener;

    public OkHttpRequester(OnResponseCompleted listener) {
        this.listener = listener;
    }

    // Синхронный запрос страницы
    public void run(String url) {
        OkHttpClient client = new OkHttpClient();        // Клиент
        Request.Builder builder = new Request.Builder(); // Создадим строителя
        builder.url(url);                                // Укажем адрес сервера
        Request request = builder.build();               // Построим запрос

        Call call = client.newCall(request);            // Ставим запрос в очередь
        call.enqueue(new Callback() {
            // Этот Handler нужен для синхронизации потоков.
// Если его сконструировать, он запомнит поток и в дальнейшем будет с ним синхронизироваться
            final Handler handler = new Handler();

            // Это срабатывает, когда приходит ответ от сервера
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String answer = response.body().string(); // Получить данные
// Синхронизируем поток с потоком UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
// Вызовем наш метод обратного вызова
                        listener.onCompleted(answer);
                    }
                });
            }

            // При сбое
            public void onFailure(Call call, IOException e) {
                fail();
            }
        });
    }
    // Интерфейс обратного вызова. Метод onCompleted вызывается по окончании загрузки страницы
    public interface OnResponseCompleted {
        void onCompleted(String content);
    }
}