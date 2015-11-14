package com.grimsmiler.PropertyListObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya on 05/11/15.
 */
public class PropertyListArray extends PropertyListObject {

    private List<PropertyListObject> array = new ArrayList<PropertyListObject>();

    public PropertyListArray(String propertyName) {
        super.propertyName = propertyName;
    }

    public void add (PropertyListObject plo) {
        array.add(plo);
    }

    public void addFromList(List<PropertyListObject> propertyListObjectList) {
        for (PropertyListObject obj : propertyListObjectList) {
            array.add(obj);
        }
    }

    public List<PropertyListObject> getList() {
        return array;
    }

}

