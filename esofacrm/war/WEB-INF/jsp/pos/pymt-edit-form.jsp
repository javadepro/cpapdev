<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div id="wrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Edit Payment</span></h1>
</div>
<div style="clear:both; width:500px;" />
<form:form id="pymt-form" commandName="invoicePayment"
	action="/pos/pymt-formsubmit" method="POST">
	<form:hidden path="id" />	
	<ul>
		<li><label>Payment Date: </label><fmt:formatDate type="both" value="${invoicePayment.insertDateTime}" />  
		</li>
		<li><label>Description: </label><c:out value="${invoicePayment.description }"/></li>	
		<li><label>Payment Method: </label>
			<form:select path="paymentMethod"><form:options items="${paymentMethods}" itemLabel="name" itemValue="value" /></form:select> 
		</li>
		<li><label>Amount: </label><c:out value="${invoicePayment.amt }"/></li>
	</ul>
	
	<br /><br />
			<input type="submit" value="update" class="bluebtn" />

</form:form>
<script>
setupAjaxFormReplace("pymt-form","wrapper");
</script>
</div>