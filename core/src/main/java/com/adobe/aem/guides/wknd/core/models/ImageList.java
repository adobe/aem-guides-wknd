package com.adobe.aem.guides.wknd.core.models;

import org.apache.sling.api.resource.Resource;

import java.util.Collection;

import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the WKND Image List Component
 */
public interface ImageList {

    /**
     * @return a collection of objects representing the items that compose the the list.
     */
    Collection<ImageList.ListItem> getListItems();

    /**
     * @return true if this component has no list items to display.
     */
    boolean isEmpty();

    /**
     * 
     * @return JSON data to populate the data layer
     */
    @JsonProperty("dataLayer")
    default ComponentData getData() {
        return null;
    }

    /**
     * 
     * @return String representing the unique identifier of the ImageList component on a page
     */
    String getId();

    /**
     * Describes a item of the Image List.
     */
    interface ListItem{
        /**
         * This method returns a resource that is an WKND Image Component resource (rather than an image binary, such as a DAM asset).
         * This resource is intended to be rendered via the WKND Image Component's logic via a Sling include of this resource.
         *
         * @return the (Sling) resource that represents that image to display in the list.
         */
        Resource getImage();

        /**
         * @return the title of the Image List item (Page).
         */
        String getTitle();

        /**
         * @return the description of the Image List item (Page).
         */
        String getDescription();

        /**
         * @return the url to the Page the Image List item represents.
         */
        String getURL();

        @JsonProperty("dataLayer")
        default ComponentData getData() {
            return null;
        }

        /**
         * 
         * @return String representing the unique identifier of the ImageList component on a page
         */
        String getId();
    }
}
