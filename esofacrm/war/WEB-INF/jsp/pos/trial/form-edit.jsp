<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="addformwrapper">
<div id="title">
	<h1>
		<div id="icon-tools" class="icon32"></div>
		<span style="float: left; margin-left: 5px; margin-top: 6px;">POS</span>
	</h1>
</div>
<div style="clear: both" />
<h2>Add Machine to Trial/Rental Pool</h2>
<p class="pageDesc">Use the product barcode to search for the product you wish to add the trial pool.  When initially setting up a new machine, there is no need to enter customer information.  When the customer borrows a machine, please add the customer to this record.  Also, ensure that the location is set properly.  When the customer returns a machine, remove the customer name, and then ensure that the location machines matches it's current location.</p>
<p class="pageDesc">Remember to hit the orange 'refresh' arrows after typing in the barcode</p>
<br/>
<form:form id="trialItem-form" commandName="trialItemForm"
	action="/pos/trial/formsubmit" method="POST">
	
	<form:errors path="*" cssClass="errorblock" element="div" />
	<c:if test="${message!=null}">
			<div class="messageblock">${message}</div>
	</c:if>
	<form:hidden path="mode"/>
	<form:hidden path="trialItem.id" />
	<form:hidden path="trialItem.product" />
	<ul>
		<li><label>Product Barcode:<span style="color:red">*</span></label>
		
		<c:if test="${trialItemForm.trialItem.id == null }">
		 <form:input path="productBarCode"  />
			<input type="submit" name="actionType" value="refresh"
			class="img-submit-refresh" style="background: url(/images/reload_icon.png) no-repeat; width: 16px; height: 16px;" />
		</c:if>
		<c:if test="${trialItemForm.trialItem.id != null }">
			<form:hidden path="productBarCode"/>
			<c:out value="${trialItemForm.productBarCode}"/>
		</c:if>
		
		</li>		
		<li><label>Product:</label><c:out value="${productMap[trialItemForm.trialItem.product].name}" /></li>
		<li><label>Product Serial:<span style="color:red">*</span></label>
			<c:if test="${trialItemForm.trialItem.id == null }">
				 <form:input path="trialItem.serialNumber" />
			</c:if>
			 <c:if test="${trialItemForm.trialItem.id != null }">
				<form:hidden path="trialItem.serialNumber"/>
				<c:out value="${trialItemForm.trialItem.serialNumber}"/>
			</c:if>
		</li>
		<li><label>Location:</label>
				<form:select path="trialItem.location">	
					<form:options items="${shops}" itemLabel="name" />
				</form:select>&nbsp;		
		</li>
		<li><label>Item Status</label>
			<form:select path="trialItem.trialStatus">	
				<form:options items="${trialStatuses}"/>
			</form:select>&nbsp;		
		</li>
	</ul>
	<c:if test="${trialItemForm.trialItem.id != null }">
	<ul>	
		<li><label>Customer:<span style="color:red">*</span></label> <form:input path="trialItem.customerFullName" id="customerSearch"  style="width: 200px;"/>
			<form:hidden path="customerId" id="trialItemCustomerId"/>
			
			<input type="submit" name="actionType" value="deleteCustomer" class="img-submit"							
				style="background: url(/images/trash.gif) no-repeat; width: 16px; height: 16px;" />			
		</li>	
		<li>
			<label>Health Card Number:</label><form:input path="healthCardNumber" id="healthCardNumber" disabled="true" style="width: 208px;"/>
		</li>
		<li><label>Last Updated :</label>
		<form:hidden path="trialItem.lastUpdatedDate"/>
		<c:out value="${trialItemForm.trialItem.lastUpdatedDate}" /></li>		
	</ul>
	</c:if>
	<br /><br />
	<c:if test="${trialItem.id != null }">
		<a href="/pos/trial/view?id=${trialItem.id}" style="color:#FFF;"  class="bluebtn">Cancel</a>&nbsp;&nbsp; 
	</c:if>
	<input type="submit" name="actionType" value="Save" class="bluebtn" />
</form:form>
<link rel="stylesheet" type="text/css" href="/css/autocomplete.css" />
<script >
$(function() {
	 
    $("#customerSearch").autocomplete({
        source: "/pos/trial/customerSearch",
        minLength: 2,
        select: function(event, ui) {

        	event.preventDefault();
        	$("#trialItemCustomerId").val(ui.item.id);
			$("#healthCardNumber").val(ui.item.fullHealthCardNumber);
            $(this).val(ui.item.value);
        },
 
        html: false, // optional (jquery.ui.autocomplete.html.js required)
 
      // optional (if other layers overlap autocomplete list)
        open: function(event, ui) {
            $(".ui-autocomplete").css("z-index", 1000);
        }
    });
 
});

		</script>
</div>
