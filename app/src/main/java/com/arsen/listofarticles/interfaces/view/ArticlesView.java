package com.arsen.listofarticles.interfaces.view;

import android.content.Context;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

import java.util.ArrayList;

public interface ArticlesView {
    void addArticles(ArrayList<? extends ArticleField> articles);
    void addNewArticle(ArticleField article);
    void addPinnedArticles(ArrayList<? extends ArticleField> articles);
    void addPinnedArticle(ArticleField article);
    void invalidate();
    void startNotificationService();
    void showLoader();
    void hideLoader();
    Context provideContext();
}
