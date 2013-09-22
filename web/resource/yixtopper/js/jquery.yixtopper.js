/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-22
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
var jsPath = $("script:last").attr("src");
$(document).ready(function () {
    var upDiv = createUpDiv();
    var body = $(top.document).find("body");
    body.append(upDiv);
    $(top.window).scroll(function () {
        var top = $(this).scrollTop();
        if (top > 0) {
            if (upDiv.is(":hidden")) {
                upDiv.fadeIn();
            }
        } else {
            if (!upDiv.is(":hidden")) {
                upDiv.stop(true, false).fadeOut();
            }
        }
    });
    upDiv.click(function () {
        body.animate({scrollTop: 0});
    });

    function createUpDiv() {
        var upDiv = $("<div></div>");
        var imgPath = "../img/arrow_up.png";
        var imgAbsolutePath = jsPath.substr(0, jsPath.lastIndexOf("/") + 1) + imgPath;
        upDiv.css({position: "fixed", right: "30px", bottom: "30px", "z-index": 9999, cursor: "pointer",
            display: "none", width: "64px", height: "64px", background: "url('" + imgAbsolutePath + "') no-repeat"});
        return upDiv;
    }
});