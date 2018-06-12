package com.zalesskyi.android.diploma.realm;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Abstract extends RealmObject {
    @PrimaryKey
    private String id;
    private String pathToSource;       // или URL
    private String pathToAbstract;     // для web-страницы, буфера обмена - ссылка на txt-файл с рефератом
    private String pathToAbstractThumb;
    private String pathToImagesIfDoc;  // путь к директории с изображениями страниц
    private String isFavorite;
    private long creationDate;
    private int type;
    private boolean isSyncedWithCloud;

    private static Builder sBuilder;

    public static Builder builder() {
        if (sBuilder == null) {
            return new Abstract.Builder();
        }
        return sBuilder;
    }

    public Abstract() {

    }

    public Abstract(Builder builder) {
        id = builder.mId;
        pathToSource = builder.mPathToSource;
        pathToAbstract = builder.mPathToAbstract;
        pathToAbstractThumb = builder.mPathToAbstractThumb;
        isFavorite = builder.mIsFavorite;
        creationDate = builder.mCreationDate;
        type = builder.mType;
        isSyncedWithCloud = builder.mIsSyncedWithCloud;
        pathToImagesIfDoc = builder.mPathToImagesIfDoc;
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

    public String getPathToAbstractThumb() {
        return pathToAbstractThumb;
    }

    public String getIsFavorite() {
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

    public String getPathToImagesIfDoc() {
        return pathToImagesIfDoc;
    }

    private static class Builder {
        private String mId = UUID.randomUUID().toString();
        private String mPathToSource;
        private String mPathToAbstract;
        private String mPathToImagesIfDoc;
        private String mPathToAbstractThumb;
        private String mIsFavorite;
        private long mCreationDate;
        private int mType;
        private boolean mIsSyncedWithCloud;

        public void setPathToSource(String pathToSource) {
            mPathToSource = pathToSource;
        }

        public void setPathToAbstract(String pathToAbstract) {
            mPathToAbstract = pathToAbstract;
        }

        public void setPathToAbstractThumb(String pathToAbstractThumb) {
            mPathToAbstractThumb = pathToAbstractThumb;
        }

        public void setIsFavorite(String isFavorite) {
            mIsFavorite = isFavorite;
        }

        public void setCreationDate(long creationDate) {
            mCreationDate = creationDate;
        }

        public void setType(int type) {
            mType = type;
        }

        public void setSyncedWithCloud(boolean syncedWithCloud) {
            mIsSyncedWithCloud = syncedWithCloud;
        }

        public Abstract build() {
            return new Abstract(this);
        }

        public void setPathToImagesIfDoc(String pathToImagesIfDoc) {
            mPathToImagesIfDoc = pathToImagesIfDoc;
        }
    }
}
