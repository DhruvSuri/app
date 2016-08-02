package com.app.azazte.azazte.Utils.Api;

import com.app.azazte.azazte.Beans.NewsCardWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sprinklr on 14/05/16.
 */
public interface AzazteApi {
    @GET("/service/rest/news")
    Call<NewsCardWrapper> getNews(@Query("start") Integer start,@Query("limit") Integer limit);
}
