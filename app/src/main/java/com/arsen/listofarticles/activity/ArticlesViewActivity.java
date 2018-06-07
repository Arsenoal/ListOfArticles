package com.arsen.listofarticles.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.interfaces.ArticlesView;
import com.arsen.listofarticles.presenters.ArticlesPresenter;

public class ArticlesViewActivity extends AppCompatActivity implements ArticlesView {

    private ArticlesPresenter articlesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPresenter();
    }

    private void initPresenter() {
        this.articlesPresenter = new ArticlesPresenter();
        articlesPresenter.attachView(this);
    }

    @Override
    public void addArticles() {

    }

    @Override
    public Context provideContext() {
        return this;
    }
}