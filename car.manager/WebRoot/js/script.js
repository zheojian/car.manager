function loadFail(html){
	$("#loading p").html("出错了，错误代码："+html.status);
}
function loadDom(href) {
	$("#loading").modal("show");
	//get data
	$.post(href,{ t: (new Date().getTime()) }).done(function( data ) {
		bindData = $.parseJSON( data );
		if($.type(bindData)==="array"){
			for(var item in bindData){
				var tr = "<tr>"+$("table.bind tr:first-child").html()+"</tr>";
				tr = tr.replace(/th/g,"td");
				$("table.bind tbody").append(tr);
				var trData = bindData[item];
				for(var cell in trData){
					var val,opts;
					if($.type(trData[cell]) === "array"){
						opts = trData[cell];
						val = opts[opts.length-1];
						$("table.bind tr:last-child").children("td[bindData='"+cell+"']").attr("options",opts);
					}else{
						val = trData[cell];
					}
					$("table.bind tr:last-child").children("td[bindData='"+cell+"']").html(val);
				}
			}
			$("td[bindOperate]").each(function(data){
				$this = $(this);
				var opts = $this.attr("bindOperate").split(",");
				$this.html("");
				for(var i in opts){
					if(opts[i]==="update"){
						$this.append("&nbsp;<a onclick='update(this)'>修改</a>");
					}else if(opts[i]==="del"){
						$this.append("&nbsp;<a onclick='del(this)'>删除</a>");
					}
				}
			});
		}else{
			for(var item in bindData){
				var i = $("#"+item);
				i.val(bindData[item]).attr("resetValue",bindData[item]);
			}
		}
		$("#loading").modal("hide");
	}).fail(
		function(data){
			loadFail(data);
		}		
	);
}
function nreset(){
	$("[resetValue]").each(function(){
		if(this.tagName == "INPUT"){
			$(this).val($(this).attr("resetValue"));
		}else if(this.tagName == "OPTION"){
			$(this).attr("selected",true);
		}
	});
	//清空所有表单验证提示
	$(".Validform_checktip").html('').removeClass("Validform_right").removeClass("Validform_wrong");
}
