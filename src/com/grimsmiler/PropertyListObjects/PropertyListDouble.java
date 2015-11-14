package com.grimsmiler.PropertyListObjects;

/**
 * Created by ilya on 04/11/15.
 */
public class PropertyListDouble extends PropertyListObject {

    public PropertyListDouble(String propertyName, Double propertyValue) {
        super.propertyName = propertyName;
        super.propertyValue = propertyValue;
    }

    @Override
    public Double getPropertyValue() {
        return (Double) super.getPropertyValue();
    }
}
