package com.arsen.listofarticles.rest.services;

import com.arsen.listofarticles.rest.models.Article;
import com.arsen.listofarticles.rest.models.FilmsResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticlesService {

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

    @GET("{id}")
    Single<Article> getArticle(
            @Path(value = "id", encoded = true) String id,
            @Query("api-key") String apiKey,
            @Query("show-fields") String fields
    );
}
