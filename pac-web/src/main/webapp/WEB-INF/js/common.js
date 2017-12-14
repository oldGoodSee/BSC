/**
 * 公共js
 */

var Common = function () {
	var localObj = window.location;

	var contextPath = localObj.pathname.split("/")[1];

	var basePath = localObj.protocol+"//"+localObj.host+"/"+contextPath;
	
	return {
		"localObj":localObj,
		"contextPath":contextPath,
		"basePath":basePath
	};
}();

function verifyLength(str, length, content)
{
    var strLength = str.replace("[^\x00-\xff]/g","aa").length;
    if (strLength > length)
    {
        alert(content+"的长度不能超过"+length+"个字符,其中中文占两个字符");
        return false;
    }
    return true;
}

Date.prototype.format = function(format)
{ 
    var o = { 
    "M+" : this.getMonth()+1, //month 
    "d+" : this.getDate(), //day 
    "h+" : this.getHours(), //hour 
    "m+" : this.getMinutes(), //minute 
    "s+" : this.getSeconds(), //second 
    "q+" : Math.floor((this.getMonth()+3)/3), //quarter 
    "S" : this.getMilliseconds() //millisecond 
    } 

    if(/(y+)/.test(format)) { 
    format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    } 

    for(var k in o) { 
    if(new RegExp("("+ k +")").test(format)) { 
    format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
    } 
    } 
    return format; 
}

// 格式化yyyy-mm-dd
function getNowFormatDate() 
{ 
var day = new Date(); 
var Year = 0; 
var Month = 0; 
var Day = 0; 
var CurrentDate = ""; 
//初始化时间 
Year= day.getFullYear();//ie火狐下都可以 
Month= day.getMonth()+1; 
Day = day.getDate(); 
//Hour = day.getHours(); 
// Minute = day.getMinutes(); 
// Second = day.getSeconds(); 
CurrentDate += Year + "-"; 
if (Month >= 10 ) 
{ 
CurrentDate += Month + "-"; 
} 
else 
{ 
CurrentDate += "0" + Month + "-"; 
} 
if (Day >= 10 ) 
{ 
CurrentDate += Day ; 
} 
else 
{ 
CurrentDate += "0" + Day ; 
} 
return CurrentDate; 
}

function getContextRoot() {
    var webroot = document.location.href;
    webroot = webroot.substring(webroot.indexOf("//") + 2, webroot.length);
    webroot = webroot.substring(webroot.indexOf("/") + 1, webroot.length);
    webroot = webroot.substring(0, webroot.indexOf('/'));

    rootpath = "/" + webroot;

    return rootpath;
}
