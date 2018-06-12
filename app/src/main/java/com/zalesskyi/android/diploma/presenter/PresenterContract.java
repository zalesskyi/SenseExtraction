package com.zalesskyi.android.diploma.presenter;


import android.graphics.Bitmap;

import rx.Observable;

public interface PresenterContract {

    interface MainPresenter {

        /**
         * Поделиться результатом реферирования.
         *
         * @param id id реферата
         */
        void shareAbstract(long id);

        /**
         * Отметить результат реферирования, как favorite.
         *
         * @param id id реферата
         */
        void starAbstract(long id);

        /**
         * Открыть файл с результатом реферирования с помощью одного из приложений,
         * установленных на устройстве.
         *
         * @param id id реферата
         */
        void openWithAbstract(long id);

        /**
         * Удалить реферат с устройства.
         *
         * @param id id реферата.
         */
        void removeAbstract(long id);
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
         *
         * @param pathToSource путь к исходному файлу.
         * @param abstractText содержимое реферата.
         */
        void doCreateTxtFileWithAbstract(String pathToSource, String abstractText);

        /**
         * Создание pdf-файла реферата.
         *
         * @param pathToSource путь к исходному файлу.
         * @param abstractText содержимое реферата.
         */
        void doCreatePdfFileWithAbstract(String pathToSource, String abstractText);

        /**
         * Создание doc-файла реферата.
         *
         * @param pathToSource путь к исходному файлу.
         * @param abstractText содержимое реферата.
         */
        void doCreateDocFileWithAbstract(String pathToSource, String abstractText);
    }
}
