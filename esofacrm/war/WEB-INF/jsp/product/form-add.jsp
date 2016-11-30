<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
	<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
<div id="addformwrapper">
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Product</span>
	</h1>
</div>
<div style="clear: both" />
<h2>Basic Form</h2>
<p class="pageDesc" >When adding your product, make sure that you include a bar code.  Barcodes are suppose to be unique within the system.  If your Product does not have a barcode with it, you can have the system generate one for you by selecting "Generate Bar Code". </p>
<p class="pageDesc" >BarCodes with format "CPAP-nnnnnnnn" are reserved for auto barcode generation and should be avoided if you are manually entering one </p>
<form:form id="product-form" commandName="product"
	action="/product/add-formsubmit" method="POST">
	
	<form:errors path="*" cssClass="errorblock" element="div" />
	<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
	</c:if>
	
	<form:hidden path="id" />
	
	<ul>
		<li><label>Product Name:<span style="color:red">*</span></label> <form:input path="name" /></li>
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
		<li><label>Product Bar Code:<span style="color:red">*</span></label><form:input path="productBarCode" /></li>
		<li><label>Generate Bar Code:</label><form:checkbox path="generateBarCode" /></li>
		<li><label>ADP Catalog Number:</label> <form:input
				path="adpCatalogNumber" /></li>
		<li><label>Reference Number:</label> <form:input
				path="referenceNumber" /></li>
		<c:if test="${isCpapMode == 'true' }">
		<li><label>Total Threshold Qty:</label> <form:input path="thresholdQty" /></li>
		</c:if>
				<!--  no point adding isActive here..when i'm creating a new product -->
	</ul>
	<br /><br />
	<input type="submit" value="Save Product Details" class="bluebtn" />
</form:form>
<script>
$('form').bind('submit', function(e) {
    $(this).find('input:submit').attr('disabled', 'disabled').attr('class','orangebtn').val("Working...");
});

			setupAjaxFormReplace("product-form","addformwrapper");
		</script>
</div>
