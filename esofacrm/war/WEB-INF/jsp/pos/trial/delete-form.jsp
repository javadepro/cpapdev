<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="wrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Delete Item from Trial/Rental Pool</span></h1>
</div>
<div style="clear:both" />
<p>
Are you sure you want to delete the item from the trial/rental pool with serial: ${trialItem.serialNumber} ?</p>
<form:form id="trial-delete-form" commandName="trialItem"
	action="/pos/trial/delete-formsubmit" method="POST">
	<form:hidden path="id" />
	<br /><br />
			<input type="submit" value="Confirm" class="bluebtn" />

</form:form>
<script>
setupAjaxFormReplace("trial-delete-form","wrapper");
</script>
</div>