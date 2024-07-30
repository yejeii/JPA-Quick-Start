<%@ page contentType="text/html; charset=EUC-KR" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Ruby Shopping Town</title>
</head>

<body>
<jsp:include page="../layouts/header.jsp" />

<center>
<form action="/product/new" method="post">
<table border="0" cellpadding="3" cellspacing="3" width="300">
	<tr>
		<td bgcolor="orange" width="100"><br>상품명<br><br></td>
		<td><input type="text" name="name"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>가격<br><br></td>
		<td><input type="text" name="price"></td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>수량<br><br></td>
		<td><input type="text" name="quantity"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<input type="submit" value="상품 등록"/></td>
	</tr>   		
</table>
</form>
</center>

<jsp:include page="../layouts/footer.jsp" />
</body>
</html>
