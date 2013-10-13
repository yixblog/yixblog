/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-7-13
 * Time: 上午12:07
 * To change this template use File | Settings | File Templates.
 */
$(function(){
    var uid_input = $("#register_uid");
    var pwd_input = $("#register_pwd");
    var pwd2_input = $("#register_pwd2");
    var nick_input = $("#register_nick");
    var email_input = $("#register_email");
    var sex_selector = $("#register_sex");
    var validate_input = $("#register_validate");

    var regInputs = $([]).add(uid_input).add(pwd_input).add(pwd2_input).add(nick_input).add(email_input).add(sex_selector).add(validate_input);
    var validateImg = $("#validate_img");
    var form_tag = $(".validate_info");
    validateImg.click(function(){
        validateImg[0].src="validate/img";
    });
    $(".login_box").dialog({
        width:400,
        height:650,
        resizable:false,
        modal:false,
        autoOpen:true,
        buttons:{
            "注册":function(){
                regInputs.removeClass("ui-state-error");
                if(!checkLength(uid_input,"登陆用户名",5,25,form_tag)){
                    return;
                }
                if(!checkEmpty(pwd_input,"密码",form_tag)){
                    return;
                }
                if(!checkReg(email_input,"email",/^[0-9a-zA-Z_]+[0-9a-zA-Z_.]*@[0-9a-zA-Z.-_]+/,form_tag)){
                    return;
                }
                if(!checkSimilar(pwd_input,pwd2_input,"两次输入的密码不同",form_tag)){
                    return;
                }
                if(!checkEmpty(validate_input,"验证码",form_tag)){
                    return;
                }
                var uid = uid_input.val();
                var pwd = pwd_input.val();
                var nick = nick_input.val();
                nick = nick.length==0?uid:nick;
                var email = email_input.val();
                var sex = sex_selector.val();
                var validate = validate_input.val();
                $.ajax({
                    url:"a/accountservice/account/register.action",
                    type:"post",
                    dataType:"json",
                    data:{uid:uid,pwd:pwd,nick:nick,email:email,sex:sex,validate:validate},
                    success:function(data){
                        if(data.success){
                            location.href="index.htm"
                        }else{
                            updateTips(form_tag,data.msg);
                        }
                    }
                })
            }
        },
        open:function(){
            validateImg.click();
        },
        close:function(){
            form_tag.html("&nbsp;");
            regInputs.removeClass("ui-state-error").val("");
        }
    })
});