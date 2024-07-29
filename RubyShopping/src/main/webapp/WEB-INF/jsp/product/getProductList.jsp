<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Ruby Shopping Town</title>
</head>

<body>
<jsp:include page="../layouts/header.jsp" />
<center>

	<table border="1" cellpadding="0" cellspacing="0" width="800">
		<tr>
			<th bgcolor="orange" width="100"><br>순번<br>
			<br></th>
			<th bgcolor="orange" width="400">상품명</th>
			<th bgcolor="orange" width="100">가격</th>
			<th bgcolor="orange" width="100">재고수량</th>
			<th bgcolor="orange" width="100">정보 수정</th>
		</tr>

		<c:forEach var="product" items="${productList}">
		<tr>
			<td align="center">${product.id}</td>
			<td>${product.name}</td>
			<td align="center">${product.price}</td>
			<td align="center">${product.quantity}</td>
			<td align="center">
				<a href="/product/${product.id}/get">상품 수정</a>
			</td>

		</tr>
		</c:forEach>
	</table>
</center>

<jsp:include page="../layouts/footer.jsp" />
</body>
</html>
