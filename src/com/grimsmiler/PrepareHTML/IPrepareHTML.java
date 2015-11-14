package com.grimsmiler.PrepareHTML;

import j2html.tags.ContainerTag;
import j2html.tags.Tag;

/**
 * Created by ilya on 07/11/15.
 */
public abstract class IPrepareHTML {

    protected static Tag addPropertyAsAttribute(Tag tag, String propertyName, String propertyValue) {
        if (propertyValue != null)
        {
            tag.setAttribute(propertyName,propertyValue);
        }
        return tag;
    }

    protected static ContainerTag addPropertyAsAttribute(ContainerTag tag, String propertyName, String propertyValue) {
        if (propertyValue != null)
        {
            tag.setAttribute(propertyName,propertyValue);
        }
        return tag;
    }
}
