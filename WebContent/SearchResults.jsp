<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
body{
	font-family:"Arial";
	background-color:#FFFFC6;
	}
h1{
	color:black;
	text-align:center;
}
h4 {
	color:black;
	text-align:center;
}
p {
	font-family:"Arial";
	font-size:20px;
}
th {
	background-color:#C4C4C4;
	color:black;
}
td {
	height:50px;
	vertical-align:bottom;
	text-align:left;
	padding:15px;
}
</style>
<title>Search results</title>
<script type="text/javascript">
	
	function validateForm() {	
		var len = (document.querySelectorAll('input[type="checkbox"]:checked').length);
		if (len < 1 || len > 5) {
	    	alert("Please select 1 ~ 5 relevant search results!");	
	    	return false;
	    } else {
	    	return true;
	    }
		
	}
</script>	
</head>
	<h3 align="center"> * * * Poorva's search engine * * * </h3>
	<h3 align="center"> Search results </h3>
	<h3 align="center"> Select 1 ~ 5 relevant search results and click "Re-rank".</h3>
<body>
	<form  name="feedbackForm" method="post" action="Rerank" onsubmit="return validateForm()">
	<div style="text-align:center">  
    	<input type="submit" value="Re-rank" />  
	</div>
	<br>
	<table border="1" width="100%" align='center'>
	<tr>
		<th align='center'> Sr.no </th>
		<th align='center'> Select </th>
		<th align='center'> Title </th>
		<th align='center'> Snippet </th>
	</tr>
		<script type="text/javascript">  
			var item;
			var sno;
			var response = <%=(String)request.getAttribute("todo")%>
			for (var i = 0; i < response.items.length; i++) {
				item = response.items[i];
				sno = i + 1;
				document.write("<tr><td>"+ sno + "</td>");
				document.write("<td><input name=\"checkbox_" + sno + "\" type=\"checkbox\" id= \""+ sno +"\" name= \"cb\" value=\""+ sno +"\"></td>");
				document.write("<td>" + item.htmlTitle + "</td><td>" + item.htmlSnippet + "</td></tr>");
				"<br>";
			}
		</script>	
	</table>
	<br>
	<div style="text-align:center">  
    	<input type="submit" value="Re-rank"/>  
	</div>  
	</form>
</body>
</html>