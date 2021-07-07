package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.adobe.aem.guides.wknd.core.models.Card;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.xfa.ModelFactory;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.eclipse.jetty.util.StringUtil;

@Model(
    adaptables = {SlingHttpServletRequest.class},
    adapters =  {Card.class},
    resourceType = {CardImpl.RESOURCE_TYPE},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL

)
public class CardImpl implements Card {
    protected static final String RESOURCE_TYPE = "wknd/components/card";
    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @Self
    private Image image;

    @OSGiService
    ModelFactory modelFactory;


    @Override
    public String getTitle() {
        
        return title;
    }


    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return description;
    }

    @Override
    public boolean isEmpty() {
        if(StringUtil.isBlank(title)){
            return true;
        }
        if (StringUtil.isBlank(description)){
            return true;
        }
        return false;
    }
    
}
