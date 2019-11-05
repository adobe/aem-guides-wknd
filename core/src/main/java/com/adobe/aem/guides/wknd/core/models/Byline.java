/*
 *  Copyright 2019 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
	 * @return a boolean if the component has content to display.
	 */
	boolean isEmpty();
}