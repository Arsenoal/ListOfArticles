package com.arsen.listofarticles.interfaces;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

public interface OnArticleLoadedCallback {
    void onLoad(ArticleField articleField);
}
