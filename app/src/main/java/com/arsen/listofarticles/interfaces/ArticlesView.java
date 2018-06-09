package com.arsen.listofarticles.interfaces;

import android.content.Context;

import com.arsen.listofarticles.common.model.ArticleFields;

import java.util.ArrayList;

public interface ArticlesView {
    void addArticles(ArrayList<ArticleFields> articles);
    Context provideContext();
}
