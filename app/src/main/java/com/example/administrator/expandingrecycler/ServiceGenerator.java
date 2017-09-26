package com.example.administrator.expandingrecycler;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 레트로핏 객체 생성
 */
public class ServiceGenerator {

    public static <T> T createService(Class<T> retrofitInterface){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IRemoteService.FULL_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(retrofitInterface);
    }

}
