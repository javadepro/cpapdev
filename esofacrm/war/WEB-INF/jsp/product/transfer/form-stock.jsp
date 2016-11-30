<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="transferFormWrapper" style="display:hidden">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Inventory Add Stock</h2>
	<br />
<form:form id="transfer-form" commandName="inventoryTransferForm"
	action="/product/inventory/addstock-formsubmit" method="POST">
	
	<form:errors path="*" cssClass="errorblock" element="div" />
		<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
		</c:if>
		
	
	<ul>
		<li><label>Product:</label>
			<form:hidden path="product" />
			<c:out value="${productMap[inventoryTransferForm.product].name}"/>
		</li>
		<li><label>To Location:</label>
			<form:hidden path="toShop"/>
			<c:out value="${shops[inventoryTransferForm.toShop].name}"/>
		</li>
		<li><label>Cost:</label><form:input path="cost" class="numeric"/></li>
		<li><label>Quantity:</label><form:input path="qty" /></li>
	</ul>
	<br /><br />
	<input type="submit" value="Add Stock" class="bluebtn" />

<script>
setupAjaxFormReplace("transfer-form","transferFormWrapper");

$(document).ready(function(){
    $(".numeric").numeric();
});
</script>
</form:form>
</div>