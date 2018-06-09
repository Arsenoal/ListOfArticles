package com.arsen.listofarticles.interfaces;

import android.content.Context;

import com.arsen.listofarticles.common.model.ArticleField;

import java.util.ArrayList;

public interface ArticlesView {
    void addArticles(ArrayList<? extends ArticleField> articles);
    Context provideContext();
}
