package com.arsen.listofarticles.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.adapter.ArticlesAdapter;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.custom_layout_managers.WrapContentLinearLayoutManager;
import com.arsen.listofarticles.database.DbHelper;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.listeners.EndlessRecyclerViewScrollListener;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.presenters.ArticlesPresenter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticlesViewActivity
        extends AppCompatActivity implements ArticlesView {

    private final static Logger LOGGER = Logger.getLogger(ArticlesViewActivity.class.getSimpleName());

    @BindView(R.id.articles_list)
    RecyclerView articlesList;

    private ArticlesPresenter articlesPresenter;
    private ArticlesAdapter articlesAdapter;
    private WrapContentLinearLayoutManager wrapContentLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initArticlesList();

        initPresenter();

        setupInfiniteScroll();
    }

    private void initPresenter() {
        DbHelper dbHelper = new DbHelper(this);
        ArticlesModel articlesModel = new ArticlesModel(this, dbHelper);
        this.articlesPresenter = new ArticlesPresenter(articlesModel);
        articlesPresenter.attachView(this);

        articlesPresenter.startLoading(1);
    }

    @Override
    public void addArticles(ArrayList<? extends ArticleField> articles) {
        articlesAdapter.addArticles(articles);
    }

    private void setupInfiniteScroll() {
        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener
                = new EndlessRecyclerViewScrollListener(wrapContentLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                articlesPresenter.startLoading(page);
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
}