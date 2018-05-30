package com.zalesskyi.android.diploma.presenter;

import java.io.File;

public interface PresenterContract {

    interface MainPresenter {

        /**
         * Получение реферата текста, который содержится в файле.
         * Файл может быть 3 видов:
         *  1) PDF
         *  2) DOC/DOCX
         *  3) TXT
         *
         * Текст извлекается из файла и отправляется на сервер.
         *
         * @param sourceText файл с исходным текстом
         */
        void doGetAbstractFromFile(File sourceText);

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
}
