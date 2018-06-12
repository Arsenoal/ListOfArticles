package com.arsen.listofarticles.interfaces;

import com.arsen.listofarticles.common.model.ArticleField;

import java.util.ArrayList;

public interface LoadArticlesCallback {
    void onLoad(ArrayList<? extends ArticleField> article);
}
