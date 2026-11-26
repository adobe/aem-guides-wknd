package com.adobe.aem.guides.wknd.core.sightly;

import com.adobe.aem.guides.wknd.core.utils.UniqueIdGenerator;
import com.adobe.aem.guides.wknd.core.utils.Constants;
import com.adobe.cq.sightly.WCMUsePojo;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import java.security.NoSuchAlgorithmException;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static com.adobe.aem.guides.wknd.core.utils.Constants.HYPHEN;
import static com.adobe.aem.guides.wknd.core.utils.Constants.REGEX_ALLCASE_ALPHA;

public class UniquePathIdGenerator extends WCMUsePojo {

  @Getter 
  private String formattedNodePath = EMPTY;

  @Override
  public void activate() throws Exception {
    Resource resource = getResource();
    String currentNodePath = get(Constants.CURRENT_NODE_PATH, String.class);
    String itemName = get(Constants.ITEM_NAME, String.class);
    String itemText = get(Constants.ITEM_TEXT, String.class);

    if (StringUtils.isNotBlank(currentNodePath)) {
      itemName = StringUtils.isBlank(itemName) ? EMPTY : itemName;
      itemText = StringUtils.isBlank(itemText) ? EMPTY : itemText;
      formattedNodePath = getFormattedNodePath(currentNodePath.concat(itemName).concat(itemText), resource);
    }
  }

  public static String getFormattedNodePath(String unformattedPathString, Resource resource)
      throws NoSuchAlgorithmException {
    String currentNodeName = resource.getPath().hashCode() + resource.getName();
    String head = unformattedPathString.concat(currentNodeName).replaceAll(REGEX_ALLCASE_ALPHA, EMPTY);
    String tail = currentNodeName.concat(unformattedPathString).replaceAll(REGEX_ALLCASE_ALPHA, EMPTY);
    return UniqueIdGenerator.generateUniqueId(head) + HYPHEN + UniqueIdGenerator.generateUniqueId(tail);
  }
}
