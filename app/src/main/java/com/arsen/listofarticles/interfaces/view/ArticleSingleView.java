package com.arsen.listofarticles.interfaces.view;

import android.content.Context;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

public interface ArticleSingleView {
    void loadData(ArticleField articleField);
    Context provideContext();
}
