package com.zalesskyi.android.diploma.interactor;

public interface InteractorContract {

    /**
     * Запрос на сервер для получения реферата исхзодного текста.
     *
     * @param source исходный текст.
     * @param coefficient коэффициент реферирования.
     * @return поток данных с сервера (реферат)
     */
    void toDoGetAbstract(String source, Integer coefficient);
}
