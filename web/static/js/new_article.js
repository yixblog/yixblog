/**
 * Created with IntelliJ IDEA.
 * User: Yixian
 * Date: 13-9-1
 * Time: 下午10:10
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function(){
    var editor = new UE.ui.Editor();
    editor.render("editor");
    editor.ready(function(){
        editor.setHeight(400);
    })
});