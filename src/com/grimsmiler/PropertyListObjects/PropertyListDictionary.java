package com.grimsmiler.PropertyListObjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ilya on 04/11/15.
 */
public class PropertyListDictionary extends PropertyListObject {

    private Map<String,PropertyListObject> mapPLO = new HashMap<String, PropertyListObject>();

    public PropertyListDictionary(String propertyName) {
        super.propertyName = propertyName;
    }

    public PropertyListObject getProperty(String propertyName) {
        return mapPLO.get(propertyName);
    }

    public void addPropertyToPropertyList (String name, PropertyListObject plo) {
        mapPLO.putIfAbsent(name, plo);
    }

    public void addPropertiesToPropertyList(List<PropertyListObject> plo) {
        for (PropertyListObject obj : plo)
        {
            mapPLO.put(obj.propertyName,obj);
        }
    }

    public Object getPropertyValue(String propertyName) {
        return getProperty(propertyName).getPropertyValue();
    }
}
