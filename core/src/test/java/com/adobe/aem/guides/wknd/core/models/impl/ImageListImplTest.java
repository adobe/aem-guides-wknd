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
package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ImageList;
import com.adobe.cq.wcm.core.components.internal.DataLayerConfig;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ImageListImplTest {

    private final AemContext ctx = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private QueryBuilder mockQueryBuilder;

    @Mock
    private ResourceResolver leakedResourceResolver;

    @Mock
    private Query page1MockQuery;

    @Mock
    private SearchResult page1MockSearchResult;

    @Mock
    private Query page2MockQuery;

    @Mock
    private SearchResult page2MockSearchResult;

    @Mock
    private Query page3MockQuery;

    @Mock
    private SearchResult page3MockSearchResult;

    @BeforeEach
    void setUp() {
        ctx.load().json("/com/adobe/aem/guides/wknd/core/models/impl/ImageListImplTest.json", "/content");
        ctx.registerService(QueryBuilder.class, mockQueryBuilder);
        ctx.addModelsForClasses(MockList.class);
        ctx.addModelsForClasses(ImageList.class);
    }

    @Test
    void getListItems() throws NoSuchFieldException, IllegalAccessException {
        // Page 1
        Resource page1ImageComponentResource = spy(ctx.resourceResolver().getResource("/content/pages/page-1/jcr:content/root/responsivegrid/page-1-image-component"));
        doReturn(leakedResourceResolver).when(page1ImageComponentResource).getResourceResolver();

        doReturn(page1MockQuery).when(mockQueryBuilder).createQuery(argThat(new PredicateGroupByPath("/content/pages/page-1/jcr:content")), any(Session.class));
        doReturn(page1MockSearchResult).when(page1MockQuery).getResult();
        doReturn(Arrays.asList(new Resource[]{page1ImageComponentResource}).iterator()).when(page1MockSearchResult).getResources();

        // Page 2
        Resource page2ImageComponentResource = spy(ctx.resourceResolver().getResource("/content/pages/page-2/jcr:content/root/responsivegrid/page-2-image-component"));
        doReturn(leakedResourceResolver).when(page2ImageComponentResource).getResourceResolver();

        doReturn(page2MockQuery).when(mockQueryBuilder).createQuery(argThat(new PredicateGroupByPath("/content/pages/page-2/jcr:content")), any(Session.class));
        doReturn(page2MockSearchResult).when(page2MockQuery).getResult();
        doReturn(Arrays.asList(new Resource[]{page2ImageComponentResource}).iterator()).when(page2MockSearchResult).getResources();

        // Page 3 (Without image component)
        doReturn(page3MockQuery).when(mockQueryBuilder).createQuery(argThat(new PredicateGroupByPath("/content/pages/page-3/jcr:content")), any(Session.class));
        doReturn(page3MockSearchResult).when(page3MockQuery).getResult();
        doReturn(Arrays.asList(new Resource[]{}).iterator()).when(page3MockSearchResult).getResources();

        ctx.currentResource("/content/image-list");

        final ImageList actual = ctx.request().adaptTo(ImageList.class);

        setCoreList((ImageListImpl) actual, new MockList("/content/pages/page-1", "/content/pages/page-2", "/content/pages/page-3", "/content/not-a-page"));

        assertNotNull(actual);
        assertEquals(2, actual.getListItems().size());

        ImageList.ListItem[] actualListItems = actual.getListItems().toArray(new ImageList.ListItem[2]);

        assertEquals("/content/pages/page-1/jcr:content/root/responsivegrid/page-1-image-component", actualListItems[0].getImage().getPath());
        assertEquals("Page 1", actualListItems[0].getTitle());
        assertEquals("Page 1 Description", actualListItems[0].getDescription());
        assertEquals("/content/pages/page-1.html", actualListItems[0].getURL());

        assertEquals("/content/pages/page-2/jcr:content/root/responsivegrid/page-2-image-component", actualListItems[1].getImage().getPath());
        assertEquals("Page 2", actualListItems[1].getTitle());
        assertEquals("Page 2 Description", actualListItems[1].getDescription());
        assertEquals("/content/pages/page-2.html", actualListItems[1].getURL());

        assertFalse(actual.isEmpty());

        // Close for each page that has a result
        verify(leakedResourceResolver, times(2)).close();
    }

    @Test
    void getListItems_MissingCoreList() throws NoSuchFieldException, IllegalAccessException {
        ctx.currentResource("/content/image-list");

        final ImageList actual = ctx.request().adaptTo(ImageList.class);

        assertEquals(0, actual.getListItems().size());
        assertTrue(actual.isEmpty());
    }

    @Test
    void SimpleImageComponentResource_getValueMap() {
        final Resource imageResource = ctx.resourceResolver().getResource("/content/image");

        final ValueMap actual = new ImageListImpl.SimpleImageComponentResource(imageResource, "Test alt text").getValueMap();

        assertEquals(5, actual.values().size());
        assertEquals("/content/dam/test.png", actual.get("fileReference"));
        assertEquals("Test alt text", actual.get("alt"));
        assertEquals(false, actual.get(Image.PN_IS_DECORATIVE));
        assertEquals(false, actual.get(Image.PN_DISPLAY_POPUP_TITLE));
        assertEquals(false, actual.get(Image.PN_TITLE_VALUE_FROM_DAM));
    }

    @Test
    void SimpleImageComponentResource_adaptToValueMap() {
        final Resource imageResource = ctx.resourceResolver().getResource("/content/image");

        final ValueMap actual = new ImageListImpl.SimpleImageComponentResource(imageResource, "Test alt text").adaptTo(ValueMap.class);

        assertEquals(5, actual.values().size());
        assertEquals("/content/dam/test.png", actual.get("fileReference"));
        assertEquals("Test alt text", actual.get("alt"));
        assertEquals(false, actual.get(Image.PN_IS_DECORATIVE));
        assertEquals(false, actual.get(Image.PN_DISPLAY_POPUP_TITLE));
        assertEquals(false, actual.get(Image.PN_TITLE_VALUE_FROM_DAM));
    }


    @Test
    void SimpleImageComponentResource_adaptToSomethingElse() throws RepositoryException {
        final Resource imageResource = ctx.resourceResolver().getResource("/content/image");

        final Node actual = new ImageListImpl.SimpleImageComponentResource(imageResource, "Test alt text").adaptTo(Node.class);

        assertNotNull(actual);
        assertEquals("/content/image", actual.getPath());
    }

    @Test
    void getData() throws RepositoryException, IllegalAccessException, NoSuchFieldException {
    	
   	 	// Page 1
        Resource page1ImageComponentResource = spy(ctx.resourceResolver().getResource("/content/pages/page-1/jcr:content/root/responsivegrid/page-1-image-component"));
        doReturn(leakedResourceResolver).when(page1ImageComponentResource).getResourceResolver();

        doReturn(page1MockQuery).when(mockQueryBuilder).createQuery(argThat(new PredicateGroupByPath("/content/pages/page-1/jcr:content")), any(Session.class));
        doReturn(page1MockSearchResult).when(page1MockQuery).getResult();
        doReturn(Arrays.asList(new Resource[]{page1ImageComponentResource}).iterator()).when(page1MockSearchResult).getResources();

        //Enable the data layer
        enableDataLayer(ctx, true);
        ctx.currentResource("/content/image-list");

        final ImageList actual = ctx.request().adaptTo(ImageList.class);
        
        setCoreList((ImageListImpl) actual, new MockList("/content/pages/page-1"));

        ImageList.ListItem[] actualListItems = actual.getListItems().toArray(new ImageList.ListItem[0]);
        
        //Test Data Layer on Image List (parent)
        assertNotNull(actual.getData());
        assertEquals("wknd/components/image-list", actual.getData().getType());
        assertEquals("image-list-2719473ca4", actual.getData().getId());
        
        //Test Data Layer on Image List Items
        ComponentData listItemData = actualListItems[0].getData();
        
        assertNotNull(listItemData);
        assertEquals("wknd/components/image-list/image-list-item", listItemData.getType());
        assertEquals("image-list-2719473ca4-image-list-item-58adf87daa", actualListItems[0].getData().getId());
        assertEquals("Page 1", listItemData.getTitle());
        assertEquals("Page 1 Description", listItemData.getDescription());
        assertEquals("/content/pages/page-1.html", listItemData.getLinkUrl());
        assertEquals("image-list-2719473ca4", listItemData.getParentId());
    }
    
    @Test
    void getData_disabled() throws RepositoryException, IllegalAccessException, NoSuchFieldException {
    	
    	// Page 1
        Resource page1ImageComponentResource = spy(ctx.resourceResolver().getResource("/content/pages/page-1/jcr:content/root/responsivegrid/page-1-image-component"));
        doReturn(leakedResourceResolver).when(page1ImageComponentResource).getResourceResolver();

        doReturn(page1MockQuery).when(mockQueryBuilder).createQuery(argThat(new PredicateGroupByPath("/content/pages/page-1/jcr:content")), any(Session.class));
        doReturn(page1MockSearchResult).when(page1MockQuery).getResult();
        doReturn(Arrays.asList(new Resource[]{page1ImageComponentResource}).iterator()).when(page1MockSearchResult).getResources();

        //Disable the data layer
        enableDataLayer(ctx, false);
        
        ctx.currentResource("/content/image-list");

        final ImageList actual = ctx.request().adaptTo(ImageList.class);
        assertNull(actual.getData());
        
        setCoreList((ImageListImpl) actual, new MockList("/content/pages/page-1"));

        ImageList.ListItem[] actualListItems = actual.getListItems().toArray(new ImageList.ListItem[2]);
        
        //Test Data Layer on Image List Items
        assertNull(actualListItems[0].getData());
        
        
    }

    /**
     * Mock utility function to enable or disable the Data Layer
     * @param ctx
     * @param enabled
     */
    void enableDataLayer(AemContext ctx, boolean enabled) {
        ConfigurationBuilder builder = mock(ConfigurationBuilder.class);
        DataLayerConfig dataLayerConfig = mock(DataLayerConfig.class);
        when(dataLayerConfig.enabled()).thenReturn(enabled);
        when(builder.as(DataLayerConfig.class)).thenReturn(dataLayerConfig);
        ctx.registerAdapter(Resource.class, ConfigurationBuilder.class, builder);
    }

    /**
     * Mockito Matchers
     */

    class PredicateGroupByPath implements ArgumentMatcher<PredicateGroup> {
        private final String path;

        public PredicateGroupByPath(String path) {
            this.path = path;
        }

        public boolean matches(PredicateGroup predicateGroup) {
            return StringUtils.equals(path, predicateGroup.getByName("path").getParameters().get("path"));
        }

        public String toString() {
            //printed in verification errors
            return "[PredicateGroup matching path " + path + " ]";
        }
    }


    /**
     * Mock test objects required for the unit tests.
     * <p>
     * These mock objects represent implementations of AEM WCM Core Component dependencies the Image List depends on.
     **/

    void setCoreList(ImageListImpl imageListImpl, List mockList) throws IllegalAccessException, NoSuchFieldException {
        Field reader = ImageListImpl.class.getDeclaredField("coreList");
        reader.setAccessible(true);
        reader.set(imageListImpl, mockList);
    }

    public class MockList implements List {
        private Collection<ListItem> listItems = null;

        public MockList(String... pagePaths) {
            this.listItems = Arrays.asList(pagePaths).stream()
                    .map(pagePath -> ctx.resourceResolver().getResource(pagePath))
                    .map(resource -> new MockListItem(resource))
                    .collect(Collectors.toList());
        }

        public Collection<ListItem> getListItems() {
            return listItems;
        }
    }

    public class MockListItem implements ListItem {
        Resource resource;

        public MockListItem(Resource resource) {
            this.resource = resource;
        }

        public String getURL() {
            return resource.getPath() + ".html";
        }

        public String getTitle() {
            return resource.getValueMap().get("jcr:content/jcr:title", String.class);
        }

        public String getDescription() {
            return resource.getValueMap().get("jcr:content/jcr:description", String.class);
        }

        public String getPath() {
            return resource.getPath();
        }
    }
}