<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
	<meta charset="utf-8" />

		<title>芜湖公安禁毒情报系统</title>

	<meta content="width=device-width, initial-scale=1.0" name="viewport" />

	<meta content="" name="description" />

	<meta content="" name="author" />

	<!-- BEGIN GLOBAL MANDATORY STYLES -->

	<link href="${pageContext.request.contextPath}/media/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/style-metro.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/style.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/style-responsive.css" rel="stylesheet" type="text/css"/>

	<link href="${pageContext.request.contextPath}/media/css/default.css" rel="stylesheet" type="text/css" id="style_color"/>

	<link href="${pageContext.request.contextPath}/media/css/uniform.default.css" rel="stylesheet" type="text/css"/>

	<!-- END GLOBAL MANDATORY STYLES -->

	<!-- BEGIN PAGE LEVEL STYLES -->

	<link href="${pageContext.request.contextPath}/media/css/login.css" rel="stylesheet" type="text/css"/>

	<!-- END PAGE LEVEL STYLES -->

	<link rel="shortcut icon" href="${pageContext.request.contextPath}/media/image/favicon.ico" />

</head>
 <body class="login">

	<!-- BEGIN LOGO -->

	<div class="logo">

		<img src="${pageContext.request.contextPath}/image/bocom_logo_middle.png" alt="" /> 

	</div>

	<!-- END LOGO -->

	<!-- BEGIN LOGIN -->

	<div class="content">

		<!-- BEGIN LOGIN FORM -->
		<form class="form-vertical login-form" > 

			<h3 class="form-title">请输入您的账号</h3>

			<div id="repeatEnter" class="alert alert-error hide">

				<button class="close" data-dismiss="alert"></button>
				
				<span>警号和密码错误，请重新输入。</span>

			</div>

			<div class="alert alert-error hide">

				<button class="close" data-dismiss="alert"></button>
				
				<span>输入你的警号和密码。</span>

			</div>

			<div class="control-group">

				<!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->

				<label class="control-label visible-ie8 visible-ie9">警号</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-user"></i>

						<input class="m-wrap placeholder-no-fix" type="text" placeholder="警号" name="policeCode"/>

					</div>

				</div>

			</div>

			<div class="control-group">

				<label class="control-label visible-ie8 visible-ie9">密码</label>

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-lock"></i>

						<input class="m-wrap placeholder-no-fix" type="password" placeholder="密码" name="password"/>

					</div>

				</div>

			</div>

			<div class="form-actions">

				<label class="checkbox">

				<input type="checkbox" name="remember" value="1"/> 保存账号

				</label>

				<button type="submit" class="btn green pull-right">

				登录 <i class="m-icon-swapright m-icon-white"></i>

				</button>            

			</div>

<!-- 			<div class="forget-password"> -->

<!-- 				<h4>忘记密码 ?</h4> -->

<!-- 				<p> -->

<!-- 					no worries, click <a href="javascript:;" class="" id="forget-password">here</a> -->

<!-- 					to reset your password. -->

<!-- 				</p> -->

<!-- 			</div> -->
		</form>

		<!-- END LOGIN FORM -->        

		<!-- BEGIN FORGOT PASSWORD FORM -->

		<form class="form-vertical forget-form" action="index.html">

			<h3 class="">Forget Password ?</h3>

			<p>Enter your e-mail address below to reset your password.</p>

			<div class="control-group">

				<div class="controls">

					<div class="input-icon left">

						<i class="icon-envelope"></i>

						<input class="m-wrap placeholder-no-fix" type="text" placeholder="Email" name="email" />

					</div>

				</div>

			</div>

			<div class="form-actions">

				<button type="button" id="back-btn" class="btn">

				<i class="m-icon-swapleft"></i> Back

				</button>

				<button type="submit" class="btn green pull-right">

				Submit <i class="m-icon-swapright m-icon-white"></i>

				</button>            

			</div>

		</form>

		<!-- END FORGOT PASSWORD FORM -->

	</div>

	<!-- END LOGIN -->

	<!-- BEGIN COPYRIGHT -->

	<div class="copyright">

		2016 &copy; bocom. 博康智能.

	</div>

	<!-- END COPYRIGHT -->

	<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->

	<!-- BEGIN CORE PLUGINS -->

	<script src="${pageContext.request.contextPath}/media/js/jquery-1.10.1.min.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/media/js/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>

	<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->

	<script src="${pageContext.request.contextPath}/media/js/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      

	<script src="${pageContext.request.contextPath}/media/js/bootstrap.min.js" type="text/javascript"></script>

	<!--[if lt IE 9]>

	<script src="${pageContext.request.contextPath}/media/js/excanvas.min.js"></script>

	<script src="${pageContext.request.contextPath}/media/js/respond.min.js"></script>  

	<![endif]-->   

	<script src="${pageContext.request.contextPath}/media/js/jquery.slimscroll.min.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/media/js/jquery.blockui.min.js" type="text/javascript"></script>  

	<script src="${pageContext.request.contextPath}/media/js/jquery.cookie.min.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/media/js/jquery.uniform.min.js" type="text/javascript" ></script>

	<!-- END CORE PLUGINS -->

	<!-- BEGIN PAGE LEVEL PLUGINS -->

	<script src="${pageContext.request.contextPath}/media/js/jquery.validate.min.js" type="text/javascript"></script>

	<!-- END PAGE LEVEL PLUGINS -->

	<!-- BEGIN PAGE LEVEL SCRIPTS -->

	<script src="${pageContext.request.contextPath}/media/js/app.js" type="text/javascript"></script>

	<script src="${pageContext.request.contextPath}/media/js/login.js" type="text/javascript"></script>      

	<!-- END PAGE LEVEL SCRIPTS --> 

	<script>

		jQuery(document).ready(function() {     

		  App.init();

		  Login.init();

		});

	</script>

	<!-- END JAVASCRIPTS -->

<script type="text/javascript">  
var _gaq = _gaq || [];  _gaq.push(['_setAccount', 'UA-37564768-1']);  _gaq.push(['_setDomainName', 'keenthemes.com']);  _gaq.push(['_setAllowLinker', true]);  _gaq.push(['_trackPageview']);  (function() {    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);  })();
$(document).ready(function(){
if("${isRepeat}"){
	$("#repeatEnter").show();
}
});
</script>
 
</body>

</html>