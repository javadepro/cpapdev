<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ page session="true" %>
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
<h2>Basic Details</h2>
<form>
	<ul>
		<li><label>Product Name:</label> <c:out value="${product.name}" /></li>
		<li><label>Product Type:</label> <c:out
				value="${productTypeOnlyMap[productSubTypeMap[product.productSubType].parentType].type}" />
		</li>
		<li><label>Product SubType:</label> <c:out
				value="${productSubTypeMap[product.productSubType].type}" /></li>
		<li><label>Price (ADP/Tier1):</label> <c:out value="${product.price}" />&nbsp;</li>
		<li><label>Price (Return Customer/Tier2):</label> <c:out value="${product.priceNonAdp}" />&nbsp;</li>
		<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">
		<li><label>Default Inventory Cost:</label> <c:out value="${product.defaultCost}" />&nbsp;</li>
		</sec:authorize>
		<li><label>Description:</label> <textarea cols="60" rows="5"
				disabled="disabled"><c:out value="${product.description}" />
			</textarea></li>
		<li><label>Manufacturer:</label> <c:out
				value="${manufacturers[product.manufacturer].name}" /></li>
		<li><label>Product Bar Code:</label> <c:out
				value="${product.productBarCode}" />&nbsp;</li>
		<li><label>ADP Catalog Number:</label> <c:out
				value="${product.adpCatalogNumber}" />&nbsp;</li>
		<li><label>Reference Number:</label> <c:out
				value="${product.referenceNumber}" />&nbsp;</li>
		<c:if test="${isCpapMode == 'true' }">
		<li><label>Total Threshold Qty:</label> <c:out
				value="${product.thresholdQty}" />&nbsp;</li>
		</c:if>
	</ul>
	<br />
			
			
	<c:if test="${isCpapMode == 'true' }">
	<h2>Inventory</h2>
	<table class="listing">
		<tr>
			<th>Clinic</th>
			<th>Qty</th>
			<th>Threshold</th>
		</tr>
		<c:forEach var="shop" items="${shops}" varStatus="x">
		<tr>
			<td><c:out value="${shop.shortName}" /></td>
			<td><c:out value="${inventories.inventories[x.index].qty}"/></td> 
			<td><c:out value="${inventories.inventories[x.index].threshold}"/></td>
		</tr>
	</c:forEach>
		<tr>
			<td><b>Total</b></td>
			<td><c:out value="${inventories.inventoryTotal}"/></td> 
			<td>&nbsp;</td>
		</tr>
	
	
	</table>
	</c:if>
	<br /> 
	
	<a href="/product/form?id=<c:out value="${product.id}" />"class="bluebtn open-on-same-page">Edit</a>&nbsp;&nbsp;
	<a href="/product/view?id=<c:out value="${product.id}" />"class="bluebtn">Refresh</a>&nbsp;&nbsp;
	
	<c:if test="${isCpapMode == 'true' }">	
	<a href="/product/inventory/transfer-form?productId=<c:out value="${product.id}" />" id="transferModal"  class="bluebtn"  style="text-decoration:none;" title="Transfer">Transfer</a>&nbsp;&nbsp;	
	<sec:authorize access="hasRole('ROLE_INVENTORY_ADMIN')">					
	<a href="/product/inventory/addstock-form?productId=<c:out value="${product.id}" />" id="stockModal"  class="bluebtn" title="Add Stock">Add Stock</a>	
	</sec:authorize>
	</c:if>

	<script>
		$(".open-on-same-page").click(function(e) {
			e.preventDefault();
			var url = $(this).attr('href');
			var title = $(this).attr('title');
			$('#mainbody').load(url);
			return false;
		});

		$(function (){
		    $('#transferModal,#stockModal').click(function() {
		        var url = this.href;
		        var title = this.title;
		        // show a spinner or something via css
		        var dialog = $('<div class="loading">&nbsp;</div>').appendTo('body');
		        // open the dialog
		        dialog.dialog({
		            // add a close listener to prevent adding multiple divs to the document
		            close: function(event, ui) {
		                // remove div with all data and events
		                dialog.log("close");
		                refreshList();
		                dialog.remove();
		            },
		            modal: true,
		            title: title,
		            width: "auto",
		            height: "auto",
		        });
		        // load remote content
		        dialog.load(
		            url, 
		         	 // omit this param object to issue a GET request instead a POST request, otherwise you may provide post parameters within the object
		            function (responseText, textStatus, XMLHttpRequest) {
		                // remove the loading class
		                dialog.removeClass('loading');
		            }
		        )
		       
		        //prevent the browser to follow the link
		        return false;
		    });
		});

		
	</script>