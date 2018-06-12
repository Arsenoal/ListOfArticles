package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.rest.models.FilmsResponse;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.helper.NetworkHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticlesPresenter {
    private static final Logger LOGGER = Logger.getLogger(ArticlesPresenter.class.getSimpleName());

    private AppCompatActivity appCompatActivity;
    private ArticlesView articlesView;
    private ArticlesModel articlesModel;

    public ArticlesPresenter(ArticlesModel articlesModel) {
        this.articlesModel = articlesModel;
    }

    public void attachView(ArticlesView articlesView) {
        this.appCompatActivity = (AppCompatActivity) articlesView.provideContext();
        this.articlesView = articlesView;

        ((App) (appCompatActivity.getApplication())).getNetComponent().inject(this);
    }

    public void detachView() {
        articlesView = null;
    }

    public void startLoading(int page) {
        if (NetworkHelper.isNetworkAvailable(appCompatActivity))
            loadArticles(page);
        else
            loadArticlesFromDb();
    }

    public void loadArticles(int page) {
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
                                filmsResponse -> {
                                    ArrayList<FilmsResponse.Response.Film.Field> fields = filmsResponse.getResponse().getFields();
                                    articlesView.addArticles(fields);

                                    addArticleToDb(fields);
                                },
                                error -> {
                                    //TODO handle is left for future, for now just log what went wrong
                                    LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "error cause on getting films: %s", error.getCause()));
                                }
                        ));
    }

    private void loadArticlesFromDb() {
        articlesView.invalidate();
        articlesModel.loadArticlesFromDB(articles -> articlesView.addArticles(articles));
    }

    private void addArticleToDb(ArrayList<? extends ArticleField> articles) {
        ContentValues cv;
        for(ArticleField articleField: articles) {
            cv = new ContentValues(3);
            cv.put(ArticlesTable.COLUMN.TITLE, articleField.getTitle());
            cv.put(ArticlesTable.COLUMN.CATEGORY, articleField.getCategory());
            cv.put(ArticlesTable.COLUMN.THUMBNAIL, articleField.getThumbnail());

            articlesModel.addArticleToDB(() -> LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "item successfully added to db: %s", articleField.getCategory())), cv);
        }
    }
}
