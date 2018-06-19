package com.arsen.listofarticles.common.db;

import java.util.Locale;

public class ArticlesTable {
    public static final String ARTICLES_TABLE = "articles";
    public static final String PINNED_ARTICLES_TABLE = "pinned_articles";

    public static class COLUMN {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String CATEGORY = "category";
        public static final String THUMBNAIL = "thumbnail";
    }

    public static final String CREATE_SCRIPT =
            String.format(Locale.ENGLISH, "create table if not exists %s ("
                            + "%s integer primary key autoincrement,"
                            + "%s text,"
                            + "%s text,"
                            + "%s text" + ");",
                    ARTICLES_TABLE, COLUMN.ID, COLUMN.TITLE, COLUMN.CATEGORY, COLUMN.THUMBNAIL);

    public static final String CREATE_PINNED_ARTICLES_SCRIPT =
            String.format(Locale.ENGLISH, "create table if not exists %s ("
                            + "%s integer primary key autoincrement,"
                            + "%s text,"
                            + "%s text,"
                            + "%s text" + ");",
                    PINNED_ARTICLES_TABLE, COLUMN.ID, COLUMN.TITLE, COLUMN.CATEGORY, COLUMN.THUMBNAIL);

}
