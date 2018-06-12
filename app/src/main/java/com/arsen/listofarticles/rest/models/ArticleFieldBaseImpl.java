package com.arsen.listofarticles.rest.models;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;

public class ArticleFieldBaseImpl implements ArticleField {

    private String id;
    private String title;
    private String thumbnail;
    private String category;

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getThumbnail() {
        return this.thumbnail;
    }

    @Override
    public String getCategory() {
        return this.category;
    }
}
