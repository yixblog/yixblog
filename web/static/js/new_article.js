/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-1
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function () {
    var editor = new UE.ui.Editor();
    editor.render("editor");
    editor.ready(function () {
        editor.setHeight(400);
    });

    $("#submit_article_btn").button().click(function () {
        var tags = $("#tags").val();
        var content = editor.getContent();
        console.log(content);
        var title = $("#article_title").val();
        if (title.length == 0) {
            alert("标题不能为空！");
            return;
        }
        $.ajax({
            url: "user/article/save.action",
            data: {title: title, content: content, tags: tags},
            dataType: "json",
            type: "post",
            success: function (data) {
                console.log("submitted return:" + JSON.stringify(data));
                location.href = "artile/view/" + data.id + ".htm";
            }
        })
    });

    $(".tag_btn").click(function () {
        var tagInput = $("#tags");
        tagInput.val(tagInput.val() + "," + $(this).html());
        tagInput.change();
    });

    $("#tags").change(function () {
        var tags = $(this).val();
        tags = fullToHalf(tags);
        console.log("full to half string:" + tags);
        tags = replaceSplitChar(tags);
        console.log("replaced tags string:" + tags);
        var tagArray = tags.split(",");
        var finalArray = [];
        for (var i = 0; i < tagArray.length; i++) {
            var tag = tagArray[i];
            pushToFinalArray(finalArray, tag);
        }
        console.log("final array");
        console.log(finalArray);
        $(this).val(buildArrayString(finalArray));
    });

    function buildArrayString(array) {
        var result = "";
        for (var i = 0; i < array.length; i++) {
            result += array[i] + ",";
        }
        result = result.substr(0, result.length - 1);
        return result;
    }

    function pushToFinalArray(array, tag) {
        if (!tagExists(array, tag)) {
            array.push(tag);
        }
    }

    function tagExists(array, tag) {
        for (var i = 0; i < array.length; i++) {
            if (tag == array[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return {string}
     */
    function fullToHalf(str) {
        var result = "";
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) == 12288) {
                result += String.fromCharCode(str.charCodeAt(i) - 12256);
                continue;
            }
            if (str.charCodeAt(i) > 65280 && str.charCodeAt(i) < 65375) {
                result += String.fromCharCode(str.charCodeAt(i) - 65248);
            } else {
                result += String.fromCharCode(str.charCodeAt(i));
            }
        }
        return result;
    }

    function replaceSplitChar(str) {
        str = str.replace(/;/g, ",");
        str = str.replace(/\|/g, ",");
        str = str.replace(/,+/, ",");
        return str;
    }
});