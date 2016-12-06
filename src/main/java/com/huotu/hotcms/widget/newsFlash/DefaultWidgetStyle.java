/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.newsFlash;

import com.huotu.hotcms.widget.WidgetStyle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Locale;

/**
 * @author CJ
 */
public class DefaultWidgetStyle implements WidgetStyle{

    @Override
    public String id() {
        return "newsFlashDefaultStyle";
    }

    @Override
    public String name(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "默认样式";
        }
        return "default style";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "简单的文章列表样式，展示文章数据源对应的文章内容列表，单击文章跳转至*页面类型为对应数据源的内容页面*";
        }
        return "Simple article list style, showing the article data source corresponding to the article content list," +
                " click the article to jump to the page type for the corresponding data source page";
    }

    @Override
    public Resource thumbnail() {
        return new ClassPathResource("/thumbnail/defaultStyleThumbnail.png", getClass().getClassLoader());
    }

    @Override
    public Resource previewTemplate() {
        return null;
    }

    @Override
    public Resource browseTemplate() {
        return new ClassPathResource("/template/defaultStyleBrowseTemplate.html", getClass().getClassLoader());
    }

}
