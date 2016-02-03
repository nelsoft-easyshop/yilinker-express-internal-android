package com.yilinker.expressinternal.dao;

import java.util.HashMap;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by rlcoronado on 12/01/2016.
 */
public abstract class DBTransaction<T extends RealmObject> {

    public abstract boolean add(T object);

    public abstract boolean delete(T object);

    public abstract boolean update(T object);

    public abstract List<T> getAll(Class<T> className);

    public abstract void deleteAll(Class<T> className);

    public abstract void deleteAll(Class<T> className, HashMap<String, Object> query);

}
