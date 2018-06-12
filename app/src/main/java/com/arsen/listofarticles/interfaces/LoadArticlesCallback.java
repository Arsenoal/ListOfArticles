package com.arsen.listofarticles.interfaces;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

import java.util.ArrayList;

public interface LoadArticlesCallback {
    void onLoad(ArrayList<? extends ArticleField> article);
}
