package com.adobe.aem.guides.wknd.core.sightly;

import com.adobe.aem.guides.wknd.core.utils.Constants;
import com.adobe.cq.sightly.WCMUsePojo;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import lombok.Getter;
import lombok.Setter;

/**
 * get the value from Node.
 */
public class ResourceValueExists extends WCMUsePojo {

  @Getter @Setter 
  private String value;

  @Getter @Setter 
  private String textValue;

  @Getter @Setter 
  private String propertyName;

  @Getter @Setter 
  private boolean exists;

  @Override
  public void activate() throws Exception {
    ValueMap valueMap;
    String nodePathParameter = get(Constants.NODE, String.class);

    if (nodePathParameter != null) {
      String[] nodePaths = nodePathParameter.split(Constants.COMMA);

      for (String nodePath : nodePaths) {
        Resource resource;

        if (StringUtils.isNotBlank(nodePath)) {
          // Check if it's an absolute path
          if (nodePath.startsWith(Constants.FORWARD_SLASH)) {
            resource = getResource().getResourceResolver().getResource(nodePath);
          } else {
            resource = getResource().getChild(nodePath);
          }

          // Check the value in the resource
          if (null != resource) {
            valueMap = resource.adaptTo(ValueMap.class);

            // get property name or use default value of "text"
            propertyName = get(Constants.PROPERTY_NAME, String.class);
            propertyName = StringUtils.isEmpty(propertyName) ? Constants.TEXT : propertyName;

            if (valueMap.containsKey(propertyName)) {
              value = valueMap.get(propertyName, StringUtils.EMPTY);
              exists = true;
            } else {
              // If value not found, Check the value in the current resource
              value = getProperties().get(propertyName, StringUtils.EMPTY);
            }

            if (StringUtils.isNotEmpty(value)) {
              textValue = SightlyStringUtils.removeHtmlAttributes(value);
            }
          }
        }

        // Break if the value is found anywhere.
        if (StringUtils.isNotEmpty(value)) {
          break;
        }
      }
    }
  }
}
