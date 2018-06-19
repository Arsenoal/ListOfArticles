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
                public String getArticleId() {
                    return this.id;
                }

                @Override
                public String getId() {
                    return null;
                }

                void setId(String id) {
                    this.id = id;
                }

                @Override
                public boolean equals(Object o) {
                    if (!(o instanceof Field)) return false;

                    Field field = (Field) o;

                    return
                            getArticleId().equals(field.getArticleId())
                                    && (getCategory() != null ? getCategory().equals(field.getCategory()) : field.getCategory() == null)
                                    && (headline != null ? headline.equals(field.headline) : field.headline == null)
                                    && (getShortUrl() != null ? getShortUrl().equals(field.getShortUrl()) : field.getShortUrl() == null)
                                    && (getThumbnail() != null ? getThumbnail().equals(field.getThumbnail()) : field.getThumbnail() == null);
                }

                @Override
                public int hashCode() {
                    int result = getArticleId().hashCode();
                    result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
                    result = 31 * result + (headline != null ? headline.hashCode() : 0);
                    result = 31 * result + (getShortUrl() != null ? getShortUrl().hashCode() : 0);
                    result = 31 * result + (getThumbnail() != null ? getThumbnail().hashCode() : 0);
                    return result;
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
