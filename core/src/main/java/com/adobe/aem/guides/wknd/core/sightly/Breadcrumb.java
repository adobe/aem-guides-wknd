package com.adobe.aem.guides.wknd.core.sightly;

import java.util.ArrayList;
import java.util.List;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.wcm.api.Page;

import lombok.Getter;

/**
 * This generate breadcrumb for the page
 */
public class Breadcrumb extends WCMUsePojo {

  @Getter 
  private List<Page> breadcrumbList = new ArrayList<Page>();

  @Override
  public void activate() throws Exception {
    breadcrumbList = generateBreadcrumb();
  }

  private List<Page> generateBreadcrumb() {
    List<Page> breadcrumbLinks = new ArrayList<Page>();

    long level = getCurrentStyle().get("absParent", 2L);
    long endLevel = getCurrentStyle().get("relParent", 1L);
    int currentLevel = getCurrentPage().getDepth();
    while (level <= currentLevel - endLevel) {
      Page trail = getCurrentPage().getAbsoluteParent((int) level);
      if (null != trail) {
        breadcrumbLinks.add(trail);
      }
      level++;
    }
    return breadcrumbLinks;
  }
}
