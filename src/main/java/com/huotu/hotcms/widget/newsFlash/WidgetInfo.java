/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.newsFlash;

import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.PreProcessWidget;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.repository.PageInfoRepository;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * @author CJ
 */
public class WidgetInfo implements Widget, PreProcessWidget {

    public static final String SERIAL = "serial";

    public static final String COUNT = "count";

    public static final int NEWS_FLASH_LIST_SIZE = 10;

    @Autowired
    CMSDataSourceService cmsDataSourceService;

    @Override
    public String groupId() {
        return "com.huotu.hotcms.widget.newsFlash";
    }

    @Override
    public String widgetId() {
        return "newsFlash";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "A custom Widget";
        }
        return "newsFlash";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINESE)) {
            return "这是一个 A custom Widget，你可以对组件进行自定义修改。";
        }
        return "This is a newsFlash,  you can make custom change the component.";
    }

    @Override
    public String dependVersion() {
        return "1.0-SNAPSHOT";
    }

    @Override
    public WidgetStyle[] styles() {
        return new WidgetStyle[]{new DefaultWidgetStyle()};
    }

    @Override
    public Resource widgetDependencyContent(MediaType mediaType) {
        if (mediaType.equals(Widget.Javascript))
            return new ClassPathResource("js/widgetInfo.js", getClass().getClassLoader());
        return null;
    }

    @Override
    public Map<String, Resource> publicResources() {
        Map<String, Resource> map = new HashMap<>();
        map.put("thumbnail/defaultStyleThumbnail.png", new ClassPathResource("thumbnail/defaultStyleThumbnail.png", getClass().getClassLoader()));
        return map;
    }

    @Override
    public void valid(String styleId, ComponentProperties componentProperties) throws IllegalArgumentException {
        WidgetStyle style = WidgetStyle.styleByID(this, styleId);
        //加入控件独有的属性验证

    }

    @Override
    public Class springConfigClass() {
        return null;
    }


    @Override
    public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException {
        ComponentProperties properties = new ComponentProperties();
        properties.put(COUNT, NEWS_FLASH_LIST_SIZE);
        properties.put(SERIAL, "0");
        return properties;
    }

    @Override
    public void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables, Map<String, String> parameters) {
        PageInfoRepository pageInfoRepository = CMSContext.RequestContext().getWebApplicationContext().getBean(PageInfoRepository.class);
        if(properties.containsKey("pageSerial")){
            String serial = properties.get("pageSerial").toString();
            PageInfo pageInfo = pageInfoRepository.findBySerial(serial);
            if (pageInfo != null) {
                variables.put("pagePath",pageInfo.getPagePath());
            }
        }
    }


}
