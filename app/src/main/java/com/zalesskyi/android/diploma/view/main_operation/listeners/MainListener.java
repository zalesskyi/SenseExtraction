package com.zalesskyi.android.diploma.view.main_operation.listeners;

import rx.Observable;

public interface MainListener {

    /**
     * Выбор пользователем doc-файла.
     * @return путь к doc-файлу.
     */
    Observable<String> getDocFile();

    /**
     * Указание пользователем URL web-страницы.
     * @return URL web-страницы
     */
    Observable<String> getLink();

    /**
     * Выбор пользователем pdf-файла.
     * @return путь к pdf-файлу.
     */
    Observable<String> getPdfFile();

    /**
     * Выбор пользователем txt-файла.
     * @return путь к txt-файлу.
     */
    Observable<String> getTxtFile();

    /**
     * Выбор пользователем файла с google drive.
     * @return путь к файлу с google drive.
     */
    Observable<String> getFileFromDrive();

    //----------------------------------------------------------------------------------------------

    /**
     * Открыть txt-файл.
     * Метод открывает DetailActivity.
     *
     * @param path путь к файлу.
     */
    void openTxtFile(String path);

    /**
     * Открыть pdf-файл.
     * Метод открывает DetailActivity.
     *
     * @param path путь к файлу.
     */
    void openPdfFile(String path);

    /**
     * Открыть doc-файл.
     * Метод открывает DetailActivity.
     *
     * @param path путь к файлу.
     */
    void openDocFile(String path);

    //todo String openDriveFile(String );

    /**
     * Открыть web-страницу.
     * Метод открывает DetailActivity.
     *
     * @param url URL web-страницы.
     */
    void openWebPage(String url);
}
