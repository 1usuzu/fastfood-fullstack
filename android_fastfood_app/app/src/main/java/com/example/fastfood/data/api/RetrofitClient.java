package com.example.fastfood.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://10.0.2.2:3000/";

    // Dùng Singleton để đảm bảo chỉ tạo 1 Retrofit instance
    private static Retrofit retrofit;

    // Private constructor để ngăn tạo object bên ngoài
    private RetrofitClient() {}

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // ✅ Dùng chung cho mọi API interface
    public static <T> T getApi(Class<T> apiServiceClass) {
        return getRetrofitInstance().create(apiServiceClass);
    }
}
