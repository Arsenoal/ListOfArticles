package com.arsen.listofarticles.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.adapter.ArticlesAdapter;
import com.arsen.listofarticles.common.adapter.PinnedArticlesAdapter;
import com.arsen.listofarticles.custom_layout_managers.WrapContentLinearLayoutManager;
import com.arsen.listofarticles.interfaces.view.ArticlesView;
import com.arsen.listofarticles.listeners.EndlessRecyclerViewScrollListener;
import com.arsen.listofarticles.presenters.ArticlesPresenter;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.services.notifications.NotificationsService;
import com.arsen.listofarticles.util.helper.ScreenHelper;
import com.arsen.listofarticles.widgets.HorizontalRecyclerView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesListActivity
        extends AppCompatActivity implements ArticlesView {

    private final static Logger LOGGER = Logger.getLogger(ArticlesListActivity.class.getSimpleName());

    @BindView(R.id.articles_list)
    RecyclerView articlesList;

    @BindView(R.id.pinned_items)
    HorizontalRecyclerView pinnedArticles;

    @Inject
    ArticlesPresenter articlesPresenter;

    private ArticlesAdapter articlesAdapter;
    private PinnedArticlesAdapter pinnedArticlesAdapter;
    private WrapContentLinearLayoutManager wrapContentLinearLayoutManager;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @BindDimen(R.dimen.pinned_articles_full_height)
    int pinnedArticlesFullHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_list);
        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);

        prepareArticlesList();

        preparePinnedArticles();

        initPresenter();

        setupInfiniteScroll();

        setupArticleClick();

        setupPinnedArticleClick();
    }

    private void initPresenter() {
        this.articlesPresenter.attachView(this);

        this.articlesPresenter.startLoading();
    }

    @Override
    public void addArticles(ArrayList<? extends ArticleField> articles) {
        articlesAdapter.addArticles(articles);
    }

    @Override
    public void addNewArticle(ArticleField article) {
        articlesAdapter.addNewArticle(article);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    @Override
    public void addPinnedArticles(ArrayList<? extends ArticleField> articles) {
        if (articles.size() == 0)
            articlesList.setPadding(0, 0, 0, 0);
        else {
            pinnedArticlesAdapter.addArticles(articles);
            articlesList.setPadding(0, 0, 0, pinnedArticlesFullHeight);
        }
    }

    @Override
    public void addPinnedArticle(ArticleField article) {
        pinnedArticlesAdapter.addArticle(article);
        int lastItemPosition = pinnedArticlesAdapter.getLastItemsPosition();
        pinnedArticles.smoothScrollToPosition(lastItemPosition);

        if (lastItemPosition == 0)
            articlesList.setPadding(0, 0, 0, pinnedArticlesFullHeight);
    }

    @Override
    public void invalidate() {
        if (endlessRecyclerViewScrollListener != null)
            endlessRecyclerViewScrollListener.resetState();
        if (articlesAdapter != null)
            articlesAdapter.reset();
    }

    @Override
    public void setupNotificationsService() {
        try {
            startService(new Intent(this, NotificationsService.class));
        } catch (IllegalStateException ignored) {
            //android oreo case
        }
    }

    private void setupInfiniteScroll() {
        endlessRecyclerViewScrollListener
                = new EndlessRecyclerViewScrollListener(wrapContentLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                articlesPresenter.loadArticles(page);
                LOGGER.log(Level.INFO, String.format(Locale.ENGLISH, "page: %s", page));
            }
        };

        articlesList.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    private void prepareArticlesList() {
        articlesAdapter = new ArticlesAdapter();
        wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        articlesList.setLayoutManager(wrapContentLinearLayoutManager);
        articlesList.setAdapter(articlesAdapter);
    }

    private void preparePinnedArticles() {
        pinnedArticlesAdapter = new PinnedArticlesAdapter();
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        wrapContentLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        pinnedArticles.setLayoutManager(wrapContentLinearLayoutManager);
        pinnedArticles.setAdapter(pinnedArticlesAdapter);
    }

    public void setupArticleClick() {
        articlesAdapter
                .getArticleIdOnItemClick()
                .subscribe(pair ->
                        articlesPresenter.articleClicked(pair)
                );
    }

    public void setupPinnedArticleClick() {
        pinnedArticlesAdapter
                .getArticleOnItemClick()
                .subscribe(pair -> articlesPresenter.articleClicked(pair));
    }

    @Override
    protected void onResume() {
        super.onResume();
        articlesPresenter.updatePinnedArticles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        articlesPresenter.detachView();
    }
}