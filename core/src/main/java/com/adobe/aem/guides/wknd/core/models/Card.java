package com.adobe.aem.guides.wknd.core.models;

/**
* Represents the Card AEM Component for the WKND Site project.
**/
public interface Card {
    /***
    * @return a string to display as the name.
    */
    String getTitle();

    /***
    * Occupations are to be sorted alphabetically in a descending order.
    *
    * @return a list of occupations.
    */
    String getDesc();

    /***
    * @return a boolean if the component has enough content to display.
    */
    boolean isEmpty();
} 