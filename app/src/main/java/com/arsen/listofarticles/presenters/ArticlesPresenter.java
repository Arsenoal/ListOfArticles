package com.arsen.listofarticles.presenters;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.activity.ArticleSingleViewActivity;
import com.arsen.listofarticles.common.db.ArticlesTable;
import com.arsen.listofarticles.interfaces.OnCompletedCallback;
import com.arsen.listofarticles.interfaces.view.ArticlesView;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.rest.models.FilmsResponse;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.Quad;
import com.arsen.listofarticles.util.helper.NetworkHelper;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.arsen.listofarticles.util.Constants.ARTICLE_DB_ID_KEY;
import static com.arsen.listofarticles.util.Constants.ARTICLE_ID_KEY;
import static com.arsen.listofarticles.util.Constants.ARTICLE_TYPE_KEY;
import static com.arsen.listofarticles.util.Constants.LIST_UPDATE_TIME;

public class ArticlesPresenter {
    private static final Logger LOGGER = Logger.getLogger(ArticlesPresenter.class.getSimpleName());

    @Inject
    ArticlesModel articlesModel;

    private ArticlesView articlesView;
    private AppCompatActivity activity;

    private ArrayList<ArticleField> allArticles;
    private ArrayList<ArticleField> pinnedArticles;

    private boolean isViewAttached;

    private Timer mTimer;

    public ArticlesPresenter() {
        allArticles = new ArrayList<>();
        isViewAttached = false;
        mTimer = new Timer();
    }

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;
        this.activity = (AppCompatActivity) articlesView.provideContext();

        ((App) activity.getApplication()).getNetComponent().inject(this);
        isViewAttached = true;
    }

    public void detachView() {
        articlesView = null;
        articlesModel.invalidate();
        isViewAttached = false;
    }

    public void startLoading() {
        if (NetworkHelper.isNetworkAvailable(articlesView.provideContext())) {
            loadArticles(1);
            handleUpdates();
        } else
            loadArticlesFromDb();

        loadPinnedArticles();
    }

    public void loadArticles(int page) {
        articlesModel.loadArticles(
                articlesModel.
                        getArticlesService().
                        getFilms(
                                "12%20years%20a%20slave",
                                Constants.TAGS,
                                Constants.FROM_DATE,
                                Constants.FIELDS,
                                Constants.ORDER_BY,
                                Constants.API_KEY,
                                page).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        map(filmsResponse -> filmsResponse).
                        subscribe(
                                filmsResponse -> {
                                    ArrayList<FilmsResponse.Response.Film.Field> fields = filmsResponse.getResponse().getFields();
                                    this.articlesView.addArticles(fields);

                                    addArticlesToDb(fields);
                                    for (FilmsResponse.Response.Film.Field article : fields)
                                        if (!this.allArticles.contains(article))
                                            this.allArticles.add(article);
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

    private void loadPinnedArticles() {
        articlesModel.loadPinnedArticlesFromDB(articles -> {
            this.pinnedArticles = new ArrayList<>();
            this.pinnedArticles.addAll(articles);
            articlesView.addPinnedArticles(articles);
        });
    }

    public void updatePinnedArticles() {
        articlesModel.loadPinnedArticlesFromDB(articles -> {
            if (ArticlesPresenter.this.pinnedArticles.size() < articles.size()) {
                articlesView.addPinnedArticle(articles.get(articles.size() - 1));
                ArticlesPresenter.this.pinnedArticles.add(articles.get(articles.size() - 1));
            }
        });
    }

    private void handleUpdates() {
        if (isViewAttached)
            mTimer.schedule(new UpdateTask(), LIST_UPDATE_TIME);
    }

    private void addArticlesToDb(ArrayList<? extends ArticleField> articles) {
        ContentValues cv;
        for (ArticleField article : articles) {
            cv = new ContentValues(4);
            cv.put(ArticlesTable.COLUMN.ARTICLE_ID, article.getArticleId());
            cv.put(ArticlesTable.COLUMN.TITLE, article.getTitle());
            cv.put(ArticlesTable.COLUMN.CATEGORY, article.getCategory());
            cv.put(ArticlesTable.COLUMN.THUMBNAIL, article.getThumbnail());

            articlesModel.addArticleToDB(new OnCompletedCallback() {
                @Override
                public void completed() {
                    LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "item successfully added to db: %s", article.getCategory()));
                }

                @Override
                public void onError() {

                }
            }, cv);
        }
    }

    private void addArticleToDb(ArticleField article) {
        ContentValues cv;
        cv = new ContentValues(4);
        cv.put(ArticlesTable.COLUMN.ARTICLE_ID, article.getArticleId());
        cv.put(ArticlesTable.COLUMN.TITLE, article.getTitle());
        cv.put(ArticlesTable.COLUMN.CATEGORY, article.getCategory());
        cv.put(ArticlesTable.COLUMN.THUMBNAIL, article.getThumbnail());

        articlesModel.addArticleToDB(new OnCompletedCallback() {
            @Override
            public void completed() {
                LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "item successfully added to db: %s", article.getCategory()));
            }

            @Override
            public void onError() {

            }
        }, cv);
    }

    public void articleClicked(Quad<String, String, String, AppCompatImageView> quad) {
        String articleId = quad.first;
        String articleDbId = quad.second;
        String articleType = quad.third;
        AppCompatImageView articleImage = quad.fourth;

        Intent intent = new Intent(activity, ArticleSingleViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ARTICLE_ID_KEY, articleId);
        intent.putExtra(ARTICLE_DB_ID_KEY, articleDbId);
        intent.putExtra(ARTICLE_TYPE_KEY, articleType);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(activity, articleImage, "article");
        activity.startActivity(intent, options.toBundle());
    }

    private class UpdateTask extends TimerTask {
        @Override
        public void run() {
            //re check if activity is on foreground because 30 seconds has passed from last check
            if (isViewAttached)
                articlesModel.loadArticles(
                        articlesModel.
                                getArticlesService().
                                getFilms(
                                        "12%20years%20a%20slave",
                                        Constants.TAGS,
                                        Constants.FROM_DATE,
                                        Constants.FIELDS,
                                        Constants.ORDER_BY,
                                        Constants.API_KEY,
                                        1).
                                subscribeOn(Schedulers.io()).
                                observeOn(AndroidSchedulers.mainThread()).
                                map(filmsResponse -> filmsResponse).
                                subscribe(
                                        filmsResponse -> {
                                            ArrayList<FilmsResponse.Response.Film.Field> fields = filmsResponse.getResponse().getFields();

                                            FilmsResponse.Response.Film.Field article;
                                            for (int i = 0; i < fields.size(); i++) {
                                                article = fields.get(i);
                                                if (!allArticles.get(i).equals(article)) {
                                                    addArticleToDb(article);
                                                    ArticlesPresenter.this.allArticles.add(i, article);
                                                    ArticlesPresenter.this.articlesView.addNewArticle(article);
                                                }
                                            }

                                            LOGGER.log(Level.INFO, "timer scheduled");

                                            handleUpdates();
                                        },
                                        error -> {
                                            //TODO handle is left for future, for now just log what went wrong
                                            LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "error cause on getting films: %s", error.getCause()));
                                        }
                                ));
        }
    }
}
