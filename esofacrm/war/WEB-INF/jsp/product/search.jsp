<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<form:form id="search-form" commandName="product" action="/product/search/formsubmit" method="POST" >

<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product Search</span></h1>
</div>
<div style="clear:both" />
<p class="pageDesc">Use this page to search for products.  The best way to search is by using the barcode.  Scan the product and hit search.  For searching by product name, it is not a case sensitive search.  You can do a wildcard match in any of the fields by using a * (ie Product: Some Machi*).</p>
<p class="pageDesc">To search products that are discontinued, uncheck the "Search Active Only" checkbox.</p>
<h2>Search Criteria</h2>
<form:errors path="*" cssClass="errorblock"/>
<ul>
<li><label>Product Name:</label><form:input path="name" /></li>
<li><label>Barcode:</label><form:input path="productBarCode" /></li>
<li><label>Reference Number:</label><form:input path="referenceNumber" /></li>
<li><label>Type:</label> <form:select path="productSubType">
				<form:option value="" label="--" />
				<c:forEach var="productType" items="${productTypeMap}" varStatus="x">
					<optgroup label="${productType.key.type}">
						<form:options items="${productType.value}" itemLabel="type" />
					</optgroup>
				</c:forEach>
			</form:select>
</li>
<li><label>Search Active Only</label><form:checkbox path="isActive" /></li>

</ul>
<br />
<input type="submit" value="Search" class="bluebtn"  />

<c:choose>
<c:when test="${productMap!=null}">
<h2>Search Result</h2>
<table class="listing">
	<tr>
	<!-- 	<th  class="minorinfo">Id</th> -->
		<th style="width: 120px;">Name</th>
		<th style="width: 90px;">Barcode</th>
		<th style="width: 90px;">Reference Number</th>
		<c:forEach var="shop" items="${shopMap}" varStatus="x">
			<th><c:out value="${shop.value.shortName}" /></th>
		</c:forEach>		
		<th>Trn</th>
		<th>Total</th>
		<th style="width: 80px;">Action</th>
	</tr>
	<c:forEach var="product" items="${productMap}">
		<tr>
		<!-- <td  class="minorinfo"><c:out value="${product.key.id}" /></td> -->
			<td><a href="/product/view?id=<c:out value="${product.key.id}" />" style="color:#333;" title="${product.key.name}"><c:out value="${product.key.name}" /></a></td>
			<td>${product.key.productBarCode}</td>
			<td>${product.key.referenceNumber}</td>
			<c:set var="inventoryTotal" value="0" />
			<c:forEach var="shop" items="${shopMap}" varStatus="x">
				<c:choose>
					<c:when
						test="${product.value[shop.key].qty<product.value[shop.key].threshold}">
					<td style="color:red"><c:out value="${product.value[shop.key].qty}" />(<c:out value="${product.value[shop.key].threshold}" />)</td>
					</c:when>
					<c:otherwise>
					<td><c:out value="${product.value[shop.key].qty}" /></td>
					</c:otherwise>
					</c:choose>
				<c:set var="inventoryTotal" value="${inventoryTotal + product.value[shop.key].qty}" />
			</c:forEach>

			<!-- inventory transfers -->
			<td style="color:green; font-weight: bold;"> 
				<c:choose>
					<c:when test="${inventoryTransferQtyMap[product.key.id]!= null}">
						<c:out value="${inventoryTransferQtyMap[product.key.id]}"/>
						<c:set var="inventoryTotal" value="${inventoryTotal + inventoryTransferQtyMap[product.key.id]}" />
					</c:when>										
					<c:otherwise>0
					</c:otherwise>
				</c:choose>
			</td>
			
			<c:choose>
					<c:when
						test="${inventoryTotal<product.key.thresholdQty}">
						<td style="color:red"><c:out value="${inventoryTotal}" />(${product.key.thresholdQty})</td>
					</c:when>
					<c:otherwise>
						<td><c:out value="${inventoryTotal}" /></td>
					</c:otherwise>
				</c:choose> 
			<td>

				<a href="/product/inventory/transfer-form?productId=<c:out value="${product.key.id}" />" id="transferModal"  style="color:#333;text-decoration:none;" title="Transfer">Trnfer</a>
				<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">				
				&nbsp;|&nbsp;
				<a href="/product/inventory/addstock-form?productId=<c:out value="${product.key.id}" />" id="stockModal"  style="color:#333;text-decoration:none;" title="Add Stock">Stock</a>				
				</sec:authorize>
			</td> 
			
		</tr>
	</c:forEach>
	
</table>
<br />
<sec:authorize access="hasRole('ROLE_SUPER') or hasRole('ROLE_INVENTORY_ADMIN')">
<a href="/product/add" class="bluebtn open-on-tab">Add</a>
</sec:authorize>
</c:when>
<c:when test="${products==null}">
<p>No Result!</p>
</c:when>
</c:choose>
</form:form>

<script>

$(".open-on-tab").click(function(e) {
	e.preventDefault();
	var url = $(this).attr('href');
	var title = $(this).attr('title');
	$('#mainbody').load(url);
	return false;
});
setupAjaxForm("search-form");

$(function (){
    $('#transferModal,a.modal,#stockModal').click(function() {
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

