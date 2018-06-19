package com.arsen.listofarticles.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.adapter.ArticlesAdapter;
import com.arsen.listofarticles.custom_layout_managers.WrapContentLinearLayoutManager;
import com.arsen.listofarticles.interfaces.view.ArticlesView;
import com.arsen.listofarticles.listeners.EndlessRecyclerViewScrollListener;
import com.arsen.listofarticles.presenters.ArticlesPresenter;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesViewActivity
        extends AppCompatActivity implements ArticlesView {

    private final static Logger LOGGER = Logger.getLogger(ArticlesViewActivity.class.getSimpleName());

    @BindView(R.id.articles_list)
    RecyclerView articlesList;

    @Inject
    ArticlesPresenter articlesPresenter;

    private ArticlesAdapter articlesAdapter;
    private WrapContentLinearLayoutManager wrapContentLinearLayoutManager;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);

        initArticlesList();

        initPresenter();

        setupInfiniteScroll();

        setupItemClick();
    }

    private void initPresenter() {
        this.articlesPresenter.attachView(this);

        this.articlesPresenter.startLoading();
    }

    @Override
    public void addArticles(ArrayList<? extends ArticleField> articles) {
        articlesAdapter.addArticles(articles);
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

    private void initArticlesList() {
        articlesAdapter = new ArticlesAdapter();
        wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        articlesList.setLayoutManager(wrapContentLinearLayoutManager);
        articlesList.setAdapter(articlesAdapter);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    @Override
    public void invalidate() {
        if (endlessRecyclerViewScrollListener != null)
            endlessRecyclerViewScrollListener.resetState();
        if (articlesAdapter != null)
            articlesAdapter.reset();
    }

    public void setupItemClick() {
        articlesAdapter
                .getArticleIdOnItemClick()
                .subscribe(pair -> articlesPresenter.itemClicked(pair));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        articlesPresenter.detachView();
    }
}