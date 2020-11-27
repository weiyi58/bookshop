$(function() {
	/*
	 * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
	 */
	$(".errorClass").each(function() {

		showError($(this));//遍历每个元素，使用每个元素来调用showError方法
	});
	
	/*
	 * 2. 切换注册按钮的图片
	 */
	$("#submitBtn").hover(
		function() {

			$("#submitBtn").attr("src", "/bookshop/images/regist2.jpg");
		},
		function() {
			$("#submitBtn").attr("src", "/bookshop/images/regist1.jpg");
		}
	);
	//输入框得到焦点隐藏错误信息

	$(".inputClass").focus(function (){
	    //alert();
	    $("#"+$(this).attr("id")+"Error").text("")
	    showError($("#"+$(this).attr("id")+"Error"));
	})

	//输入框失去焦点校验
	$(".inputClass").blur(function(){
	    var id = $(this).attr("id");

	    //得到需调用的函数名,
        // 拼接方法，substring(0,1)表示第一位，转化为大写之后
        //substrin表示第二位及以后的，拼接成函数名
        var funName = "validate" + id.substring(0,1).toUpperCase() + id.substring(1)+ "()";
        
        //执行函数调用
        eval(funName);

	});
	$("#registForm").submit(function (){

	    var bool = true;
	    if(!validateLoginname()){
	        bool = false;}
	    if(!validateLoginpass()){
	        bool = false;}
	    if(!validateReloginpass()){
	        bool = false;}
	    if(!validateEmail()){
	        bool = false;}
	    if(!validateVerifyCode()){
	        bool = false;}

	    return bool;

	});

});

//登录名校验方法
function validateLoginname() {
     var id = "loginname";
    var value = $("#"+id).val();//获取输入框内容

    //非空校验
    if (!value){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("用户名不能为空！");
         showError($("#"+id+"Error"))
         return false;
    }
    //长度校验
    if (value.length < 3 || value.length>20){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */

         $("#"+id+"Error").text("用户名长度必须在3-20之间");
         showError($("#"+id+"Error"));
         return false;
    }

    //是否注册校验
    $.ajax({
        url:"/bookshop/UserServlet",
        data: {method:"ajaxValidateLoginname",loginname:value},
        type:"post",
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            if (!data){
                $("#"+id+"Error").text("用户名已被注册");
                showError($("#"+id+"Error"));
                return false;
            }
        }
    })


     return true;
}
//登录密码验方法
function validateLoginpass(){
    var id = "loginpass";
    var value = $("#"+id).val();//获取输入框内容
    //非空校验
    if (!value){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("密码不能为空！");
         showError($("#"+id+"Error"))
         return false;
    }
    //长度校验
    if (value.length < 3 || value.length>20){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */

         $("#"+id+"Error").text("密码长度必须在3-20之间");
         showError($("#"+id+"Error"));
         return false;
    }
     return true;
}
//确认密码验方法
function validateReloginpass(){
    var id = "reloginpass";
    var value = $("#"+id).val();//获取输入框内容
    //非空校验
    if (!value){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("密码不能为空！");
         showError($("#"+id+"Error"))
         return false;
    }
    //两次输入是否一致
    if (value != ($("#loginpass").val())){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */

         $("#"+id+"Error").text("两次输入密码不一致");
         showError($("#"+id+"Error"));
         return false;
    }
     return true;
}
//email校验方法
function validateEmail(){
    var id = "email";
    var value = $("#"+id).val();//获取输入框内容

    //非空校验
    if (!value){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("email不能为空！");
         showError($("#"+id+"Error"))
         return false;
    }
    //Email格式校验
    if (!(/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/).test(value)){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("错误的Email格式1");
         showError($("#"+id+"Error"));
         return false;
    }

    $.ajax({
        url:"/bookshop/UserServlet",
        data: {method:"ajaxValidateEmail",email:value},
        type:"post",
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            if (!data){
                $("#"+id+"Error").text("email已被注册");
                showError($("#"+id+"Error"));
                return false;
            }
        }

    })
     return true;
}
//验证码校验方法
function validateVerifyCode(){
     var id = "verifyCode";
    var value = $("#"+id).val();//获取输入框内容
    //非空校验
    if (!value){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */
         $("#"+id+"Error").text("验证码不能为空！");
         showError($("#"+id+"Error"))
         return false;
    }
    //长度校验
    if (value.length != 4){
        //获取对应lable，
        /*
        添加错误信息
        显示lable
         */

         $("#"+id+"Error").text("验证码长度不正确");
         showError($("#"+id+"Error"));
         return false;
    }
    //是否正确校验
    $.ajax({
        url:"/bookshop/UserServlet",
        data: {method:"ajaxValidateVerifyCode",verifyCode:value},
        type:"post",
        dataType:"json",
        async:false,
        cache:false,
        success:function (data){
            if (!data){
                $("#"+id+"Error").text("验证码错误");
                showError($("#"+id+"Error"));
                return false;
            }
        }

    })
    return true;
}
/*
 * 判断当前元素是否存在内容，如果存在显示，不页面不显示！
 */
function showError(ele) {

	var text = ele.text();//获取元素的内容
	if(!text) {//如果没有内容

		ele.css("display", "none");//隐藏元素
	} else {//如果有内容

		ele.css("display", "");//显示元素
	}
}

/*
 * 换一张验证码
 */
function _hyz() {
	/*
	 * 1. 获取<img>元素
	 * 2. 重新设置它的src
	 * 3. 使用毫秒来添加参数
	 */
	$("#imgVerifyCode").attr("src", "/bookshop/VerifyCodeServlet?a=" + new Date().getTime());
}
