package com.yilinker.expressinternal.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rlcoronado on 12/01/2016.
 */
public class SyncDBTransaction extends DBTransaction<SyncDBObject>{

    private Context context;

    public SyncDBTransaction(Context context) {
        this.context = context;
    }

    @Override
    public boolean add(final SyncDBObject object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealmOrUpdate(object);
            }
        });


        return false;
    }

    @Override
    public boolean delete(final SyncDBObject object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {

                object.removeFromRealm();

            }
        });



        return false;
    }

    @Override
    public boolean update(final SyncDBObject object) {

        Realm realm = Realm.getInstance(context);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.copyToRealmOrUpdate(object);
            }
        });

        return false;
    }

    @Override
    public RealmResults<SyncDBObject> getAll() {

        Realm realm = Realm.getInstance(context);

        RealmResults<SyncDBObject> results = realm.allObjects(SyncDBObject.class);

        return results;

    }
}
