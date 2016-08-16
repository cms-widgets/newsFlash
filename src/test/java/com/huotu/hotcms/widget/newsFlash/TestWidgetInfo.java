/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.newsFlash;

import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.widget.test.WidgetTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author CJ
 */
public class TestWidgetInfo extends WidgetTest {

    @Override
    protected boolean printPageSource() {
        return true;
    }

    @Override
    protected void editorWork(Widget widget, WebElement editor, Supplier<Map<String, Object>> currentWidgetProperties) {
        Map<String,Object> properties = currentWidgetProperties.get();
        List<Map<String, Object>> newsList = (List<Map<String, Object>>) properties.get(WidgetInfo.NEWS_FLASH_LIST);
        assertThat(newsList.size()).isEqualTo(WidgetInfo.NEWS_FLASH_LIST_SIZE);
    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger) throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        WebElement webElement = uiChanger.apply(properties);
        List<WebElement> browseNewsLs = webElement.findElements(By.cssSelector(".news-link"));
        assertThat(browseNewsLs.size()).isEqualTo(WidgetInfo.NEWS_FLASH_LIST_SIZE);

    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> function, Supplier<Map<String, Object>> supplier) throws IOException {
        WebElement webElement = function.apply(widget.defaultProperties(resourceService));
        List<WebElement> news = webElement.findElements(By.tagName("tr"));
        assertThat(news.size()).isEqualTo(WidgetInfo.NEWS_FLASH_LIST_SIZE);

        List<WebElement> checkboxs = webElement.findElements(By.className("news-check"));
        for(int i=0;i<checkboxs.size();i++){
            if(i%2==0){
                checkboxs.get(i).click();
            }
        }

        for(int i=0;i<checkboxs.size();i++){
            if(i%2==0){
                assertThat(checkboxs.get(i).getAttribute("checked")).isEqualTo("true");
            }
        }
    }


}
