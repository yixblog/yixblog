/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-7-8
 * Time: 下午11:09
 * To change this template use File | Settings | File Templates.
 */
$(document).ready(function(){
    var uid_input = $("#login_uid");
    var pwd_input = $("#login_pwd");
    var val_input = $("#login_validate");
    var loginInputs = $([]).add(uid_input).add(pwd_input).add(val_input);
    var validateTip = $(".validate_info");

    $("#login_validate_img").click(function(){
        $(this)[0].src="validate/img"
    });
    $(".login_box").dialog({
        width:400,
        height:550,
        autoOpen:true,
        modal:true,
        resizable:false,
        buttons:{
            "登陆":function(){
                loginInputs.removeClass("ui-state-error");
                var $dialog = $(this);
                if(!checkLength(uid_input,"用户名",5,30,validateTip)){
                    return;
                }
                if(!checkEmpty(pwd_input,"密码",validateTip)){
                    return;
                }
                if(!checkEmpty(val_input,"验证码",validateTip)){
                    return;
                }
                var uid = uid_input.val();
                var pwd = pwd_input.val();
                var validate = val_input.val();
                $.ajax({
                    url:"accountservice/adminaccount/login.action",
                    data:{uid:uid,pwd:pwd,validate:validate},
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        if(data.success){
                            $dialog.dialog("close");
                        }else{
                            updateTips(validateTip,data.msg);
                            $("#login_validate_img").click();
                        }
                    }
                })
            },
            "忘记密码":function(){
                location.href="accountservice/adminaccount/forget_pwd_request.htm";
            }
        },
        close:function(){
            loginInputs.val("").removeClass("ui-state-error");
            validateTip.html("&nbsp;");
        },
        open:function(){
            $("#login_validate_img").click();
        }
    });
});
