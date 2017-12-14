//定义好的正则表达式
var regObj = [
	{reg:/^[\d]{1,}$/,tip:"必须是数字"},		//数字
	{reg:/^[A-Za-z]{1,}$/,tip:"必须是英文"},	//英文
	{reg:/^[\u4e00-\u9fa5]{1,}$/,tip:"必须是中文"},	//中文
	{reg:/^[A-Za-z\u4e00-\u9fa5]{1,}$/,tip:"必须是中文或英文"},	//中英文
	{reg:/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])(\d{4}|\d{3}x)$/,tip:"必须是18位身份证号码"},	 //身份证
	{reg:/^0?(13|14|15|18)[0-9]{9}$/,tip:"必须是国内号码"},	//手机
];

//绑定事件，失去焦点检验
function autoCheckForm(id){
	var inputs = $("#"+id).find("input");
	var select = $("#"+id).find("select");
	inputs.on("blur",function(){
		if($(this).attr("type") != "file"){
			$(this).val($.trim($(this).val()));
			checkItem($(this));
		}
	});
	select.on("blur",function(){
		checkItem($(this));
	});
}

//提交时全部检验
var canSubmit = true;
function checkForm(id){
	canSubmit = true;
	var inputs = $("#"+id).find("input");
	var textareas = $("#"+id).find("textarea");
	var select = $("#"+id).find("select");
	for(var i=0;i<inputs.length;i++){
		if(inputs.eq(i).attr("name") && inputs.eq(i).attr("type") != "file"){
			inputs.eq(i).val($.trim(inputs.eq(i).val()));
			checkItem(inputs.eq(i))
		}
	}
	for(var i=0;i<select.length;i++){
		if(select.eq(i).attr("name")){
			checkItem(select.eq(i))
		}
	}
}

function checkItem(jqObj){
	if(jqObj.attr("requried")&&$.trim(jqObj.val())==""){
		if("blank" != jqObj.attr("errortype")){
			jqObj.attr("errortype","blank");
			jqObj.parent().addClass("has-error");
			jqObj.tooltip("destroy");
			jqObj.tooltip({
				title:"该项为必填项",
				animation:false,
				placement:"bottom"
			});
			jqObj.tooltip("show");
		}
		canSubmit = false;
		return;
	}
	if(jqObj.attr("len")){
		var len = parseInt(jqObj.attr("len"));
		if(len){
			if(jqObj.val().replace(/[\u4e00-\u9fa5]/g,"aa").length>len){
				if("outLen" != jqObj.attr("errortype")){
					jqObj.attr("errortype","outLen");
					jqObj.parent().addClass("has-error");
					jqObj.tooltip("destroy");
					jqObj.tooltip({
						title:"长度超过限制,最多"+len+"个字符",
						animation:false,
						placement:"bottom"
					});
					jqObj.tooltip("show");
				}
				canSubmit = false;
				return;
			}
		}
	}
	if(jqObj.attr("reg") && jqObj.attr("reg") != "0"){
		if(jqObj.val()!=""&&jqObj.val().match(regObj[parseInt(jqObj.attr("reg"))-1].reg)==null){
			if("regError" != jqObj.attr("errortype")){
				jqObj.attr("errortype","regError");
				jqObj.parent().addClass("has-error");
				jqObj.tooltip("destroy");
				jqObj.tooltip({
					title:regObj[parseInt(jqObj.attr("reg"))-1].tip,
					animation:false,
					placement:"bottom"
				});
				jqObj.tooltip("show");
			}
			canSubmit = false;
			return;
		}
	}
	jqObj.attr("errortype","");
	jqObj.parent().removeClass("has-error");
	jqObj.tooltip("destroy");
}
