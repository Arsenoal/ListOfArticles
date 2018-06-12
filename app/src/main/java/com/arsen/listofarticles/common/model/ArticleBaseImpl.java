package com.arsen.listofarticles.common.model;

public class ArticleBaseImpl implements ArticleField {

    private long id;
    private String title;
    private String thumbnail;
    private String category;

    public void setId(long id) {
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

    public long getId() {
        return this.id;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getThumbnail() {
        return null;
    }

    @Override
    public String getCategory() {
        return null;
    }
}
