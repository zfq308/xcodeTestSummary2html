package com.grimsmiler.PropertyListObjects;

import java.lang.String;

/**
 * Created by ilya on 04/11/15.
 */
public class PropertyListString extends PropertyListObject {

    public PropertyListString(String propertyName, String propertyValue) {
        super.propertyName = propertyName;
        super.propertyValue = propertyValue;
    }

    @Override
    public String getPropertyValue() {
        return  super.getPropertyValue().toString();
    }
}
