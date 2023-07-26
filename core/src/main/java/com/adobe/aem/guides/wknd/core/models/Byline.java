package com.adobe.aem.guides.wknd.core.models;

import java.util.List;

/**
* Represents the Byline AEM Component for the WKND Site project.
**/
public interface Byline {
    /***
    * @return a string to display as the name.
    */
    String getName();

    /***
    * Occupations are to be sorted alphabetically in a descending order.
    *
    * @return a list of occupations.
    */
    List<String> getOccupations();

    /***
    * @return a boolean if the component has enough content to display.
    */
    boolean isAnyFieldEmpty();

    /***
     * @return a boolean based on the state of the name field.
     */
    boolean isNameEmpty();

    /***
     * @return a boolean based on the state of the occupations field.
     */
    boolean isOccupationsEmpty();

    /***
     * @return a boolean based on the state of the occupations field.
     */
    boolean isImageEmpty();
}
