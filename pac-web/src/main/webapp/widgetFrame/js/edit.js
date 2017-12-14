function edit(){
	$("button").off("click");
	
	$(".ajax-data").on("click",function(e){
		e.preventDefault();
		window.parent.selDom($(e.target));
		return false;
	});
}