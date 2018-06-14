package com.zalesskyi.android.diploma.presenter;


import android.graphics.Bitmap;

import com.zalesskyi.android.diploma.realm.Abstract;
import com.zalesskyi.android.diploma.view.main_operation.listeners.MainListener;

import rx.Observable;

public interface PresenterContract {

    interface MainPresenter {

        /**
         * Получение списка рефератов из локальной БД.
         *
         * @param callback callback отображения списка.
         */
        void doGetListFromRealm(MainListener.ListCallback callback);

        /**
         * Удаление элемента из списка.
         *
         * @param item элемент списка
         */
        void doRemoveItemFromList(Abstract item);
    }
    //----------------------------------------------------------------------------------------------
    interface DetailPresenter {
        /**
         * Получение реферата txt-файла.
         * @param pathToSourceTxt путь к txt-файлу.
         */
        void doGetAbstractFromTxt(String pathToSourceTxt);

        /**
         * Получение реферата pdf-файла.
         * @param pathToSourcePdf путь к pdf-файлу.
         */
        void doGetAbstractFromPdf(String pathToSourcePdf);

        /**
         * Получение реферата doc(x)-файла.
         * @param pathToSourceDoc путь к doc(x)-файлу.
         */
        void doGetAbstractFromDoc(String pathToSourceDoc);

        /**
         * Получение реферата текста.
         *
         * @param sourceText исходный текст
         */
        void doGetAbstractFromString(String sourceText);

        /**
         * Получение реферата текста исходя из его url.
         *
         * @param sourceTextUrl url на web-страницу с исходным текстом
         */
        void doGetAbstractFromUrl(String sourceTextUrl);

        /**
         * @param path путь к doc-файлу.
         * @param pageNum номер страницы, которую надо отобразить.
         *
         * @return картинку страницы doc-файла.
         */
        Observable<Bitmap> doGetDocPageImage(String path, int pageNum);

        Observable<Integer> doGetWordDocumentPageCount(String pathToDoc);

        /**
         * Создание txt-файла реферата.
         *  @param pathToSource путь к исходному файлу.
         * @param abstractText содержимое реферата.
         */
        String doCreateTxtFileWithAbstract(String pathToSource, String abstractText);
    }
}
