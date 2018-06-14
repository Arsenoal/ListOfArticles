package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Pair;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.activity.ArticleSingleViewActivity;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.interfaces.view.ArticlesView;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.rest.models.FilmsResponse;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.helper.NetworkHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arsen.listofarticles.util.Constants.ARTICLE_ID_KEY;

public class ArticlesPresenter {
    private static final Logger LOGGER = Logger.getLogger(ArticlesPresenter.class.getSimpleName());

    @Inject
    ArticlesModel articlesModel;

    private ArticlesView articlesView;
    private AppCompatActivity activity;

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;
        this.activity = (AppCompatActivity) articlesView.provideContext();

        ((App) activity.getApplication()).getNetComponent().inject(this);
    }

    public void detachView() {
        articlesView = null;
    }

    public void startLoading() {
        if (NetworkHelper.isNetworkAvailable(articlesView.provideContext()))
            loadArticles(1);
        else
            loadArticlesFromDb();
    }

    public void loadArticles(int page) {
        articlesModel.loadArticles(
                articlesModel.
                        getArticlesService().
                        getFilms(
                                "12%20years%20a%20slave",
                                Constants.TAGS,
                                "2010-01-01",
                                Constants.FIELDS,
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
        for (ArticleField articleField : articles) {
            cv = new ContentValues(3);
            cv.put(ArticlesTable.COLUMN.TITLE, articleField.getTitle());
            cv.put(ArticlesTable.COLUMN.CATEGORY, articleField.getCategory());
            cv.put(ArticlesTable.COLUMN.THUMBNAIL, articleField.getThumbnail());

            articlesModel.addArticleToDB(() -> LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "item successfully added to db: %s", articleField.getCategory())), cv);
        }
    }

    public void itemClicked(Pair<String, AppCompatImageView> pair) {
        String articleId = pair.first;
        AppCompatImageView articleImage = pair.second;

        Intent intent = new Intent(activity, ArticleSingleViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARTICLE_ID_KEY, articleId);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, articleImage, "article");
        activity.startActivity(intent, options.toBundle());
    }
}
