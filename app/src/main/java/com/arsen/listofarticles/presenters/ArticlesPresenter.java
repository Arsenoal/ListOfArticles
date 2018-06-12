package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.interfaces.LoadArticlesCallback;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.util.Constants;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticlesPresenter {
    private static final Logger LOGGER = Logger.getLogger(ArticlesPresenter.class.getSimpleName());

    private ArticlesView articlesView;
    private ArticlesModel articlesModel;

    public ArticlesPresenter(ArticlesModel articlesModel) {
        this.articlesModel = articlesModel;
    }

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;

        ((App) (((AppCompatActivity) articlesView.provideContext()).getApplication())).getNetComponent().inject(this);
    }

    public void detachView() {
        articlesView = null;
    }

    public void startLoading(int page) {
        loadArticles(page);
    }

    private void loadArticles(int page) {
        articlesModel.loadArticles(
                articlesModel.
                        getFilmsService().
                        getFilms(
                                "12%20years%20a%20slave",
                                "film/film,tone/reviews",
                                "2010-01-01",
                                "starRating,headline,thumbnail,short-url",
                                "relevance",
                                Constants.API_KEY,
                                page).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        map(filmsResponse -> filmsResponse).
                        subscribe(
                                filmsResponse -> articlesView.addArticles(filmsResponse.getResponse().getFields()),
                                error -> {
                                    //TODO handle is left for future, for now just log what went wrong
                                    LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "error cause on getting films: %s", error.getCause()));
                                }
                        ));

    }

    private void loadArticlesFromDb() {
        articlesModel.loadArticlesFromDB(articles -> {

        });
    }

    private void addArticleToDb(ArticleField article) {
        articlesModel.addArticleToDB(() -> {

        });
    }
}
