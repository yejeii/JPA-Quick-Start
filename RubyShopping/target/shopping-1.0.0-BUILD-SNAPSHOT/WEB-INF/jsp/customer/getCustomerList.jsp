<%@ page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<jsp:include page="../layouts/header.jsp" />

<center>

<table border="1" cellpadding="0" cellspacing="0" width="800">
	<tr>
		<th bgcolor="orange" width="50"><br>����<br><br></th>
		<th bgcolor="orange" width="100">�̸�</th>
		<th bgcolor="orange" width="70">����</th>
		<th bgcolor="orange" width="200">�ּ�</th>
		<th bgcolor="orange" width="80">�����ȣ</th>
		<th bgcolor="orange" width="150">��ȭ��ȣ</th>
		<th bgcolor="orange" width="200">ȸ��Ư¡</th>
	</tr>
	
	<c:forEach var="customer" items="${customerList}">
		<tr>
		<td align="center">${customer.id}</td>
		<td align="center">${customer.name}</td>
		<td>${customer.address.city}</td>
		<td>${customer.address.roadName}</td>
		<td align="center">${customer.address.zipCode}</td>
		<td align="center">${customer.phone}</td>
		<td>${customer.comments}</td>
		</tr>
	</c:forEach>	
</table>
</center>

<jsp:include page="../layouts/footer.jsp" />

