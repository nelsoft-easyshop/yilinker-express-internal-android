package com.yilinker.expressinternal.dao;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by rlcoronado on 12/01/2016.
 */
public class SyncDBTransaction<T extends RealmObject> extends DBTransaction<T> {

    private Context context;

    public SyncDBTransaction(Context context) {
        this.context = context;
    }

    /**
     * Adds record to realm database
     *
     * @param object
     * @return
     */
    @Override
    public boolean add(final T object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealmOrUpdate(object);
            }
        });


        return false;
    }

    /**
     * Deletes specified object from realm database
     *
     * @param object
     * @return
     */
    @Override
    public boolean delete(final T object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                object.removeFromRealm();

            }
        });


        return false;
    }

    /**
     * Same function as add, only using update for
     * proper wording
     *
     * @param object
     * @return
     */
    @Override
    public boolean update(final T object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealmOrUpdate(object);
            }
        });

        return false;
    }

    /**
     * Returns a list of objects queried
     *
     * @param className
     * @return
     */
    @Override
    public List<T> getAll(Class<T> className) {

        Realm realm = Realm.getInstance(context);
        return realm.allObjects(className);
    }

    /**
     * Delete all objects from given class
     * @param className
     */
    @Override
    public void deleteAll(final Class<T> className) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.clear(className);
            }
        });


    }


}
