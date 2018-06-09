package com.arsen.listofarticles.rest.models;

import com.arsen.listofarticles.common.model.ArticleField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilmsResponse {

    @Expose
    @SerializedName("response")
    private Response response;

    public class Response {
        @Expose
        @SerializedName("results")
        private ArrayList<Film> films;

        public class Film {
            @Expose
            @SerializedName("id")
            private String id;

            @Expose
            @SerializedName("type")
            private String type;

            @Expose
            @SerializedName("sectionId")
            private String sectionId;

            @Expose
            @SerializedName("sectionName")
            private String sectionName;

            @Expose
            @SerializedName("fields")
            private Field fields;

            @Expose
            @SerializedName("isHosted")
            private boolean isHosted;

            @Expose
            @SerializedName("pillarId")
            private String pillarId;

            @Expose
            @SerializedName("pillarName")
            private String pillarName;

            public class Field implements ArticleField {
                @Expose
                @SerializedName("headline")
                private String headline;

                @Expose
                @SerializedName("shortUrl")
                private String shortUrl;

                @Expose
                @SerializedName("thumbnail")
                private String thumbnail;

                public String getShortUrl() {
                    return shortUrl;
                }

                @Override
                public String getTitle() {
                    return headline;
                }

                @Override
                public String getThumbnail() {
                    return thumbnail;
                }
            }

            public String getId() {
                return id;
            }

            public String getType() {
                return type;
            }

            public String getSectionId() {
                return sectionId;
            }

            public String getSectionName() {
                return sectionName;
            }

            public Field getField() {
                return fields;
            }

            public boolean isHosted() {
                return isHosted;
            }

            public String getPillarId() {
                return pillarId;
            }

            public String getPillarName() {
                return pillarName;
            }
        }

        public ArrayList<Film> getFilms() {
            return films;
        }

        public ArrayList<Film.Field> getFields() {
            ArrayList<Film.Field> fields = new ArrayList<>();

            for(Film film: films)
                fields.add(film.getField());

            return fields;
        }
    }

    public Response getResponse() {
        return response;
    }
}
