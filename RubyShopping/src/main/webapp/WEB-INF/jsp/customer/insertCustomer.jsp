<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Ruby Shopping Town</title>
</head>

<body>
<jsp:include page="../layouts/header.jsp" />

<center>
<form action="/customer/new" method="post">    
<table border="0" cellpadding="3" cellspacing="3" width="500">
	<tr>
		<td bgcolor="orange" width="100"><br>이름<br></td>
		<td><input type="text" name="name" size="20"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>도시<br></td>
		<td><input type="text" name="city" size="10"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>거리<br></td>
		<td><input type="text" name="roadName" size="50"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>우편번호<br></td>
		<td><input type="text" name="zipCode" size="20"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>전화번호<br></td>
		<td><input type="text" name="phone" size="20"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>회원 특징<br></td>
		<td><input type="text" name="comments" size="50"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<input type="submit" value="회원 가입"/></td>
	</tr>   		
</table>
</form>
</center>

<jsp:include page="../layouts/footer.jsp" />
</body>
</html>
