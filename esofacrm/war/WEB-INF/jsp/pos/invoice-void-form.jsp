<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="wrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Void Invoice</span></h1>
</div>
<div style="clear:both" />
<p>
Are you sure you want to void invoice ${invoice.invoiceNumber }?</p>
<form:form id="invoice-void-form" commandName="invoice"
	action="/pos/invoice-void-formsubmit" method="POST">
	<form:hidden path="invoiceNumber" />
	<br /><br />
			<input type="submit" value="Confirm" class="bluebtn" />

</form:form>
<script>
setupAjaxFormReplace("invoice-void-form","wrapper");
</script>
</div>