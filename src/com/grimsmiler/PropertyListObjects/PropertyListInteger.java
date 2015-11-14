package com.grimsmiler.PropertyListObjects;

/**
 * Created by ilya on 04/11/15.
 */
public class PropertyListInteger extends PropertyListObject {

    public PropertyListInteger(String propertyName, Integer propertyValue) {
        super.propertyName = propertyName;
        super.propertyValue = propertyValue;
    }

    @Override
    public Integer getPropertyValue() {
        return (Integer) super.getPropertyValue();
    }
}
