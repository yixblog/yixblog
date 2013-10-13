/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-29
 * Time: 上午10:35
 */
$(document).ready(function () {
    $(".hidden_btn").click(function () {
        $(".hide").stop(true, false).slideToggle();
    });

    function doSearch(page) {
        var data = {tag: $("#tagname").html(), page: page};
        $.ajax({
            url: "a/article/query.action",
            data: data,
            type: "post",
            dataType: "json",
            success: function (data) {
                buildArticleList(data);
            }
        });
    }


    function buildArticleList(data) {
        var articleUL = $(".list_box").empty();
        var pagerBox = $(".pager_box");
        for (var i = 0, len = data.articles.length; i < len; i++) {
            var article = data.articles[i];
            var li = buildArticleLi(article);
            li.appendTo(articleUL);
        }
        pagerBox.empty().yixpager({
            pageInfo: data,
            callback: doSearch
        });
    }

    doSearch(1);

    function buildArticleLi(article) {
        var li = $("<li></li>");
        $("<span></span>", {class: "title"}).append($("<a></a>", {target: "_blank", href: "a/article/view/" + article.id + ".htm", html: article.title})).appendTo(li);
        $("<span></span>", {class: "response_count", html: article.replycount}).appendTo(li);
        $("<span></span>", {class: "author"}).append($("<a></a>"), {href: "a/userinfo/" + article.author.id + ".htm", target: "_blank", html: article.author.nick}).appendTo(li);
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
            $("<a></a>", {href: "a/article/tags/" + tags[i] + ".htm", html: tags[i], target: "_blank"}).appendTo(parent);
        }
        return parent.children();
    }

    $("#search_btn").button().click(function () {
        doSearch(1);
    });
});