package com.arsen.listofarticles.rest.services;

import com.arsen.listofarticles.rest.models.FilmsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FilmsService {

    @GET("search")
    Single<FilmsResponse> getFilms(
            @Query("q") String q,
            @Query("tag") String tag,
            @Query("from-date") String fromDate,
            @Query("show-fields") String fields,
            @Query("order-by") String orderBy,
            @Query("api-key") String apiKey,
            @Query("page") int page
    );
}
