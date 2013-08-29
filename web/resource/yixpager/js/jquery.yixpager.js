/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-7-31
 * Time: 下午10:26
 * To change this template use File | Settings | File Templates.
 */
(function ($) {
    $.fn.yixpager = function (options) {
        options = $.extend({}, $.fn.yixpager.defaults, options);
        return this.append(getPageItems(options));
    };

    function gotoPageCallback(page, callback) {
        return function () {
            callback(page);
        }
    }

    function getPageItems(options) {
        var box = $("<div></div>");
        var pageInfo = options.pageInfo;
        $("<span class='pager_item'></span>").html("首页").click(gotoPageCallback(1, options.callback)).appendTo(box);
        var prevPageBtn = $("<span class='pager_item'></span>").html("上一页").appendTo(box);
        if (pageInfo.page > 1) {
            var prevPage = pageInfo.page - 1;
            prevPageBtn.click(gotoPageCallback(prevPage, options.callback));
        } else {
            prevPageBtn.addClass("unable");
        }
        var pageText = "当前第" + pageInfo.page + "页，共" + pageInfo.totalpage +
            "页 每页" + pageInfo.pagesize + "条，共" + pageInfo.totalcount + "条";
        $("<span class='pager_item unable'></span>").text(pageText).appendTo(box);
        var nextPageBtn = $("<span class='pager_item'></span>").html("下一页").appendTo(box);
        if(pageInfo.page<pageInfo.totalpage){
            var nextPage = pageInfo.page+1;
            nextPageBtn.click(gotoPageCallback(nextPage,options.callback));
        }else{
            nextPageBtn.addClass("unable");
        }
        $("<span class='pager_item'></span>").html("末页").click(gotoPageCallback(pageInfo.totalpage,options.callback)).appendTo(box);
        return box.children();
    }

    $.fn.yixpager.defaults = {
        pageInfo: {page: 1,
            totalpage: 0,
            pagesize: 15,
            totalcount: 0},
        callback: function () {
        }
    }
})(jQuery);