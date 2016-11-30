<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>

<div id="home-page-wrapper">
<div id="title">
	<h1>
		<div id="icon-home" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">HOME</span>
	</h1>
</div>
<div style="clear: both" />
<table>
<tr>
<td>
<div class="home-block" style="width: 430px; height: 190px">
	<div>Customer Search</div>
	<div>
		<form:form id="customer-search-form" commandName="customer"
			action="/customer/search/formsubmit" method="POST">
			<form:hidden path="active" />
			<form:hidden path="phoneOffice" />
						<form:hidden path="phoneMobile" />
			
			<ul>
				<li><label>First Name:</label> <form:input path="firstname" /></li>
				<li><label>Last Name</label> <form:input path="lastname" /></li>
				<li><label>Health Card Number</label> <form:input
						path="healthCardNumber" title="xxxx xxx xxx" /></li>
				<li><label>Phone (Home)</label> <form:input path="phoneHome" title="xxx-xxx-xxxx"/></li>
			</ul>
			<br />
			<input type="submit" class="bluebtn" value="Search" />
		</form:form>
	</div>
</div>
</td>
<c:if test="${isCpapMode == 'true' }">	
<td rowspan="2">		

<div class="home-block" style="width: 200px;">
	<div>Inventory Management</div>
	<div>
		<ul style="list-style: none;">
			<c:forEach var="shop" items="${shopMap}" varStatus="x">

				<li style="padding: 5px;"><a
					href="/product/list?shopId=${shop.value.id}" class="open-on-tab"
					style="color: #333">${shop.value.name}</a></li>

			</c:forEach>
		</ul>
	</div>
</div>

</td>
</c:if>
</tr>

<c:if test="${isCpapMode == 'true' }">			
<tr>
<td>
<div class="home-block" style="width: 430px">
	<div>Product Search</div>
	<div>
		<!--  -->
		<form:form id="product-search-form" commandName="product"
			action="/product/search/formsubmit" method="POST">

			<ul>
				<li><label>Product Name:</label> <form:input path="name" /></li>
				<li><label>Barcode:</label> <form:input path="productBarCode" /></li>				
				<li><label>Reference Number:</label> <form:input path="referenceNumber" /></li>			
				
				<li><label>Type:</label> <form:select path="productSubType">
						<form:option value="" label="--" />
						<c:forEach var="productType" items="${productTypeMap}"
							varStatus="x">
							<optgroup label="${productType.key.type}">
								<form:options items="${productType.value}" itemLabel="type" />
							</optgroup>
						</c:forEach>
					</form:select></li>
			</ul>
			<br />
			<input type="submit" value="Search" class="bluebtn" />
		</form:form>
		<!--  -->
	</div>
</div></td>
</tr>
</c:if>
<tr>
<td>
<div class="home-block" style="width: 430px">
	<div>Invoice Search</div>
	<div>
		<!--  -->
		<form:form id="invoiceSearchForm" commandName="invoiceSearchForm"
			action="/pos/invoice-search" method="POST">

			<ul>
				<li><label>Invoice Number:</label> <form:input path="invoiceNumber" /></li>
				<li>or&nbsp;</li><br/>				
				<li><label>Period:</label>
				<form:select path="numMths">
						<form:options items="${periodFilter}" />
				</form:select>&nbsp;				
				</li>				
			</ul>
			<br />
			<input type="submit" value="Search" class="bluebtn" />
		</form:form>
		<!--  -->
	</div>
</div></td>
</tr>
</table>





<div style="clear: both" />

<script>

$('input[title]').inputHints();
	setupAjaxFormReplace("customer-search-form", "home-page-wrapper");
	setupAjaxFormReplace("product-search-form", "home-page-wrapper");
</script>

</div>