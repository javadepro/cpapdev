<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<table class="listing">
	<tr>
		<th>Id</th>
		<th>Name</th>
		<th>Type</th>
		<c:forEach var="shop" items="${shops}" varStatus="x">
			<th><c:out value="${shop.shortName}" /></th>
		</c:forEach>
		<th>Total</th>
		<th>Action</th>
	</tr>
	<c:forEach var="product" items="${products}">
		<tr>
			<td><c:out value="${product.id}" /></td>
			<td><c:out value="${product.name}" /></td>

			<td><c:out value="${subTypeName[product.productSubType]}" /></td>
			<c:set var="inventoryTotal" value="0" />
			<c:forEach var="shop" items="${shops}" varStatus="x">
			  <c:set var="inventoryTotal" value="${inventoryTotal + product.inventoriesMap[shop.id].qty}" />
				<c:choose>
					<c:when
						test="${product.inventoriesMap[shop.id].qty<product.inventoriesMap[shop.id].threshold}">
						<td style="color:red"><c:out value="${product.inventoriesMap[shop.id].qty}" /></td>
					</c:when>
					<c:otherwise>
						<td><c:out value="${product.inventoriesMap[shop.id].qty}" /></td>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<c:choose>
					<c:when
						test="${inventoryTotal<product.thresholdQty}">
						<td style="color:red"><c:out value="${inventoryTotal}" /></td>
					</c:when>
					<c:otherwise>
						<td><c:out value="${inventoryTotal}" /></td>
					</c:otherwise>
				</c:choose>
			<td><a href="/product/form?id=<c:out value="${product.id}"/>">Edit</a></td>
		</tr>
	</c:forEach>
</table>
<a href="/product/form">Add</a>
<br />
<c:forEach var="shop" items="${shops}" varStatus="x">
<a href="/product/list?shopId=${shop.id}" >${shop.name}</a>&nbsp;|&nbsp;
</c:forEach>

