/**
 * Created by lhx on 2016/8/11.
 */
CMSWidgets.initWidget({
// 编辑器相关
    editor: {
        properties: null,
        saveComponent: function (onSuccess, onFailed) {
            var me = this;
            me.properties.newsFlashList = [];
            $(".news-check").each(function (i) {
                if ($(this).is(":checked")) {
                    var news = {};
                    var title = $(".news-title").eq(i).text();
                    var createTime = $(".news-publishDate").eq(i).text();
                    news.title = title;
                    news.createTime = createTime;
                    me.properties.newsFlashList.push(news);
                }
            });
            onSuccess(me.properties);
            return me.properties;
        },
        selectChange: function () {
            $(".dataSource").on("change", function () {
                var serial = $("option:selected").val();
                $(".news-link").remove();
                getDataSource("findArticleContent", serial, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        var title = data[i].title;
                        var createTime = data[i].createTime;
                        var $tr = $($(".item").html()).find("tr");
                        $tr.find(".news-title").text(title);
                        $tr.find(".news-publishDate").text(createTime);
                        $(".dataList").append($tr);
                    }
                }, function () {
                    console.error("getDataSource error")
                });
            });
        },
        open: function (globalId) {
            this.properties = widgetProperties(globalId);
            /*<![CDATA[*/
            var newsTypeList = /*[[${@cmsDataSourceService.findArticleCategory()}]]*/ '[]';
            var optionHtml = "";
            for (var i = 0; i < newsTypeList.length; i++) {
                optionHtml += "<option value='" + newsTypeList[i].serial + "'>" + newsTypeList[i].name + "</option>";
            }
            $(".dataSource").append(optionHtml);
            /*]]>*/
            this.selectChange();
        },
        close: function (globalId) {

        }

    }
});
