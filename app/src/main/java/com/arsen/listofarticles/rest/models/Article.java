package com.arsen.listofarticles.rest.models;

import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.rest.models.interfaces.ArticleModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article {

    @Expose
    @SerializedName("response")
    private Response response;

    public class Response {
        @Expose
        @SerializedName("content")
        private Content content;

        public class Content implements ArticleModel {

            @Expose
            @SerializedName("id")
            private String id;

            @Expose
            @SerializedName("fields")
            private Fields fields;

            @Expose
            @SerializedName("sectionName")
            private String sectionName;

            public class Fields implements ArticleField {
                private String id;
                private String category;

                @Expose
                @SerializedName("headline")
                private String title;

                @Expose
                @SerializedName("thumbnail")
                private String thumbnail;

                @Override
                public String getId() {
                    return null;
                }

                void setId(String id) {
                    this.id = id;
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

                public void setCategory(String category) {
                    this.category = category;
                }
            }

            public Fields getFields() {
                fields.setCategory(this.sectionName);
                fields.setId(this.id);

                return fields;
            }

            @Override
            public String getId() {
                return this.id;
            }
        }

        public Content getContent() {
            return content;
        }
    }

    public Response getResponse() {
        return response;
    }
}
