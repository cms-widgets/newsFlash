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
import com.huotu.hotcms.service.common.PageType;
import com.huotu.hotcms.service.entity.Article;
import com.huotu.hotcms.service.entity.Category;
import com.huotu.hotcms.service.repository.ArticleRepository;
import com.huotu.hotcms.service.repository.CategoryRepository;
import com.huotu.hotcms.service.service.CategoryService;
import com.huotu.hotcms.widget.*;
import com.huotu.hotcms.widget.entity.PageInfo;
import com.huotu.hotcms.widget.service.CMSDataSourceService;
import com.huotu.hotcms.widget.service.PageService;
import me.jiangcai.lib.resource.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author CJ
 */
public class WidgetInfo implements Widget, PreProcessWidget {
    public static final String ContentSerial = "ContentSerial";

    public static final String SERIAL = "serial";

    public static final String COUNT = "count";
    public static final String DATA_PAGE = "dataPage";

    public static final int NEWS_FLASH_LIST_SIZE = 10;
    private static final String CATEGORY = "category";
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
            return "快讯";
        }
        return "newsFlash";
    }

    @Override
    public String description(Locale locale) {
        if (locale.equals(Locale.CHINA)) {
            return "这是一个文章内容列表组件，选择文章数据源进行展示文章内容列表";
        }
        return "This is a list of the contents of the article components," +
                " select the article data source to display the contents of the article list";
    }

    @Override
    public String dependVersion() {
        return "1.1.0";
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
    public ComponentProperties defaultProperties(ResourceService resourceService) throws IOException, IllegalStateException {
        ComponentProperties properties = new ComponentProperties();
        // 随意找一个数据源,如果没有。那就没有。。
        CategoryRepository categoryRepository = CMSContext.RequestContext().getWebApplicationContext()
                .getBean(CategoryRepository.class);
        CategoryService categoryService = CMSContext.RequestContext().getWebApplicationContext()
                .getBean(CategoryService.class);
        List<Category> categories = categoryRepository
                .findBySiteAndContentTypeAndDeletedFalse(CMSContext.RequestContext().getSite(), ContentType.Article);
        if (categories.isEmpty()) {
            ArticleRepository articleRepository = CMSContext.RequestContext().getWebApplicationContext()
                    .getBean(ArticleRepository.class);
            Category category = new Category();
            category.setContentType(ContentType.Article);
            category.setName("资讯数据源");
            categoryService.init(category);
            category.setSite(CMSContext.RequestContext().getSite());
            categoryRepository.save(category);
            properties.put(SERIAL, category.getSerial());
            Article article = new Article();
            article.setDeleted(false);
            article.setTitle("文章标题");
            article.setCategory(category);
            article.setContent("文章内容");
            article.setCreateTime(LocalDateTime.now());
            article.setSerial(UUID.randomUUID().toString());
            articleRepository.save(article);
        } else {
            properties.put(SERIAL, categories.get(0).getSerial());
        }
        properties.put(COUNT, NEWS_FLASH_LIST_SIZE);
        return properties;
    }

    @Override
    public void prepareContext(WidgetStyle style, ComponentProperties properties, Map<String, Object> variables
            , Map<String, String> parameters) {

        String serial = (String) properties.get(SERIAL);
        CMSDataSourceService cmsDataSourceService = CMSContext.RequestContext().getWebApplicationContext()
                .getBean(CMSDataSourceService.class);
        CategoryRepository categoryRepository = getCMSServiceFromCMSContext(CategoryRepository.class);
        Category category = categoryRepository.findBySerialAndSite(serial, CMSContext.RequestContext().getSite());
        Page<Article> page = cmsDataSourceService.findArticleContent(serial, 1, NEWS_FLASH_LIST_SIZE);
        variables.put(DATA_PAGE, page);
        variables.put(CATEGORY, category);
        String contentSerial = (String) properties.get(ContentSerial);
        if (page != null && !page.getContent().isEmpty()) {
            if (contentSerial != null) {
                PageInfo contentPage = cmsDataSourceService.findPageInfoContent(contentSerial);
                variables.put("contentURI", contentPage.getPagePath());
            } else {
                try {
                    PageInfo contentPage = CMSContext.RequestContext().getWebApplicationContext().getBean(PageService.class)
                            .getClosestContentPage(category, null, PageType.DataContent);
                    variables.put("contentURI", contentPage.getPagePath());
                } catch (Exception e) {
                    variables.put("contentURI", variables.get("uri"));
                }
            }
        }
    }

    @Override
    public PageType supportedPageType() {
        return PageType.DataIndex;
    }
}
