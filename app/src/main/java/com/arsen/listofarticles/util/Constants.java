package com.arsen.listofarticles.util;

public final class Constants {
    private Constants(){}

    public final static String BASE_URL = "https://content.guardianapis.com/";
    public final static String API_KEY = "5c4394bd-ee88-436a-a151-b1e9a26dbc41";

    public final static int VISIBLE_ITEMS_THRESHOLD = 10;
    //in milliseconds
    public final static int LIST_UPDATE_TIME = 30000;

    public final static String TAGS = "film/film,tone/reviews";
    public final static String FIELDS = "starRating,headline,thumbnail,short-url";
    public final static String FROM_DATE = "2010-01-01";
    public final static String ORDER_BY = "relevance";

    public final static String ARTICLE_ID_KEY = "article_id";
    public final static String ARTICLE_DB_ID_KEY = "article_id_db";
    public final static String ARTICLE_TYPE_KEY = "article_type";

    public final static String PINNED_ARTICLE = "pinned_article";
    public final static String LIST_ARTICLE = "list_article";
}
