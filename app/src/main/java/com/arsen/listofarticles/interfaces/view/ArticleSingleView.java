package com.arsen.listofarticles.interfaces.view;

import android.content.Context;
import android.view.View;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

public interface ArticleSingleView {
    void loadData(ArticleField articleField);
    void successfullyPinned();
    void errorOnPin();
    Context provideContext();
}
