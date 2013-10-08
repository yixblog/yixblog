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

    $("#date_start_input,#date_end_input").datepicker({
        dateFormat: "yy-mm-dd",
        changeMonth: true,
        changeYear: true
    });

    function doSearch(page) {
        var keywords = $("#search_input").val().split(" ");
        var startDate = $("#date_start_input").val();
        var endDate = $("#date_end_input").val();
        var sortKey = $("#sort_key_select").val();
        var data;
        if ($(".hide").is(":hidden")) {
            data = {keywords: keywords, page: page};
        } else {
            data = {keywords: keywords, startDate: startDate, endDate: endDate, page: page, sortkey: sortKey};
        }
        $.ajax({
            url: "article/query.action",
            data: data,
            type: "post",
            dataType: "json",
            success: function (data) {
                buildArticleList(data);
                highlightKeywords(keywords);
            }
        });
    }

    function highlightKeywords(keywords) {
        var articleUL = $(".list_box");
        articleUL.removeHighlight();
        for (var i = 0; i < keywords.length; i++) {
            articleUL.highlight(keywords[i]);
        }
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
        $("<span></span>", {class: "title"}).append($("<a></a>", {target: "_blank", href: "article/view/" + article.id + ".htm", html: article.title})).appendTo(li);
        $("<span></span>", {class: "response_count", html: article.replycount}).appendTo(li);
        $("<span></span>", {class: "author"}).append($("<a></a>"), {href: "#", html: article.author.nick}).appendTo(li);
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

    $("#search_btn").button().click(function () {
        doSearch(1);
    });
});