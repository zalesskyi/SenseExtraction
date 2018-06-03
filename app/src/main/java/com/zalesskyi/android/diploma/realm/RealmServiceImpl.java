package com.zalesskyi.android.diploma.realm;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RealmServiceImpl implements RealmService {

    private Realm mRealm;

    public RealmServiceImpl(Realm realm) {
        mRealm = realm;
    }

    @Override
    public Realm get() {
        return null;
    }

    @Override
    public void closeRealm() {

    }

    @Override
    public <T extends RealmObject> Observable<T> addObject(T obj, Class<T> clazz) {
        return Observable.just(obj)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(mRealm::beginTransaction)
                        .doOnUnsubscribe(mRealm::commitTransaction)
                        .doOnError(Throwable::printStackTrace)
                        .doOnNext(next -> mRealm.copyToRealmOrUpdate(next))
                );
    }

    @Override
    public <T extends RealmObject> Observable<RealmResults<T>> getObjects(Class<T> clazz) {
        return Observable.just(clazz)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(mRealm::beginTransaction)
                        .doOnUnsubscribe(mRealm::commitTransaction)
                        .map(type -> mRealm.where(clazz).findAll()));
    }

    @Override
    public <T extends RealmObject> Observable<Class<T>> deleteObject(long id, Class<T> clazz) {
        return Observable.just(clazz)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mRealm::beginTransaction)
                .doOnUnsubscribe(mRealm::commitTransaction)
                .doOnError(Throwable::printStackTrace)
                .doOnNext(type -> {
                    mRealm.where(type).equalTo("realm_id", id).findFirst().deleteFromRealm();
                });
    }

    @Override
    public <T extends RealmObject> Observable<Class<T>> deleteAllObjects(Class<T> clazz) {
        return Observable.just(clazz)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mRealm::beginTransaction)
                .doOnUnsubscribe(mRealm::commitTransaction)
                .doOnError(Throwable::printStackTrace)
                .doOnNext(type -> mRealm.where(clazz).findAll().clear());
    }

    @Override
    public <T extends RealmObject> Observable<T> getObject(long id, Class<T> clazz) {
        return Observable.just(clazz)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Throwable::printStackTrace)
                .flatMap(t -> Observable.just(t)
                        .doOnSubscribe(mRealm::beginTransaction)
                        .doOnUnsubscribe(() -> {
                            mRealm.commitTransaction();
                        })
                        .doOnError(Throwable::printStackTrace)
                        .map(type->mRealm.where(type).equalTo("realm_id", id).findFirst()));
    }
}
