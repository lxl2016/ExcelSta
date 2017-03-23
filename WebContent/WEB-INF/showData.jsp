<%@ page language="java" import="java.util.*,com.niit.org.beans.*,java.text.SimpleDateFormat,com.niit.org.util.Dic" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Show Data</title>
	<style>
		table tr:nth-child(even){background-color:#D4FFFF;}
		body{
			over-flow:scroll;
		}
		th,td{
			white-space:nowrap;
			padding:5px;
		}
		.selectDiv{
			border:solid 1px black;
			width:100px;
			text-align:center;
		}
		.selectDiv span{
			display:inline-block;
			width:50%;
			text-align:center;
			background-color:#ADADAD;
		}
		.detail{
			display:inline_block;
			margin-left:3px;
		}
		.detail:hover{
			cursor:pointer;
		}
		.menu{
			border:1px solid blue;
			width:100px;
		}
		a{
			text-decoration:none;
		}
		a:hover{
			color:red;
		}
		a:link,a:visited{
			color:blue;
		}
		.applyLeave{
			height:200px;
			width:400px;
			position:absolute;
			z-index:3;
			background-color:#FFF8D7;
			display:none;
			border:1px solid #C6A300;
			padding:4px;
		}
		.changeName{
			height:100px;
			width:400px;
			position:absolute;
			z-index:4;
			background-color:#FFF8D7;
			display:none;
			border:1px solid #C6A300;
			padding:4px;
		}
	</style>
	<%
		List<User> userList = (List<User>)request.getAttribute("userList");
		if(userList != null)Collections.sort(userList);
		String startDate = (String)request.getAttribute("startDate");
		String endDate = (String)request.getAttribute("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = startDate==null?new Date():sdf.parse(startDate);
		Date ed = endDate==null?new Date():sdf.parse(endDate);
		Calendar scl = Calendar.getInstance();
		scl.setTime(sd);
	%>
</head>
<body>
	<div class="menu">
		<span><a href="ExcelUpload">上传</a></span>
	</div>
	<div style="margin-top:50px;">
		<form method="post" action="ShowData">
			<div>
			<label for="sd">查询日期：</label>
			<input type="date" name="startDate" id="sd" <%if(startDate!=null){ %>value="<%=startDate %>"<%} %> required="required"/>
			-
			<input type="date" name="endDate" id="ed" <%if(endDate!=null){ %>value="<%=endDate %>"<%} %> required="required"/>
			
			<!-- <label for="stuName" style="margin-left:20px">姓名</label>
			<input type="text" name="stuName" id="stuName" />
			 -->
			
			<input type="button" value="查询" style="margin-left:50px" onclick="querydata()" />
			<input type="button" value="导出" style="margin-left:7px" onclick="exportdata()"/>
			</div>
		</form>
	</div>
	<div class="selectDiv">
		<span id="detail" style="background:white;" onclick="exchange()">详情</span><span id="sta" onclick="exchange()">统计</span></div>
	<div>
	<%if( startDate != null && endDate != null){ %>
		<table border="1" cellspacing="0">
			<tr>
				<th>工号</th>
				<th>姓名</th>
				<th>部门</th>
				<%
				sdf.applyPattern("MM-dd");
				while(!scl.getTime().after(ed)){
					%>
					<th><%=sdf.format(scl.getTime()) %></th>
				<%
					scl.add(Calendar.DAY_OF_MONTH,1);
				}%>
			</tr>
			<%
			sdf.applyPattern("yyyy-MM-dd");
			for(User user:userList){ 
				scl.setTime(sd);
			%>
			<tr>
				<td><a href="javascript:showAL(<%=user.getNo() %>)"><%=user.getNo() %></a>
					<div class="applyLeave" id="apply<%=user.getNo() %>">
						<div>
							<span style="font-weight:bold">请假申请</span>
							<span style="float:right"><a href="javascript:hide('apply<%=user.getNo() %>')">关闭</a></span>
						</div>
						<div style="margin-top:2px">
							<label>工号：</label>
							<span><%=user.getNo() %></span>
						</div>
						<div>
							<label>姓名：</label>
							<span><%=user.getName() %></span>
						</div>
						<div style="margin-top:16px">
							<label>日期：</label>
							<input type="date" id="lsd<%=user.getNo() %>"/>
							-
							<input type="date" id="led<%=user.getNo() %>"/>
						</div>
						<div style="margin-top:6px;">
							<label>原因：</label>
							<span>
							<input type="text" id="reason<%=user.getNo() %>" style="width:70%" maxlength="50"/>
							</span>
						</div>
						<div>
							<input type="button" onclick="applyLeave(<%=user.getNo() %>,<%=user.getId() %>)" value="申         请" style="width:100%;height:30px;margin-top:15px;background-color:#81C0C0"/>
						</div>
					</div>
				</td>
				<td><a href="javascript:showCN(<%=user.getNo() %>)"><%=user.getName() %></a>
					<div class="changeName" id="cn<%=user.getNo() %>" >
						<div>
							<span style="font-weight:bold">姓名更改</span>
							<span style="float:right"><a href="javascript:hide('cn<%=user.getNo() %>')">关闭</a></span>
						</div>
						<div style="margin-top:10px;">
							<label>姓名：</label>
							<span>
							<input type="text" id="name<%=user.getNo() %>" style="width:70%" maxlength="50"/>
							</span>
						</div>
						<div>
							<input type="button" onclick="changeName(<%=user.getNo() %>)" value="更        改" style="width:100%;height:30px;margin-top:15px;background-color:#81C0C0"/>
						</div>
					</div>
				</td>
				<td><%=Dic.batchesById.get(user.getBatch()) %></td>
				<%while(!scl.getTime().after(ed)){ 
					String nowDay = sdf.format(scl.getTime());
				%>
				
				<td>
					<% if(user.getRecords().containsKey(nowDay)){ 
						Collections.sort(user.getRecords().get(nowDay));
						for(Record record:user.getRecords().get(nowDay)){
					%>
						<%="<span class='detail'" %>
						<%if(Dic.recordTypes.get("请假")==record.getRecord_type()){ %>
						<%="style='background-color:#ff7575'" %>
						<%=("title='"+record.getComment()+"'") %>
						<%} %>
						<%=">" %>
						<%=(new SimpleDateFormat("HH:mm").format(record.getTime())) %>
						<%="</span>" %>
					<%	}
					} %>
				</td>
				
				<%
					scl.add(Calendar.DAY_OF_MONTH,1);
				} %>
			</tr>
			<%} %>
		</table>
	<%} %>
	</div>
</body>
<script src="js/ajaxFunc.js" ></script>
<script>
	function exchange(){
		var detail = document.getElementById("detail");
		var sta = document.getElementById("sta");
		if(detail.style.backgroundColor == "white"){
			detail.style.backgroundColor = "#ADADAD";
		}else{
			detail.style.backgroundColor = "white";
			
		}
		if(sta.style.backgroundColor == "white"){
			sta.style.backgroundColor = "#ADADAD";
		}else{
			sta.style.backgroundColor = "white";
		}
		
	}
	
	function showAL(applyNo){
		hideAllEdit();
		var applyDiv = document.getElementById("apply"+applyNo);
		applyDiv.style.display = "block";
	}
	
	function showCN(cnNo){
		hideAllEdit();
		//姓名赋值
		document.getElementById("name"+cnNo).value = document.getElementById("cn"+cnNo).previousElementSibling.innerHTML;
		
		var cnDiv = document.getElementById("cn"+cnNo);
		cnDiv.style.display = "block";
	}
	
	function hideAllEdit(){
		
		var applys = document.getElementsByClassName("applyLeave");
		for(i=0;i<applys.length;i++){
			applys[i].style.display = "none";
		}
		var cns = document.getElementsByClassName("changeName");
		for(i=0;i<cns.length;i++){
			cns[i].style.display = "none";
		}
	}
	function hide(id){
		document.getElementById(id).style.display="none";
	}
	
	function applyLeave(no,id){
		var lsd = document.getElementById("lsd"+no).value;
		var led = document.getElementById("led"+no).value;
		var reason = document.getElementById("reason"+no).value;
		if(lsd == "" || led == "" || reason == ""){
			alert("请假起始时间与请假理由都不能为空！");
		}
		ajaxFunc("POSt","<%=request.getContextPath() %>/ApplyLeave",true,"lsd="+lsd+"&led="+led+"&reason="+reason+"&id="+id+"&curtime="+(new Date().getTime()),function(response){
			if(response == "SUCCESS"){
				//刷新页面
				document.forms[0].submit();
			}
		});
	}
	
	function changeName(no){
		var val = document.getElementById("name"+no).value;
		ajaxFunc("POSt","<%=request.getContextPath() %>/ChangeName",true,"val="+val+"&no="+no+"&curtime="+(new Date().getTime()),function(response){
			if(response == "SUCCESS"){
				//刷新页面
				document.forms[0].submit();
			}
		});
	}
	
	function exportdata(){
		if(document.getElementById("sd").value == "" || document.getElementById("ed").value == ""){
			alert("必须选择起始时间与结束时间！");
			return false;
		}
		document.forms[0].action = "ExportData";
		document.forms[0].submit();
	}
	
	function querydata(){
		if(document.getElementById("sd").value == "" || document.getElementById("ed").value == ""){
			alert("必须选择起始时间与结束时间！");
			return false;
		}
		document.forms[0].action = "ShowData";
		document.forms[0].submit();
	}
</script>
</html>