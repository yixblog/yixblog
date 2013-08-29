/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-26
 * Time: 上午12:16
 */
function doUserLogOut() {
    $.ajax({
        url: "accountservice/account/user/logout.action",
        dataType: "json",
        type: "post",
        success: function () {
            window.location.reload();
        }
    })
}
function updateTips( tipItem,message ) {
    tipItem
        .text( message )
        .addClass( "ui-state-highlight" );
    setTimeout(function() {
        tipItem.removeClass( "ui-state-highlight", 1500 );
    }, 500 );
}
function checkEmpty(input,name,tipbox){
    var text = trimInput(input);
    if(text.length==0){
        input.addClass("ui-state-error").focus();
        updateTips(tipbox,name+"不能为空");
        return false;
    }
    return true;
}
function trimInput(input) {
    var text = $.trim(input.val());
    input.val(text);
    return text;
}
function checkReg(input,name,reg,tipbox){
    var text = trimInput(input);
    if(!text.match(reg)){
        input.addClass("ui-state-error").focus();
        updateTips(tipbox,name+"的格式不正确");
        return false;
    }
    return true;
}
function checkSimilar(input1,input2,msg,tipbox){
    var text1=trimInput(input1);
    var text2 = trimInput(input2);
    if(text1!=text2){
        input2.addClass("ui-state-error").focus();
        updateTips(tipbox,msg);
        return false;
    }
    return true;
}
function checkLength(input,name,minLen,maxLen,tipbox){
    var text = trimInput(input)
    if(text.length<minLen||text.length>maxLen){
        input.addClass("ui-state-error").focus();
        updateTips(tipbox,name+"的长度必须在"+minLen+"到"+maxLen+"之间");
        return false;
    }
    return true;
}
$(document).ready(function () {
    var login_uid_input = $("#user_login_uid_input");
    var login_pwd_input = $("#user_login_pwd_input");
    var login_validate_input = $("#user_login_validate_input");
    var loginInputs = $([]).add(login_uid_input).add(login_pwd_input).add(login_validate_input);
    var validateTips = $(".validate_tips");

    //login
    $("#login_dialog").dialog({
        autoOpen:false,
        height:400,
        width:350,
        modal:true,
        resizable:false,
        buttons:{
            "登陆":function(){
                loginInputs.removeClass("ui-state-error");
                var validate = true;
                var $this = $(this);
                validate = validate && checkEmpty(login_uid_input,"用户名",validateTips);
                if(!validate){
                    return;
                }
                validate = validate && checkEmpty(login_pwd_input,"密码",validateTips);
                if(!validate){
                    return;
                }
                validate = validate && checkEmpty(login_validate_input,"验证码",validateTips);
                if(!validate){
                    return;
                }
                var uid = login_uid_input.val();
                var pwd = login_pwd_input.val();
                var validateCode = login_validate_input.val();
                $.ajax({
                    url:"accountservice/account/login.action",
                    data:{uid:uid,pwd:pwd,validate:validateCode},
                    type:"post",
                    dataType:"json",
                    success:function(data){
                        if(data.success){
                            location.reload();
                            $this.dialog("close");
                        }
                        updateTips(validateTips,data.msg);
                    }
                });

            },
            "忘记密码":function(){
                window.open("accountservice/account/")
            },
            "注册":function(){
                window.open("accountservice/account/register.htm");
            }
        },
        close:function(){
            loginInputs.val("").removeClass("ui-state-error");
            validateTips.html("&nbsp;");
        },
        open:function(){
            $("#validate_img").click();
        }
    });
    $("#validate_img").click(function(){
        $("#validate_img")[0].src="validate/img";
    });
    $("#login_button").click(function () {
        $("#login_dialog").dialog("open");
    });
});
