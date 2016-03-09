package com.yilinker.expressinternal.mvp.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by J.Bautista on 3/7/16.
 */
public abstract class BaseMapAdapter<M , O, V> {

    private List<M>  mapMarkers;
    private List<O> objects;
    private HashMap<O,M> mapValues;
    private V map;

    public BaseMapAdapter(List<O> objects, V map){

        this.objects = objects;
        this.map = map;

        mapValues = new HashMap<>();

        createMapMarker();
    }

    protected abstract M createMapMarker(O object);

    protected abstract void addToMap(M marker);

    protected abstract void removeFromMap(M marker);

    public abstract void clearMap();

    public abstract O getObjectById(String id);

    public void removeMarkers(List<O> objects){

        for(O object : objects){

            removeMarker(object);
        }

    }

    public void addMarkers(List<O> objects){

        for(O object : objects){

            addMarker(object);
        }
    }

    public List<M> getMapMarkers(){

        return this.mapMarkers;
    }

    protected V getMap(){

        return this.map;
    }

    public void removeMarker(O object){

        M mapMarker = mapValues.get(object);

        mapMarkers.remove(mapMarker);
        mapValues.remove(object);

        removeFromMap(mapMarker);
    }

    public void addMarker(O object){

        M marker = createMapMarker(object);

        mapMarkers.add(marker);
        mapValues.put(object, marker);

        addToMap(marker);
    }

    public O getObject(M mapMarker){

        Set<O> keys = mapValues.keySet();

        for(O object : keys){

            if(mapValues.get(object).equals(mapMarker)){

                return object;
            }
        }

        return null;
    }

    public void updateMarker(M marker, O object){

        O oldObject = getObject(marker);

        if(oldObject != null){

            removeMarker(oldObject);
            addMarker(object);
        }

    }

    private void createMapMarker(){

        if(objects != null){

            mapMarkers = new ArrayList<>();

            for(O object : objects) {

                addMarker(object);

            }

        }

    }



}
