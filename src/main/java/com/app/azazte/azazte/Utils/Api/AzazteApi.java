package com.app.azazte.azazte.Utils.Api;

import com.app.azazte.azazte.Beans.NewsCardWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sprinklr on 14/05/16.
 */
public interface AzazteApi {
    @GET("/news/fetchNewsOnRefreshv3/{untilId}")
    Call<NewsCardWrapper> getNews(@Path("untilId") Integer untilId);
}
