//function fillData(jqDom){
//	switch(jqDom[0].tagName){
//		case "TABLE":
//			var ths = jqDom.find("th");
//			var innerStr = "";
//			for(var i=0;i<data.list.length;i++){
//				innerStr += "<tr>";
//				for(var j=0;j<ths.length;j++){
//					innerStr += '<td>'+data.list[i][ths[j].attr("colname")]+'</td>';
//				}
//				innerStr += "</tr>";
//			}
//			demo(jqDom,data);
//			break;
//		default:
//			break;
//	}
//}

function fillData(url,paramObj,jqDom,pageDom,curr){
	$.ajax({
	   type: "GET",
	   url: url,
	   data: paramObj,
	   success: function(res){
		  var ths = jqDom.find("th");
		  var trs = jqDom.find("tr");
		  for(var k=1;k<trs.length;k++){
			  trs.eq(k).remove();
		  }
		  var innerStr = "";
		  for(var i=0;i<res.page.list.length;i++){
			innerStr += "<tr>";
			for(var j=0;j<ths.length;j++){
				innerStr += '<td>'+res.page.list[i][ths.eq(j).attr("colname")]+'</td>';
			}
			innerStr += "</tr>";
		}
		  jqDom.find("tbody").html(jqDom.find("tbody").html()+innerStr);
	    //显示分页
	    laypage({
	      cont: pageDom, //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
	      pages: res.pages, //通过后台拿到的总页数
	      curr: curr || 1, //当前页
	      jump: function(obj, first){ //触发分页后的回调
	        if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
	        	fillData(url,jqDom,pageDom,paramObj,obj.curr);
	        }
	      }
	    });
	   },
	   error:function(){
		   layer.alert("获取数据失败！");
	   }
	});
};

function ajaxChart(jqDom){
	if(config.url){
		$.ajax({
		   type: "GET",
		   url: config.url,
		   success: function(res){
			   var list = [];
			   config.data = res.series;
			   config.jqDom = jqDom;
			   for(var i=0;i<res.series.length;i++){
				   var obj = {
						  name:res.series[i].cityName,
						  data:[],
						  pointPlacement: 'on'
				   };
				   for(var j=0;j<res.series[i].data.length;j++){
					   if($.inArray(config.data[i].data[j].code,config.colNames) != -1){
						   obj.data.push(config.data[i].data[j].data);
						}
				   }
				   list.push(obj);
			   }
			   $("#blankTip").remove();
			   fillChart(jqDom,list);
		   },
		   error:function(){
			   layer.alert("获取数据失败！");
		   }
	   });
	}
}

function fillChart(jqDom,data){
	config.chart = Highcharts.chart(jqDom.attr("id"),{
        chart: {
            polar: true,
            type: 'line'
        },
        pane: {
            size: '80%'
        },
        xAxis: {
            categories: config.colNames,
            tickmarkPlacement: 'on',
            lineWidth: 0,
            labels:{
                formatter:function() {
                    return " <a href='javascript:void(0)' class='clickable-link'>"+getLabel(this.value)+"</a>";
                }
            }
        },
        yAxis: {
            gridLineInterpolation: 'polygon',
            lineWidth: 0,
            min: 0
        },
        tooltip: {
            shared: true,
            pointFormat: '<span style="color:{series.color}">{series.name}:<b>{point.y:,.0f}</b><br/>'
        },
        legend: {
            align: 'right',
            verticalAlign: 'top',
            y: 70,
            layout: 'vertical'
        },
        series: data
    });
}

function getLabel(v){
	for(var i =0;i<config.labels.length;i++){
		if(v == config.colNames[i]){
			return config.labels[i];
		}
	}
}

function resetChart(){
	if(!config.data){
		return;
	}
	config.chart.destroy();
	var list  = [];
	for(var i=0;i<config.data.length;i++){
	   var obj = {
			  name:config.data[i].cityName,
			  data:[],
			  pointPlacement: 'on'
	   };
	   for(var j=0;j<config.data[i].data.length;j++){
		   if($.inArray(config.data[i].data[j].code,config.colNames) != -1){
			   obj.data.push(config.data[i].data[j].data);
			}
	   }
	   list.push(obj);
    }
	fillChart(config.jqDom,list);
	/*
	config.chart.xAxis[0].setCategories(config.colNames,false);
	for(var i=0;i<config.data.length;i++){
		var list = [];
		for(var j=0;j<config.data[i].data.length;j++){
			if($.inArray(config.data[i].data[j].code,config.colNames) != -1){
				list.push(config.data[i].data[j].data);
			}
		}
		config.chart.series[i].setData(list);
		console.log(list)
	}
	setTimeout(function(){
		console.log(config.chart.series[0].data.length);
		console.log(config.chart.series[1].data.length);
		config.chart.redraw()
	},2000);
	*/
}