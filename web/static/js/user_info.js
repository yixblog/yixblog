/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-10-8
 * Time: 下午10:04
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {
    var userId = $("#user_id_hidden").val();

    function gotoPage(page) {
        $.ajax({
            url: "article/query.action",
            type: "post",
            data: {userId: userId, page: page},
            dataType: "json",
            success: function (data) {
                $(".article_box").empty();
                if (data.success) {
                    buildArticleList(data);
                }
            }
        });
    }

    function buildArticleList(data) {
        var box = $(".article_box");
        var ul = $("<ul></ul>");
        for (var i = 0, len = data.articles.length; i < len; i++) {
            var li = buildArticleRow(data.articles[i]);
            li.appendTo(ul);
        }
        ul.appendTo(box);
        $("<span></span>", {class: "pager_box"}).yixpager({
            pageInfo: data,
            callback: gotoPage
        }).appendTo(box);
    }

    function buildArticleRow(article) {
        var li = $("<li></li>");
        $("<span></span>", {class: "title"}).append($("<a></a>", {target: "_blank", href: "article/view/" + article.id + ".htm", html: article.title})).appendTo(li);
        $("<span></span>", {class: "response_count", html: article.replycount}).appendTo(li);
        $("<span></span>", {class: "tag_list"}).append(buildTagList(article.tags)).appendTo(li);
        $("<span></span>", {class: "time", html: article.addtimestring}).appendTo(li);
        return li;
    }

    function buildTagList(tags) {
        var parent = $("<div></div>");
        if (tags == null) {
            return parent.children();
        }
        for (var i = 0; i < tags.length; i++) {
            $("<a></a>", {href: "article/tags/" + tags[i] + ".htm", html: tags[i], target: "_blank"}).appendTo(parent);
        }
        return parent.children();
    }

    gotoPage(1);
});