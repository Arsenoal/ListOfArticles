package com.arsen.listofarticles.presenters;

import com.arsen.listofarticles.interfaces.ArticlesView;

public class ArticlesPresenter {
    private ArticlesView articlesView;

    public void attachView(ArticlesView articlesView) {
        this.articlesView = articlesView;
    }
}
