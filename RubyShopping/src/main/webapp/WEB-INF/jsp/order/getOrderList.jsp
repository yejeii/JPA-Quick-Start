<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Ruby Shopping Town</title>
</head>

<body>
<jsp:include page="../layouts/header.jsp" />

<center>

<form action="getOrderList" method="post">
<table border="1" cellpadding="0" cellspacing="0" width="800">
	<tr>
	<td>
	회원명 : <input type="text" name="searchCustomerName"
			value="${searchInfo.searchCustomerName}" /> 
	주문상태 : <select name="searchOrderStatus">
	<option value="ORDER" <c:if test="${searchInfo.searchOrderStatus == 'ORDER'}">selected</c:if>>주문</option>
	<option value="CANCEL" <c:if test="${searchInfo.searchOrderStatus == 'CANCEL'}">selected</c:if>>취소</option></select>
	<input type="submit" value="검색" />
	</td>
	</tr>
</table>
</form>

<table border="1" cellpadding="0" cellspacing="0" width="800">
	<tr>
		<th bgcolor="orange" width="50"><br>순번<br><br></th>
		<th bgcolor="orange" width="150">회원명</th>
		<th bgcolor="orange" width="200">상품 이름</th>
		<th bgcolor="orange" width="100">주문수량</th>
		<th bgcolor="orange" width="100">상태</th>
		<th bgcolor="orange" width="100">일시</th>
		<th bgcolor="orange" width="100">주문 취소</th>
	</tr>

	<c:forEach var="order" items="${orderList}">
	<tr>
		<td align="center">${order.id}</td>
		<td align="center">${order.customer.name}</td>
		<td>${order.itemList[0].product.name}</td>
		<td align="center">${order.itemList[0].count}</td>
		<td align="center">
			<c:if test="${order.status == 'ORDER'}">주문</c:if>
			<c:if test="${order.status == 'CANCEL'}">취소</c:if></td>
		<td align="center">
			<fmt:formatDate value="${order.orderDate}"
				pattern="yyyy-MM-dd" /></td>
		<td align="center">
			<c:if test="${order.status == 'ORDER'}">
				<a href="/order/${order.id}/orderCancel">주문취소</a>
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>

</center>

<jsp:include page="../layouts/footer.jsp" />
</body>
</html>
