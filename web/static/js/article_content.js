/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-8
 * Time: 下午9:41
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {
    SyntaxHighlighter.highlight($(".article_content"));
    var articleId = $("#articleIdInput").val();

    function gotoCommentPage(page) {
        $.ajax({
            url: "a/comments/list.action",
            type: "post",
            dataType: "json",
            data: {articleId: articleId, page: page},
            success: function (data) {
                var comments = data.comments;
                if (comments != null) {
                    showComments(data);
                }
            }
        });
    }

    gotoCommentPage(1);

    function showComments(data) {
        var commentBox = $(".comment_list ul");
        commentBox.empty();
        for (var index = 0; index < data.comments.length; index++) {
            var comment = data.comments[index];
            var li = $("<li></li>");
            var authorLink = $("<a></a>", {href: "userinfo/" + comment.author.id + ".htm", target: "_blank"}).html(comment.author.nick);
            $("<div></div>", {class: "author"}).append(authorLink).appendTo(li);
            $("<div></div>", {class: "time", html: comment.addtimestring}).appendTo(li);
            var contentBox = $("<div></div>", {class: "comment_content", html: comment.content}).appendTo(li);
            li.appendTo(commentBox);
        }
        SyntaxHighlighter.highlight(".comment_content");
        commentBox.yixpager({
            pageInfo: data,
            callback: gotoCommentPage
        })
    }

    if ($("#new_comment").length > 0) {
        var editor = new UE.ui.Editor();
        editor.render("new_comment");
        editor.ready(function () {
            editor.setHeight(200);
        });

        $("#submit_new_comment").button().click(function () {
            var commentContent = editor.getContent();
            if (commentContent.length > 0) {
                $.ajax({
                    url: "a/user/comments/add_comment.action",
                    type: "post",
                    data: {articleId: articleId, content: commentContent},
                    dataType: "json",
                    success: function (data) {
                        if (!data.success) {
                            alert(data.msg);
                        } else {
                            window.location.href = location.href;
                        }
                    }
                })
            }
        });
    }
})
;