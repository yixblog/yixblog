/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-8
 * Time: 下午9:41
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {
    $(".comment_box").mouseenter(function () {
        $(".comment_list").stop(true, false).slideDown();
    }).mouseleave(function () {
            $(".comment_list").stop(true, false).slideUp();
        });

    var articleId = $("#articleIdInput").val();

    function gotoCommentPage(page) {
        $.ajax({
            url: "comments/list.action",
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
            var authorLink = $("<a></a>").html(comment.author.nick);
            $("<div></div>").addClass("author").append(authorLink).appendTo(li);
            $("<div></div>").addClass("time").html(comment.addtimestring).appendTo(li);
            $("<div></div>").addClass("comment_content").html(comment.content).appendTo(li);
            li.appendTo(commentBox);
        }
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
            var title = $.trim($("#comment_title").val());
            if (commentContent.length > 0) {
                $.ajax({
                    url: "user/comments/add_comment.action",
                    type: "post",
                    data: {articleId: articleId, content: commentContent, title: title},
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

    uParse('div', { 'highlightJsUrl': 'static/lib/ueditor/third-party/SyntaxHighlighter/shCore.js', 'highlightCssUrl': 'static/lib/ueditor/third-party/SyntaxHighlighter/shCoreDefault.css'})
})
;