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

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.aem.guides.wknd.core.models.Byline;
import com.adobe.cq.wcm.core.components.models.Image;
import com.google.common.collect.ImmutableList;

import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class BylineImplTest {
	
	private final AemContext ctx = new AemContext();
	
	@Mock
	private Image image;

	@Mock
	private ModelFactory modelFactory;


	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(BylineImpl.class);
	    ctx.load().json("/com/adobe/aem/guides/wknd/core/models/impl/BylineImplTest.json", "/content");
	    
	    lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()),
	    	       any(Resource.class),
	    	       eq(Image.class))).thenReturn(image);

	    ctx.registerService(ModelFactory.class, modelFactory,
	    	       org.osgi.framework.Constants.SERVICE_RANKING, Integer.MAX_VALUE);
	}

	@Test
	public void testGetName() {
       final String expected = "Jane Doe";

       ctx.currentResource("/content/byline");
       Byline byline = ctx.request().adaptTo(Byline.class);

       String actual = byline.getName();

       assertEquals(expected, actual);
	}

	@Test
	   public void testGetOccupations() {
	       List<String> expected = new ImmutableList.Builder<String>()
	                               .add("Blogger")
	                               .add("Photographer")
	                               .add("YouTuber")
	                               .build();

	       ctx.currentResource("/content/byline");
	       Byline byline = ctx.request().adaptTo(Byline.class);

	       List<String> actual = byline.getOccupations();

	       assertEquals(expected, actual);
	   }

	@Test
	   public void testIsEmpty() {
	       ctx.currentResource("/content/empty");

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   }

	   @Test
	   public void testIsEmpty_WithoutName() {
	       ctx.currentResource("/content/without-name");

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   }

	   @Test
	   public void testIsEmpty_WithoutOccupations() {
	       ctx.currentResource("/content/without-occupations");

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   }

	   @Test
	   public void testIsEmpty_WithoutImage() {
	       ctx.currentResource("/content/byline");
	       
	       //ModelFactory modelFactory = mock(ModelFactory.class, withSettings().lenient());
	       
	       lenient().when(modelFactory.getModelFromWrappedRequest(eq(ctx.request()),
	           any(Resource.class),
	           eq(Image.class))).thenReturn(null);

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   }
	   
	   @Test
	   public void testIsEmpty_WithoutImageSrc() {
	       ctx.currentResource("/content/byline");

	       when(image.getSrc()).thenReturn("");

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   }
	   
	   @Test
	   public void testIsNotEmpty() {
	    ctx.currentResource("/content/byline");
	    when(image.getSrc()).thenReturn("/content/bio.png");

	    Byline byline = ctx.request().adaptTo(Byline.class);

	    assertFalse(byline.isEmpty());
	   }
	   
	   @Test
	   public void testGetOccupations_WithoutOccupations() {
	       List<String> expected = Collections.emptyList();

	       ctx.currentResource("/content/empty");
	       Byline byline = ctx.request().adaptTo(Byline.class);

	       List<String> actual = byline.getOccupations();

	       assertEquals(expected, actual);
	   }
	   
	   @Test
	   public void testIsEmpty_WithEmptyArrayOfOccupations() {
	       ctx.currentResource("/content/without-occupations-empty-array");

	       Byline byline = ctx.request().adaptTo(Byline.class);

	       assertTrue(byline.isEmpty());
	   } 

}
