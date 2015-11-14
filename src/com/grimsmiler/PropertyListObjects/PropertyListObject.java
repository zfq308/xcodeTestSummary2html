package com.grimsmiler.PropertyListObjects;

import java.lang.String;

/**
 * Created by ilya on 04/11/15.
 */

public abstract class PropertyListObject {

    String propertyName;
    Object propertyValue;

    public PropertyListObject(){};

    public PropertyListObject(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    String getPropertyName() {
        return propertyName;
    }

    Object getPropertyValue() {
        return propertyValue;
    }
}
