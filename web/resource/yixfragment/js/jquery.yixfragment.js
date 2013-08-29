/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-29
 * Time: 下午5:54
 * To change this template use File | Settings | File Templates.
 */
(function ($) {

    var tab_scroll;
    var tab_box;
    var content_list;
    var content_scroll;
    $.yix = {
        fragment: function (options) {
            var tab_list = options.tabs;
            var tab_width;
            var tab_total_width=0;
            tab_list.each(function () {
                tab_width = $(this).width();
                tab_total_width += $(this).outerWidth(true);
            });
            var activeClass = options.activeClass;

            content_list = options.contents;
            tab_scroll = tab_list.wrapAll($("<div style='white-space: nowrap;'></div>").width(tab_total_width)).parent();
            tab_box = tab_scroll.parent();
            var tab_box_width = tab_box.width();
            content_scroll = content_list.wrapAll("<div style='white-space: nowrap;width:200%'></div>").parent();
            content_scroll.html(getOuterHtml(content_list[0]));
            tab_list.click(function () {
                    var tab = $(this);
                    var index = tab.prevAll().length;
                    var left = -(tab_box_width / 2 - tab_width / 2) + index * tab_width;
                    tab_list.removeClass(activeClass);
                    tab_box.animate({scrollLeft: left}, "slow");
                    tab_box.queue(function () {
                        tab.addClass(activeClass);
                        $(this).dequeue();
                    });
                    scrollToIndex(index);
                }
            );
            $(tab_list[0]).click();
        }
    };

    var scrollToIndex = function (index) {
        var content = $(getOuterHtml(content_list[index]));
        var content_box = content_scroll.parent();
        content_scroll.append(content);
        content.children().each(function (count) {
            $(this).css("left", count * 50);
        });
        var left = content_box.width();
        content_box.animate({scrollLeft: left}, 800);
        content.children().each(function (count) {
            $(this).animate({left: 0}, 800 + 5 * count);
        });
        content_box.queue(function () {
            content.prevAll().remove();
            content_box.scrollLeft(0);
            $(this).dequeue();
        });
    };
    var getOuterHtml = function (item) {
        var parentDiv = $("<div></div>").append(item);
        return parentDiv.html();
    }
})($);