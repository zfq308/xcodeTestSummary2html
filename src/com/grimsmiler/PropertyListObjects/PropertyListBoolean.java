package com.grimsmiler.PropertyListObjects;

import java.lang.Boolean;
import java.lang.String;

/**
 * Created by ilya on 04/11/15.
 */
public class PropertyListBoolean extends PropertyListObject {

    public PropertyListBoolean(String propertyName, Boolean propertyValue) {
        super.propertyName = propertyName;
        super.propertyValue = propertyValue;
    }

    @Override
    public Boolean getPropertyValue() {
        return (Boolean) super.getPropertyValue();
    }

}
