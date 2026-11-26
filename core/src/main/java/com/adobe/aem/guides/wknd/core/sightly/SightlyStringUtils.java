package com.adobe.aem.guides.wknd.core.sightly;

import org.apache.commons.lang3.StringUtils;

public class SightlyStringUtils {

    private SightlyStringUtils() {

    }

  public static String removeHtmlAttributes(String str) {
    if (StringUtils.isNotBlank(str)) {
      str = str.replaceAll("\\<[^>]*>", " ").trim();
    }
    return str;
  }
}
