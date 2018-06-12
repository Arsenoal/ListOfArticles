package com.arsen.listofarticles.presenters;

import com.arsen.listofarticles.interfaces.view.ArticleSingleView;

public class ArticleSinglePresenter {
    private ArticleSingleView articleSingleView;

    public void attachView(ArticleSingleView articleSingleView) {
        this.articleSingleView = articleSingleView;
    }

    public void detachView() {
        articleSingleView = null;
    }
}
