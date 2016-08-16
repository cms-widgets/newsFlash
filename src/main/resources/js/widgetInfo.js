/**
 * Created by lhx on 2016/8/11.
 */
CMSWidgets.initWidget({
// 编辑器相关
    editor: {
        properties: null,
        saveComponent: function (onSuccess, onFailed) {
            this.properties.newsList = [];
            $(".news-check").each(function(i){
                if($(this).is(":checked")){
                    var news = {};
                    var title = $(".news-title").eq(i).text();
                    var publishDate = $(".news-publishDate").eq(i).text();
                    news.title = title;
                    news.publishDate = publishDate;
                    this.properties.newsList.push(news);
                }
            });

            onSuccess(this.properties);
            return this.properties;
        },
        open: function (globalId) {
            this.properties = widgetProperties(globalId);
            this.properties.newsFlashList = [];
            for(var i=0;i<10;i++){
                var news = {};
                news.title = "title22";
                news.publishDate = new Date();
                this.properties.newsFlashList.push(news);
            }
        },
        close: function (globalId) {

        }
    }
});
