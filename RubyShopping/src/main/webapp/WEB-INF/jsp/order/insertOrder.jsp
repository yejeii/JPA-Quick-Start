<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
    <title>Ruby Shopping Town</title>
</head>

<body>
<jsp:include page="../layouts/header.jsp" />

<center>
<form action="/order/new" method="post">
<table border="0" cellpadding="3" cellspacing="3" width="300">
	<tr>
		<td bgcolor="orange" width="100"><br>�ֹ�ȸ��<br></td>
		<td>
		<select name="customerId">
			<option>ȸ������</option>
			<c:forEach var="customer" items="${customerList}">
				<option value="${customer.id}">${customer.name}</option>
			</c:forEach>
		</select>
		</td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>�ֹ���ǰ<br></td>
		<td>
		<select name="productId">
			<option>��ǰ����</option>
			<c:forEach var="product" items="${productList}">
				<option value="${product.id}">${product.name}</option>
			</c:forEach>
		</select>
		</td>
	</tr>
	<tr>
		<td bgcolor="orange"><br>�ֹ�����<br></td>
		<td><input type="text" name="count"></td>
	</tr>
	<tr>
		<td colspan="2" align="center">
		<input type="submit" value="��ǰ �ֹ�"/></td>
	</tr>   		
</table>
</form>
</center>

<jsp:include page="../layouts/footer.jsp" />
</body>
</html>
