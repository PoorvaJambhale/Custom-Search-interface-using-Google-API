<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
body{
	font-family:"Arial";
	background-color:#FFFFC6;
	}
</style>
<title>Home Page</title>
<script type="text/javascript">
	
	function validateForm() {	
		var query = document.forms["queryForm"]["keyword"].value;	
		if (query == null || query == "") {
	    	alert("Must enter query!");	
	    	return false;
	    }
	}
</script>	    
</head>
<body>
<h3 align="center"> * * * Poorva's search engine * * * </h3>
<form  name="queryForm" method="post" action="API" onsubmit="return validateForm()">
<table align="center">
	<tr></tr>
	<tr></tr>
	<tr></tr>
	<tr>	
		<td>Search keyword: <input type="text" name='keyword' size="50"><br></td>
		<td><input type="submit" value="Search"/></td>
	</tr>
</table>
</form>
</body>
</html>