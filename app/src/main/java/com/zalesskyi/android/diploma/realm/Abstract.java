package com.zalesskyi.android.diploma.realm;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Abstract extends RealmObject {
    @PrimaryKey
    private String id;
    private String pathToSource;       // или URL
    private String pathToAbstract;     // для web-страницы, буфера обмена - ссылка на txt-файл с рефератом
    private String pathToSourceThumb;
    private String pathToSourceImagesIfDoc; // путь к изображениям, если исходный файл - DOC(X)
    private boolean isFavorite;
    private long creationDate;
    private int type;
    private boolean isSyncedWithCloud;

    @Ignore public static final int PDF_TYPE = 0;
    @Ignore public static final int DOC_TYPE = 1;
    @Ignore public static final int TXT_TYPE = 2;
    @Ignore public static final int WEB_TYPE = 3;
    @Ignore public static final int CLIPBOARD_TYPE = 4;

    public Abstract() {

    }

    public Abstract(Builder builder) {
        id = builder.mId;
        pathToSource = builder.mPathToSource;
        pathToAbstract = builder.mPathToAbstract;
        pathToSourceThumb = builder.mPathToSourceThumb;
        pathToSourceImagesIfDoc = builder.mPathToImagesIfDoc;
        isFavorite = builder.mIsFavorite;
        creationDate = builder.mCreationDate;
        type = builder.mType;
        isSyncedWithCloud = builder.mIsSyncedWithCloud;
    }

    public String getId() {
        return id;
    }

    public String getPathToSource() {
        return pathToSource;
    }

    public String getPathToAbstract() {
        return pathToAbstract;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public int getType() {
        return type;
    }

    public boolean isSyncedWithCloud() {
        return isSyncedWithCloud;
    }

    public String getPathToSourceThumb() {
        return pathToSourceThumb;
    }

    public String getPathToSourceImagesIfDoc() {
        return pathToSourceImagesIfDoc;
    }

    @Override
    public String toString() {
        return "id : " + id + "\n" +
                "path to source : " + pathToSource + "\n" +
                "path to abstract : " + pathToAbstract + "\n" +
                "path to source thumb : " + pathToSourceThumb + "\n" +
                "path to images if doc : " + pathToSourceImagesIfDoc + "\n";

    }


    public static class Builder {
        private String mId = UUID.randomUUID().toString();
        private String mPathToSource;
        private String mPathToAbstract;
        private String mPathToSourceThumb;
        private String mPathToImagesIfDoc;
        private boolean mIsFavorite;
        private long mCreationDate;
        private int mType;
        private boolean mIsSyncedWithCloud;

        public Builder() {

        }

        public Builder(Abstract abs) {
            mPathToSource = abs.pathToSource;
            mPathToAbstract = abs.pathToAbstract;
            mPathToSourceThumb = abs.pathToSourceThumb;
            mPathToImagesIfDoc = abs.pathToSourceImagesIfDoc;
            mIsFavorite = abs.isFavorite;
            mCreationDate = abs.creationDate;
            mType = abs.type;
            mIsSyncedWithCloud = abs.isSyncedWithCloud;
        }

        public Builder setPathToSource(String pathToSource) {
            mPathToSource = pathToSource;
            return this;
        }

        public Builder setPathToAbstract(String pathToAbstract) {
            mPathToAbstract = pathToAbstract;
            return this;
        }

        public Builder setPathToSourceThumb(String pathToSourceThumb) {
            mPathToSourceThumb = pathToSourceThumb;
            return this;
        }

        public Builder setPathToImagesIfDoc(String pathToImagesIfDoc) {
            mPathToImagesIfDoc = pathToImagesIfDoc;
            return this;
        }

        public Builder setIsFavorite(boolean isFavorite) {
            mIsFavorite = isFavorite;
            return this;
        }

        public Builder setCreationDate(long creationDate) {
            mCreationDate = creationDate;
            return this;
        }

        public Builder setType(int type) {
            mType = type;
            return this;
        }

        public Builder setSyncedWithCloud(boolean syncedWithCloud) {
            mIsSyncedWithCloud = syncedWithCloud;
            return this;
        }

        public Abstract build() {
            return new Abstract(this);
        }
    }
}
