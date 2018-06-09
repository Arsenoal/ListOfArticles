package com.arsen.listofarticles.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.adapter.ArticlesAdapter;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.custom_layout_managers.WrapContentLinearLayoutManager;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.presenters.ArticlesPresenter;
import com.arsen.listofarticles.rest.models.FilmsResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesViewActivity extends AppCompatActivity implements ArticlesView {

    @BindView(R.id.articles_list)
    RecyclerView articlesList;

    private ArticlesPresenter articlesPresenter;
    private ArticlesAdapter articlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initArticlesList();
        initPresenter();
    }

    private void initPresenter() {
        ArticlesModel articlesModel = new ArticlesModel(this);
        this.articlesPresenter = new ArticlesPresenter(articlesModel);
        articlesPresenter.attachView(this);

        articlesPresenter.startLoading();
    }

    @Override
    public void addArticles(ArrayList<? extends ArticleField> articles) {
        articlesAdapter.addArticles(articles);
    }

    private void initArticlesList() {
        articlesAdapter = new ArticlesAdapter();
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(this);
        articlesList.setLayoutManager(wrapContentLinearLayoutManager);
        articlesList.setAdapter(articlesAdapter);
    }

    @Override
    public Context provideContext() {
        return this;
    }
}