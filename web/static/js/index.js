/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-6-29
 * Time: 下午12:05
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {

    function initTagCloud(tags) {
        var tagArray = [];
        for (var i = 0; i < tags.length; i++) {
            var tag = tags[i];
            tagArray[i] = {text: tag.tag, weight: tag.count, link: "article/tag/" + tag.tag + ".htm"};
        }
        $("#tag_cloud_box").empty().jQCloud(tagArray, {width: 200, height: 478});
    }

    function initUserCloud(users) {
        var userArray = [];
        for (var i = 0; i < users.length; i++) {
            var user = users[i];
            userArray[i] = {text: user.nick, weight: user.articleCount, link: "userinfo/" + user.id + ".htm"};
        }
        $("#tag_cloud_box").empty().jQCloud(userArray, {width: 200, height: 478});
    }

    //标签切换
    $(".left_box .cloud_box .tab").click(function () {
        $(".left_box .cloud_box .active").removeClass("active");
        $(this).addClass("active");
        var type = $(this).prevAll().length;
        if (type > 0) {
            //作者
            $.ajax({
                url: "a/article/hot_users.action",
                type: "post",
                data: {topnumber: 20},
                dataType: "json",
                success: function (data) {
                    var users = data.users;
                    initUserCloud(users);
                }
            })
        } else {
            //标签
            $.ajax({
                url: "a/article/tag_cloud.action",
                type: "post",
                data: {topnumber: 20},
                dataType: "json",
                success: function (data) {
                    var tags = data.tags;
                    initTagCloud(tags);
                }
            })
        }
    });

    $(".left_box .cloud_box .tab:eq(0)").click();

    //正文tag
    $.yix.fragment({
        tabs: $(".center_box .article_box .tab_box .item"),
        contents: $(".center_box .article_box .fragment_box .item"),
        activeClass: "active"
    });

    $("#search_button").click(function () {
        var keywords = $("#search_input").val().split(" ");
        var param = "";
        for (var i = 0; i < keywords.length; i++) {
            param += "&keywords[]=" + encodeURI(keywords[i]);
        }
        param = param.substr(1);
        var uri = "a/article/query.htm?" + param;
        window.open(uri);
    });
});