package com.example.app.http;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.Consumer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpClient {
    public static final OkHttpClient INSTANCE = new OkHttpClient();

    public static void request(Request request, Consumer<Response> action) {
        INSTANCE.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);

                action.accept(response);
            }
        });
    }
}
