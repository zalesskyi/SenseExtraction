package com.zalesskyi.android.diploma.realm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;

public interface RealmService {
    Realm get();

    void closeRealm();

    <T extends RealmObject> Observable<T> addObject(T obj, Class<T> clazz);

    <T extends RealmObject> Observable<RealmResults<T>> getObjects(Class<T> clazz);

    <T extends RealmObject> Observable<Class<T>> deleteObject(String id, Class<T> clazz);

    <T extends RealmObject> Observable<Class<T>> deleteAllObjects(Class<T> clazz);

    <T extends RealmObject> Observable<T> getObject(String id, Class<T> clazz);
}


