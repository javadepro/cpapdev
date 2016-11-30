<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
		<c:if test="${sessionScope.companyMode == 'CPAPDIRECT' }">
		<c:set var="isCpapMode">true</c:set>
	</c:if>
	
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">Product</span>
	</h1>
</div>
<div style="clear: both" />
<h2>Basic Form</h2>
<p  class="pageDesc">Barcodes are suppose to be unique within the system. </p>
<p class="pageDesc" >BarCodes with format "CPAP-nnnnnnnn" are reserved for auto barcode generation and should be avoided if you are manually entering one </p>

<div id="product-info">
<%@ include file="form-product.jsp" %>
</div>
<br />

<c:if test="${isCpapMode == 'true' }">			
<sec:authorize access="hasRole('ROLE_INVENTORY_ADMIN')">
<h2>Inventory</h2>
<div id="inventory-info">
<%@ include file="form-inventory.jsp" %>
</div>
</sec:authorize>
</c:if>