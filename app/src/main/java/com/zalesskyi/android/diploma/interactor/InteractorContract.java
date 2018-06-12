package com.zalesskyi.android.diploma.interactor;

import com.zalesskyi.android.diploma.model.Response;

import rx.Observable;

public interface InteractorContract {

    /**
     * Запрос на сервер для получения реферата исхзодного текста.
     *
     * @param source исходный текст.
     * @param coefficient коэффициент реферирования.
     * @return поток данных с сервера (реферат)
     */
    Observable<Response> toDoGetAbstract(String source, Integer coefficient);
}
