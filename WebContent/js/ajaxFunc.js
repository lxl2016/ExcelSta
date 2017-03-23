/**
 * ajaxFunc(get/post,目的url，是否异步，
 * @param method
 * @returns
 */
function ajaxFunc(method,dest,async,parameters,callback){
	
	//ajax调用
	var xmlHttp;
	if (window.XMLHttpRequest)
	{
	    //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
		xmlHttp=new XMLHttpRequest();
	}
	else
	{
	    // IE6, IE5 浏览器执行代码
		xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlHttp.open(method,dest,true);
    xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    xmlHttp.send(parameters);
    
    //ajax返回信息
    xmlHttp.onreadystatechange=function()
    {
    if (xmlHttp.readyState==4 && xmlHttp.status==200)
      {
    	callback(xmlHttp.responseText);
      }
    }
}