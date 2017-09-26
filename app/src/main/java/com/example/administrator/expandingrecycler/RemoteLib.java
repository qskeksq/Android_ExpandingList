package com.example.administrator.expandingrecycler;

import android.util.Log;

import com.example.administrator.expandingrecycler.domain.Data;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-26.
 */

public class RemoteLib {

    private static RemoteLib instance;
    private Data data;

    public static RemoteLib getInstance() {
        if (instance == null) {
            instance = new RemoteLib();
        }
        return instance;
    }


    public Data getDatas(int start, int howMany, final TaskInterface taskInterface) {
        final IRemoteService remote = ServiceGenerator.createService(IRemoteService.class);
        Call<Data> call = remote.getMapData(start, howMany);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    data = response.body();
                    taskInterface.setData(data);
                } else {
                    Log.e("[RemoteLib] getDatas", "unSuccessful");
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e("[RemoteLib] getDatas", "onFailure");
            }
        });
        return data;
    }


}
