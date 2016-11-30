<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="wrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Edit Invoice</span></h1>
</div>
<div style="clear:both; width:500px;" />
<form:form id="invoice-edit" commandName="invoice"
	action="/pos/invoice-edit-formsubmit" method="POST">
	<form:hidden path="id" />	
	<ul>
		<li><label>Invoice Number: </label><c:out value="${invoice.invoiceNumber }"/></li>
		<li><label>Prepared by:</label>
		
			<form:select path="userName">
				<form:option value="" />
				<form:options items="${clinicianUsers}" itemLabel="name" itemValue="name" />
			</form:select>
		</li>
		<li><label>Machine Warranty: </label>
			<form:select path="machineWarranty"><form:options items="${machineWarranties}" itemLabel="name" itemValue="value" /></form:select> 
		</li>
		<li><label>Payment Method: </label>
			<form:select path="paymentMethod"><form:options items="${paymentMethods}" itemLabel="name" itemValue="value" /></form:select> 
		</li>	
		<li><label>Internal Note: </label>
			<form:textarea path="discountNote"  maxlength="100"  /> 
		</li>		
	</ul>
	
	<br /><br />
			<input type="submit" value="update" class="bluebtn" />

</form:form>
<script>
setupAjaxFormReplace("invoice-edit","wrapper");
</script>
</div>