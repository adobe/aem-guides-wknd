/*
 *  Copyright 2015 Adobe Systems Incorporated
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
package com.adobe.aem.guides.wknd.it.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.testing.clients.instance.InstanceConfiguration;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

import com.adobe.aem.graphql.client.AEMHeadlessClient;
import com.adobe.aem.graphql.client.AEMHeadlessClientException;
import com.adobe.aem.graphql.client.GraphQlResponse;
import com.adobe.aem.graphql.client.PersistedQuery;
import com.adobe.cq.testing.junit.rules.CQAuthorPublishClassRule;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * GraphQL tests.
 */
public class GraphQlIT {

	private static final String TEST_AUTHOR_FIRST_NAME = "Ian";
    private static final String TEST_AUTHOR_LAST_NAME = "Provo";

	@ClassRule
	public static final CQAuthorPublishClassRule cqBaseClassRule = new CQAuthorPublishClassRule();

	static AEMHeadlessClient headlessClientAuthor;

	@BeforeClass
	public static void beforeClass() {
		headlessClientAuthor = getHeadlessClient(cqBaseClassRule.authorRule.getConfiguration());
	}

	private static AEMHeadlessClient getHeadlessClient(InstanceConfiguration instanceConfig) {
		return AEMHeadlessClient.builder() //
				.endpoint(instanceConfig.getUrl()) //
				.basicAuth(instanceConfig.getAdminUser(), instanceConfig.getAdminPassword()).build();
	}

	@Test
	public void testQuery() {

		String query = "{\n" + //
				"  articleList{\n" + //
				"    items{ \n" + //
				"      _path\n" + //
				"      authorFragment{\n" + //
                "        firstName\n" + //
                "        lastName\n" + //
                "         }\n" + //
				"    } \n" + //
				"  }\n" + //
				"}";
		GraphQlResponse response = headlessClientAuthor.runQuery(query);
		assertNull(response.getErrors());
		JsonNode responseData = response.getData();

		assertNotNull(responseData);
		JsonNode articleList = responseData.get("articleList");
		assertNotNull(articleList);
		JsonNode articleListItems = articleList.get("items");
		assertNotNull(articleListItems);
		assertEquals(7, articleListItems.size());
		assertNotNull(articleListItems.get(0).get("_path"));
		assertNotNull(articleListItems.get(0).get("authorFragment"));
	}

	@Test
	public void testQueryWithSyntaxError() {
        String query = "{\n" + //
				"  articleList{\n" + //
				"    items{ \n" + //
				"      _path\n" + //
				"      author\n";

        AEMHeadlessClientException exception = assertThrows(AEMHeadlessClientException.class, 
            () -> headlessClientAuthor.runQuery(query));
        assertTrue(exception.getMessage().contains("Invalid Syntax : offending token"));
	}

	@Test
	public void testQueryWithErrorResponse() {

        String query = "{ nonExisting { items{  _path } } }";
        AEMHeadlessClientException exception = assertThrows(AEMHeadlessClientException.class,
            () -> headlessClientAuthor.runQuery(query));
        
        assertTrue(exception.getMessage().contains("Field 'nonExisting' in type 'QueryType' is undefined"));

	}

	@Test
	public void testQueryWithParameters() {

		String query = "query($authorFirstName: String, $authorLastName: String) {\n" +
            "articleList(filter: {\n" + 
              "authorFragment: {\n" +
                "firstName: {\n" +
                  "_expressions: {\n" +
                    "value: $authorFirstName\n" +
                  "}\n" +
                "}\n" +
                "lastName: {\n" +
                  "_expressions: {\n" +
                    "value: $authorLastName\n" +
                  "}\n" +
                "}\n" +
              "}\n" +
            "}) {\n" +
              "items {\n" +
                "_path\n" +
                "authorFragment {\n" +
                  "firstName\n" +
                  "lastName\n" +
                "}\n" +
              "}\n" +
            "}\n" +
          "}";          

		Map<String, Object> vars = new HashMap<>();
		vars.put("authorFirstName", TEST_AUTHOR_FIRST_NAME);
        vars.put("authorLastName", TEST_AUTHOR_LAST_NAME);

		GraphQlResponse response = headlessClientAuthor.runQuery(query, vars);
		assertNull(response.getErrors());
		JsonNode responseData = response.getData();

		assertNotNull(responseData);
		JsonNode articleList = responseData.get("articleList");
		assertNotNull(articleList);
		JsonNode articleListItems = articleList.get("items");
		assertNotNull(articleListItems);
		assertEquals(1, articleListItems.size());
		assertEquals(TEST_AUTHOR_FIRST_NAME, articleListItems.get(0).get("authorFragment").get("firstName").asText());
        assertEquals(TEST_AUTHOR_LAST_NAME, articleListItems.get(0).get("authorFragment").get("lastName").asText());
	}

	@Test
	public void testPersistedQuery() {

		GraphQlResponse response = headlessClientAuthor.runPersistedQuery("/wknd/adventures-all");
		assertNull(response.getErrors());
		JsonNode responseData = response.getData();

		assertNotNull(responseData);
		JsonNode adventureListList = responseData.get("adventureList");
		assertNotNull(adventureListList);
		JsonNode adventureListItems = adventureListList.get("items");
		assertNotNull(adventureListItems);
		assertEquals(16, adventureListItems.size());
		JsonNode firstAdvantureItem = adventureListItems.get(0);
		assertNotNull(firstAdvantureItem.get("_path"));
		assertNotNull(firstAdvantureItem.get("adventureTitle"));
		assertNotNull(firstAdvantureItem.get("adventurePrice"));
		assertNotNull(firstAdvantureItem.get("adventureTripLength"));
		assertNotNull(firstAdvantureItem.get("adventurePrimaryImage"));

	}

	@Test
	public void testListPersistedQueries() {

		List<PersistedQuery> listPersistedQueries = headlessClientAuthor.listPersistedQueries("wknd");

		assertFalse(listPersistedQueries.isEmpty());
		PersistedQuery adventuresQuery = listPersistedQueries.stream()
				.filter(p -> p.getShortPath().equals("/wknd/adventures-all")).findFirst().get();
		assertEquals("/wknd/settings/graphql/persistentQueries/adventures-all", adventuresQuery.getLongPath());
		assertThat(adventuresQuery.getQuery(), containsString("adventureList {"));
	}

}
