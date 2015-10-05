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
<title>Re-ranked results</title>
</head>
	<h3 align="center"> * * * Poorva's search engine * * * </h3>
	<h3 align="center"> Re-ranked results </h3>
<body>
	<a href="HomePage.jsp"><p style="text-align:center">Home</p></a> 
	<table border="1" width="100%" align='center'>
	<tr>
		<th align='center'> Sr.no </th>
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
				document.write("<tr><td>"+ item.documentIndex + "</td>");
				document.write("<td>" + item.htmlTitle + "</td><td>" + item.htmlSnippet + "</td></tr>");
				"<br>";
			}
		</script>	
	</table>
	<a href="HomePage.jsp"><p style="text-align:center">Home</p></a> 
</body>
</html>