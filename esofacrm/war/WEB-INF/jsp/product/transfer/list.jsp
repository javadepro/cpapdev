<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<form:errors path="*" cssClass="errorblock" element="div" />
	<c:if test="${message!=null}">
		<div class="messageblock">${message}</div>
	</c:if>

<div id="transferRequest-list">
	<div id="title">
		<h1><div id="icon-bubble" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Product</span></h1>
	</div>
	<div style="clear:both" />
	<h2>Inventory Transfer Requests</h2>
	<br />
	<form:form id="inventory-transfer-filter" commandName="inventoryTransferSearch"
		action="/product/inventory/transfer-view" method="GET">

	<b>Receiving Clinic:</b>&nbsp;<form:select path="shop">
			<form:option value="" label="ALL" />
			<form:options items="${shops}" itemLabel="name" />
		</form:select>&nbsp;

		<input type="submit" value="Filter" id="refresh-btn" class="bluebtn" />
	</form:form>

	<br /><br />
<table class="listing">
	<tr>
		<th style="width:60px;">Date</th>
		<th>Product</th>
		<th  style="width:90px;">Ref Number</th>
		<th>Requested By</th>
		<th style="width:90px;">From</th>
		<th  style="width:90px;">To Store</th>
		<th>Qty</th>
		<th style="width:130px;">Action</th>
	</tr>
<c:forEach var="transfer" items="${transferRequests}" >
<tr>
	<td><fmt:formatDate value="${transfer.initDate}" type="DATE" pattern="dd/MM/yyyy"/></td>
	<td><a href="/product/view?id=${productMap[transfer.product].id}" style="color:#333;"><c:out value="${productMap[transfer.product].name}" /></a></td>
	<td><c:out value="${productMap[transfer.product].referenceNumber}"/></td>
	<td><c:out	value="${users[transfer.user].name}" /></td>
	<td><c:out value="${shopsAll[transfer.fromShop].name}" /></td>
	<td><c:out value="${shopsAll[transfer.toShop].name}" /></td>
	<td><c:out value="${transfer.qty}" /></td>
	 
<td>
	<a href="/product/inventory/transfer-accept?transferId=${transfer.id}&shopId=<spring:bind path="inventoryTransferSearch.shop"><c:out value="${inventoryTransferSearch.shop.id}" /></spring:bind>" style="color:#333">Accept</a>
	&nbsp;|&nbsp;
	
	<a href="/product/inventory/transfer-cancel?transferId=${transfer.id}&shopId=<spring:bind path="inventoryTransferSearch.shop"><c:out value="${inventoryTransferSearch.shop.id}" /></spring:bind>"  style="color:#333">Cancel</a>

</td>
</tr>
</c:forEach>
</table>
<br />
<%--
<input type="button" value="Dismiss" class="bluebtn" /><input type="button" value="Email" class="bluebtn" />
 --%>
<br />
<br />
<div id="access-deneid" title="Access Denied">
	<p>You do not have access to view or edit this customer. Please ask reception/admin to add to the exception list</p>
</div>
<script>
	$("#access-deneid").dialog({ autoOpen: false,modal: true });
	jQuery.fn.log = function (msg) {
	  console.log("%s: %o", msg, this);
	  return this;
	};
 
$(".open-on-tab").click(function(e){
	e.preventDefault();
	var url = $(this).attr('href');
	$('#mainbody').load(
			url,function(response, status, xhr) {
		  if (status == "error") {
			  	if(xhr.status==403){
			  		//alert($("#access-deneid"));
			  		$("#access-deneid").dialog("open");
			  	}
			  }
			});
	return false;
});


$(function (){
    $('#transferModal,a.modal').click(function() {
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
</div><!-- End div:id=alert-list -->
