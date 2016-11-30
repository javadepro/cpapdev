<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Product</span>
	</h1>
</div>
<div style="clear: both" />

<h2>Basic Form</h2>
<form:form commandName="product" action="/product/product-formsubmit"
	method="POST">
	<form:hidden path="id" />
	<ul>
		<li><label>Product Name:</label> <form:input path="name" /></li>
		<li><label>Type:</label> 
		<form:select path="productSubType">
				<c:forEach var="productType" items="${productTypeMap}" varStatus="x">
					<optgroup label="${productType.key.type}">
						<form:options items="${productType.value}" itemLabel="type" />
					</optgroup>
				</c:forEach>
			</form:select>
		<li><label>Description:</label> <form:input path="description" /></li>
		<li><label>Manufacturer:</label> <form:select path="manufacturer">
		 <form:options items="${manufacturers}" itemLabel="name"  />
		<%--<c:forEach var="manufacturer" items="${manufacturers}" varStatus="x">	
			<form:option value="manufacturer" label="${manufacturer.key.name}"   />
			</c:forEach>  --%>
		</form:select></li>
		<li><label>Product Bar Code:</label> <form:input
				path="productBarCode" /></li>
		<li><label>ADP Catalog Number:</label> <form:input
				path="adpCatalogNumber" /></li>
		<li><label>Reference Number:</label> <form:input
				path="referenceNumber" /></li>
		<li><label>Total Threshold Qty:</label> <form:input
				path="thresholdQty" /></li>
	</ul>
	
<input type="submit" value="Save Product Details" class="bluebtn"/>
</form:form>
<br />
<h2>Inventory</h2>
<form:form commandName="inventories" action="/product/inventory-formsubmit"
	method="POST">
<table class="listing">
	<tr>
		<th>Clinic</th>
		<th>Qty</th>
		<th>Threshold</th>
	</tr>
	<c:forEach var="shop" items="${shops}" varStatus="x">
		<tr>
			<td><c:out value="${shop.shortName}" /></td>
			<form:hidden path="inventories[${x.index}].id" />
			<form:hidden path="inventories[${x.index}].shop" />
			<form:hidden path="inventories[${x.index}].product" />
			<td><form:input path="inventories[${x.index}].qty" size="5"/></td> 
			<td><form:input path="inventories[${x.index}].threshold" size="5"/></td>
		</tr>
	</c:forEach>
</table>
<br />
<input type="submit" value="Save Inventory Information" class="bluebtn"/>
</form:form>
