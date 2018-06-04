package com.zalesskyi.android.diploma.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Abstract extends RealmObject {
    @PrimaryKey
    private long mId;
    private String mPathToSource;       // или URL
    private String mPathToAbstract;     // для web-страницы - ссылка на txt-файл с рефератом
    private String mPathToAbstractThumb;
    private String mPathToImagesIfDoc;  // путь к директории с изображениями страниц
    private String mIsFavorite;
    private long mCreationDate;
    private int mType;
    private boolean mIsSyncedWithCloud;

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
        mId = builder.mId;
        mPathToSource = builder.mPathToSource;
        mPathToAbstract = builder.mPathToAbstract;
        mPathToAbstractThumb = builder.mPathToAbstractThumb;
        mIsFavorite = builder.mIsFavorite;
        mCreationDate = builder.mCreationDate;
        mType = builder.mType;
        mIsSyncedWithCloud = builder.mIsSyncedWithCloud;
        mPathToImagesIfDoc = builder.mPathToImagesIfDoc;
    }

    public long getId() {
        return mId;
    }

    public String getPathToSource() {
        return mPathToSource;
    }

    public String getPathToAbstract() {
        return mPathToAbstract;
    }

    public String getPathToAbstractThumb() {
        return mPathToAbstractThumb;
    }

    public String getIsFavorite() {
        return mIsFavorite;
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    public int getType() {
        return mType;
    }

    public boolean isSyncedWithCloud() {
        return mIsSyncedWithCloud;
    }

    public String getPathToImagesIfDoc() {
        return mPathToImagesIfDoc;
    }

    private static class Builder {
        private long mId;
        private String mPathToSource;
        private String mPathToAbstract;
        private String mPathToImagesIfDoc;
        private String mPathToAbstractThumb;
        private String mIsFavorite;
        private long mCreationDate;
        private int mType;
        private boolean mIsSyncedWithCloud;

        public void setId(long id) {
            mId = id;
        }

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
