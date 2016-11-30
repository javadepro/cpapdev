<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Product</span>
	</h1>
</div>
<br />
<c:choose> 
<c:when test="${shopId==null||shopId==0}">
<c:forEach var="shop" items="${shopMap}" varStatus="x">
<a href="/product/list?shopId=${shop.value.id}" class="bluebtn open-on-tab">${shop.value.name}</a>&nbsp;
</c:forEach>
</c:when>
<c:otherwise>
<a href="/product/list" class="open-on-tab bluebtn">Show All</a>
</c:otherwise>
</c:choose>
<br /><br />
<div style="clear: both" />

	<table class="listing">
	<tr>
		<th wstyle="width:300px;">Name</th>
		<th style="width:150px;">Barcode</th>
		<th style="width:150px;">Reference Number</th>
		<c:forEach var="shop" items="${shopMap}" varStatus="x">
			<th><c:out value="${shop.value.shortName}" /></th>
		</c:forEach>
		<th>Trn</th>
		<c:if test="${shopId==null||shopId==0}">
		<th>Total</th>
		</c:if>
		<th >Action</th>
	</tr>
	<c:forEach var="product" items="${productMap}">
		<tr>
			<td><a href="/product/view?id=<c:out value="${product.key.id}" />" style="color:#333"><c:out value="${product.key.name}" /></a></td>
			<td><c:out value="${product.key.productBarCode}" /></td>
			<td><c:out value="${product.key.referenceNumber}" /></td>
			<c:set var="inventoryTotal" value="0" />
			<c:forEach var="shop" items="${shopMap}" varStatus="x">
				<c:choose>
					<c:when
							test="${product.value[shop.key].qty<product.value[shop.key].threshold}">
					<td style="color:red"><c:out
									value="${product.value[shop.key].qty}" />(<c:out
									value="${product.value[shop.key].threshold}" />)</td>
					</c:when>
					<c:otherwise>
					<td><c:out value="${product.value[shop.key].qty}" /></td>
					</c:otherwise>
					</c:choose>
				<c:set var="inventoryTotal"
						value="${inventoryTotal + product.value[shop.key].qty}" />
			</c:forEach>
			
			<!-- inventory tranfers -->
			<td><c:out value="${inventoryTransferQtyMap[product.key]}"/>
				<c:set var="inventoryTotal" value="${inventoryTotal + inventoryTransferQtyMap[product.key] }"/>
			</td>
			
			<c:if test="${shopId==null||shopId==0}">
			<c:choose>
					<c:when test="${inventoryTotal<product.key.thresholdQty}">
						<td style="color:red"><c:out value="${inventoryTotal}" />(${product.key.thresholdQty})</td>
					</c:when>
					<c:otherwise>
						<td><c:out value="${inventoryTotal}" /></td>
					</c:otherwise>
				</c:choose> 
			</c:if>
			<td><a href="/product/view?id=<c:out value="${product.key.id}" />" style="color:#333">View</a>&nbsp;|&nbsp;
			<a href="/product/form?id=<c:out value="${product.key.id}" />"
					class="open-on-tab" style="color:#333">Edit</a></td> 
			
		</tr>
	</c:forEach>
	
</table>
<br />
<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">
<a href="/product/form" class="bluebtn">Add a new product</a>
</sec:authorize>
<br />
<script>
	$(".open-on-tab").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});

</script>

