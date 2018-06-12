package com.arsen.listofarticles.rest.models;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.rest.models.interfaces.ArticleModel;
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

        public class Film implements ArticleModel {
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
            @SerializedName("isHosted")
            private boolean isHosted;

            @Expose
            @SerializedName("pillarId")
            private String pillarId;

            @Expose
            @SerializedName("pillarName")
            private String pillarName;

            @Expose
            @SerializedName("fields")
            private Field field;

            @Override
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
                return field;
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

            public class Field implements ArticleField {
                private String id;
                private String category;

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

                @Override
                public String getCategory() {
                    return this.category;
                }

                void setCategory(String category) {
                    this.category = category;
                }

                @Override
                public String getId() {
                    return this.id;
                }

                void setId(String id) {
                    this.id = id;
                }
            }
        }

        public ArrayList<Film> getFilms() {
            return films;
        }

        public ArrayList<Film.Field> getFields() {
            ArrayList<Film.Field> fields = new ArrayList<>();

            for (Film film : films) {
                Film.Field field = film.getField();
                field.setCategory(film.getSectionName());
                field.setId(film.getId());
                fields.add(field);
            }
            return fields;
        }
    }

    public Response getResponse() {
        return response;
    }
}
