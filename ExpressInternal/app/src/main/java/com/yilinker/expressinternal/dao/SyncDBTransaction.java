package com.yilinker.expressinternal.dao;

import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    @Override
    public void deleteAll(final Class<T> className, HashMap<String, Object> query) {

        Realm realm = Realm.getInstance(context);

        final RealmResults<T> finalResult = filter(className, query);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                finalResult.clear();
            }
        });

    }

    private RealmResults<T> filter(Class<T> className, HashMap<String, Object> query) {

        Realm realm = Realm.getInstance(context);

        Iterator iterator = query.entrySet().iterator();
        RealmResults<T> results = realm.allObjects(className);

        while (iterator.hasNext()) {

            Map.Entry pair = (Map.Entry) iterator.next();
            Object value = pair.getValue();

            //For proper casting
            if (value instanceof Integer) {

                results = results.where().equalTo(pair.getKey().toString(), (int) pair.getValue()).findAll();

            } else if (value instanceof String) {

                results = results.where().equalTo(pair.getKey().toString(), pair.getValue().toString()).findAll();

            } else if (value instanceof Boolean) {

                results = results.where().equalTo(pair.getKey().toString(), (boolean) pair.getValue()).findAll();

            } else if (value instanceof Double) {

                results = results.where().equalTo(pair.getKey().toString(), (double) pair.getValue()).findAll();

            } else if (value instanceof Short) {

                results = results.where().equalTo(pair.getKey().toString(), (short) pair.getValue()).findAll();

            } else if (value instanceof Long) {

                results = results.where().equalTo(pair.getKey().toString(), (long) pair.getValue()).findAll();

            } else if (value instanceof Float) {

                results = results.where().equalTo(pair.getKey().toString(), (float) pair.getValue()).findAll();

            } else if (value instanceof Byte) {

                results = results.where().equalTo(pair.getKey().toString(), (byte) pair.getValue()).findAll();
            }


            iterator.remove();
        }

        return results;
    }
}
