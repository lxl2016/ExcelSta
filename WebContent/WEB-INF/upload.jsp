<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Excel upload</title>
	<style>
		h1{
			text-align:center;
		}
		.mainDiv{
			width:500px;
			border:1px solid #6A6AFF;
			background-color:#ECFFFF;
			margin:30px auto 0 auto;
		}
		.mainDiv div{
			width:95%;
			margin:20px auto 0 auto;
		}
		input{
			border:1px solid #3D7878;
		}
	</style>
</head>
<body>
	<h1>Excel upload</h1>
	<div class="mainDiv">
		<form action="ExcelHandler" method="post" enctype="multipart/form-data">
			<div style="width:95%;margin:auto;">
				<label for="fu" style="display:inline-block;width:30%">上传文件：</label>
				<input style="width:68%" type="file" id="fu" name="fileupload" />
			</div>	
			<div style="width:95%;margin:20px auto 0 auto">
				<input type="button" id="sbutton" style="width:100%;height:30px;background-color:#A5A552" value="上    传" onclick="dis()"/>
			</div>	
		</form>
	</div>
</body>
<script>
	function dis(){
		if(document.getElementById("fu").value == ""){
			alert("上传文件不能为空！");
			return;
		}
		document.getElementById("sbutton").value="正在上传。。。。";
		document.getElementById("sbutton").disabled = true;
		document.forms[0].submit();
	}


</script>

</html>