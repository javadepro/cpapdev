<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
<form:form id="product-form" commandName="product"
	action="/product/product-formsubmit" method="POST">
	
	<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		
	<form:hidden path="id" />
	
	<ul>
		<li><label>Product Name:</label> <form:input path="name" /></li>
		<li><label>Type:</label> <form:select path="productSubType">
				<c:forEach var="productType" items="${productTypeMap}" varStatus="x">
					<optgroup label="${productType.key.type}">
						<form:options items="${productType.value}" itemLabel="type" />
					</optgroup>
				</c:forEach>
			</form:select>
		</li>
		<li><label>Price (ADP/Tier1):</label> <form:input path="price" /></li>
		<li><label>Price (Return Customer/Tier2):</label> <form:input path="priceNonAdp" /></li>
		<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">
			<li><label>Default Inventory Cost:</label> <form:input path="defaultCost" /></li>
		</sec:authorize>
		<li><label>HST Applies:</label> <form:checkbox path="hstApplicable" /></li>
		<li><label>Description:</label> <form:textarea cols="60"
				rows="5" path="description" size="40"
				cssErrorClass="form-error-field" /></li>
		<li><label>Manufacturer:</label> <form:select path="manufacturer">
				<form:options items="${manufacturers}" itemLabel="name" />
			</form:select></li>
		<li><label>Product Bar Code:</label> <form:input
				path="productBarCode" /></li>
		<li><label>ADP Catalog Number:</label> <form:input
				path="adpCatalogNumber" /></li>
		<li><label>Reference Number:</label> <form:input
				path="referenceNumber" /></li>
		<c:if test="${isCpapMode == 'true' }">
		<li><label>Total Threshold Qty:</label> <form:input path="thresholdQty" /></li>
		</c:if>
		<li><label>Currently Active Product:</label> <form:checkbox
				path="isActive" /></li>				
	</ul>
	<br /><br />
	<input type="submit" value="Save Product Details" class="bluebtn" />
	&nbsp; &nbsp;
	<a href="/product/view?id=<c:out value="${product.id}" />" class="bluebtn">View</a>
	

	<script>
	$(".open-on-same-page").click(function(e) {
		e.preventDefault();
		var url = $(this).attr('href');
		var title = $(this).attr('title');
		$('#mainbody').load(url);
		return false;
	});
			setupAjaxForm("product-form");
		</script>
</form:form>
