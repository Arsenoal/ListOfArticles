package com.arsen.listofarticles.rest;

import com.arsen.listofarticles.rest.models.Film;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeneralService {

    @GET()
    Single<Film> getFilms(
            @Query("api-key") String apiKey,
            @Query("q") String q,
            @Query("tag") String tag,
            @Query("from-date") String fromDate,
            @Query("show-fields") String fields,
            @Query("order-by") String orderBy,
            @Query("page") int page
    );
}
