package com.example.administrator.expandingrecycler;

import com.example.administrator.expandingrecycler.domain.Data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 레트로핏 리모트 인터페이스
 */

public interface IRemoteService {

    static final String FULL_URL = "http://openAPI.seoul.go.kr:8088/******/json/GeoInfoPublicToiletWGS/";
    static final String URL_PREFIX = "http://openAPI.seoul.go.kr:8088/";
    static final String URL_CERT = "*******";
    static final String URL_MID = "/json/GeoInfoPublicToiletWGS/";

    // 시작 값부터 몇 개 가져올 것인지 조사한 후
    @GET("{start}/{howMany}")
    Call<Data> getMapData(@Path("start") int start, @Path("howMany") int howMany);

}
