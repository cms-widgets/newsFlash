/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huotu.hotcms.widget.newsFlash;

import com.huotu.hotcms.service.common.ContentType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.widget.CMSContext;
import com.huotu.hotcms.widget.ComponentProperties;
import com.huotu.hotcms.widget.Widget;
import com.huotu.hotcms.widget.WidgetStyle;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.widget.test.Editor;
import com.huotu.widget.test.WidgetTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.data.domain.Page;

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
    protected void editorWork(Widget widget, Editor editor, Supplier<Map<String, Object>> currentWidgetProperties)
            throws IOException {
        WebElement count = editor.getWebElement().findElement(By.name("count"));
        count.clear();
        Actions actions = new Actions(driver);
        actions.sendKeys(count,"20").build().perform();
        Category category = new Category();
        category.setSerial("123444");
        category.setContentType(ContentType.Article);
        editor.chooseCategory(WidgetInfo.SERIAL, category);
        Map map = currentWidgetProperties.get();
        assertThat(map.get(WidgetInfo.COUNT)).isEqualTo("20");
        assertThat(map.get(WidgetInfo.SERIAL)).isEqualTo("123444");
    }

    @Override
    protected void browseWork(Widget widget, WidgetStyle style, Function<ComponentProperties, WebElement> uiChanger)
            throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        WebElement webElement = uiChanger.apply(properties);
        List<WebElement> lis = webElement.findElements(By.tagName("li"));
        CMSDataSourceService cmsDataSourceService = CMSContext.RequestContext().getWebApplicationContext()
                .getBean(CMSDataSourceService.class);
        Page<Article> articleContent = cmsDataSourceService.findArticleContent(properties.get(WidgetInfo.SERIAL).toString()
                ,1, Integer.valueOf(properties.get(WidgetInfo.COUNT).toString()));
        if (articleContent==null)
            assertThat(lis).isNull();
        else
            assertThat(articleContent.getContent().size()).isEqualTo(lis.size());
    }

    @Override
    protected void editorBrowseWork(Widget widget, Function<ComponentProperties, WebElement> function
            , Supplier<Map<String, Object>> supplier) throws IOException {
        ComponentProperties properties = widget.defaultProperties(resourceService);
        WebElement webElement = function.apply(properties);
        WebElement count = webElement.findElement(By.name(WidgetInfo.COUNT));
        assertThat(count.getAttribute("value")).isEqualTo(properties.get(WidgetInfo.COUNT).toString());
    }

}
