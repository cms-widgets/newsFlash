/**
 * Created by lhx on 2016/8/11.
 */
CMSWidgets.initWidget({
// 编辑器相关
    editor: {
        saveComponent: function (onFailed) {
            this.properties.count = $(".count").val();
            console.error(this.properties);
            if (this.properties.serial == null || this.properties.serial == '' || this.properties.count == ''
                || this.properties.count == '0') {
                onFailed("数据源serial不能为空，且展示条数不能为空");
                return;
            }
        }
    }
});
