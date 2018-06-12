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
     * @param isForUploading предназначение открытия файла (для загрузки реферата?)
     */
    void openTxtFile(String path, boolean isForUploading);

    /**
     * Открыть pdf-файл.
     * Метод открывает DetailActivity.
     *
     * @param path путь к файлу.
     * @param isForUploading предназначение открытия файла (для загрузки реферата?)
     */
    void openPdfFile(String path, boolean isForUploading);

    /**
     * Открыть doc-файл.
     * Метод открывает DetailActivity.
     *
     * @param path путь к файлу.
     * @param isForUploading предназначение открытия файла (для загрузки реферата?)
     */
    void openDocFile(String path, boolean isForUploading);

    //todo String openDriveFile(String );

    /**
     * Открыть web-страницу.
     * Метод открывает DetailActivity.
     *
     * @param url URL web-страницы.
     * @param isForUploading предназначение открытия файла (для загрузки реферата?)
     */
    void openWebPage(String url, boolean isForUploading);

    /**
     * Открыть экран с текстом из буфера обмена.
     *
     * @param isForUploading предназначение открытия файла (для загрузки реферата?)
     */
    void openClipboardText(boolean isForUploading);
}
