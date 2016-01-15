package com.yilinker.expressinternal.dao;

import java.util.List;

/**
 * Created by rlcoronado on 12/01/2016.
 */
public abstract class DBTransaction<T> {

    public abstract boolean add(T object);

    public abstract boolean delete(T object);

    public abstract boolean update(T object);

    public abstract List<T> getAll();

//    public abstract List<T> get(HashMap<T, T> query);

}
