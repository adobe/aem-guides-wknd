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

import com.adobe.cq.testing.client.CQClient;
import com.adobe.cq.testing.junit.rules.CQPublishClassRule;
import com.adobe.cq.testing.junit.rules.CQRule;
import org.apache.sling.testing.clients.ClientException;
import org.apache.sling.testing.clients.SlingHttpResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.*;

import static org.junit.Assert.assertEquals;


public class SamplePublishIT {

    // The CQAuthorClassRule represents an author service. The rule will read
    // the hostname and port of the author service from the system properties
    // passed to the tests.@ClassRule

    @ClassRule
    public static final CQPublishClassRule cqBaseClassRule = new CQPublishClassRule();

    // CQRule decorates your test and adds additional functionality on top of
    // it, like session stickyness, test filtering and identification of the
    // test on the remote service.

    @Rule
    public CQRule cqBaseRule = new CQRule(cqBaseClassRule.publishRule);


    static CQClient adminPublish;

    // Thanks to the CQAuthorClassRule, we can create two CQClient instances
    // bound to the admin user on both the author and publish service.

    @BeforeClass
    public static void beforeClass() throws ClientException {
        adminPublish = cqBaseClassRule.publishRule.getAdminClient(CQClient.class);
    }

    /**
     * Verifies that the homepage exists on author
     * @throws ClientException if cannot connect
     */
    @Test
    public void testPublishPage() throws ClientException {
        SlingHttpResponse response = adminPublish.doGet("/", 200);

        // get html rendered component
        Document document = Jsoup.parse(response.getContent());

        // content is rendered as: <h2 class="cmp-byline__name">Andres</h2>
        Elements name = document.select("title");
        assertEquals("WKND Adventures and Travel",name.text());
    }
}
