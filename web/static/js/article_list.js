/**
 * Created with IntelliJ IDEA.
 * User: yixian
 * Date: 13-9-29
 * Time: 上午10:35
 * To change this template use File | Settings | File Templates.
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
});