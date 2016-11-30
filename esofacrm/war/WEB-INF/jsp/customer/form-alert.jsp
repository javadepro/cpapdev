<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div id="alertformwrapper" style="display:hidden">
<div id="title">
<h1><div id="icon-tools" class="icon32"></div><span style="float:left; margin-left: 5px; margin-top: 6px;">Alerts</span></h1>
</div>
<div style="clear:both" />
<p>
Alerts are can be set to remind you or your colleagues when<br/> to follow up with this particular patient only. 
</p>
<form:form id="alert-form" commandName="alert"
	action="/customer/alert/formsubmit" method="POST">
	<form:hidden path="customer" />
	<form:hidden path="id" />
	<form:errors path="*" cssClass="errorblock" element="div" />
	<ul>
		<li><label>Alert Type:</label>
		<form:select path="alertSubType">
				<c:forEach var="alertType" items="${alertTypeMap}" varStatus="x">
					<optgroup label="${alertType.key.type}">
						<form:options items="${alertType.value}" itemLabel="type" /> 
					</optgroup>
				</c:forEach>
			</form:select>
		</li>
		<li><label>Alert Date:</label><form:input
					path="alertDate" cssClass="datepicker"/>&nbsp;</li>
		<li><label>Assign To:</label>
		<form:select path="clinician">
			<form:options items="${clinicianMap}" itemLabel="name" />
		</form:select>		
		</li>					
		<li><label>Message:</label><span><form:textarea 
					path="message" cols="40" rows="3"/>&nbsp;</span></li>
		

	</ul>
	<br /><br />
	<input type="submit" value="Save Alert" class="bluebtn" />
</form:form>
<script>
$(".datepicker").datepicker({
	dateFormat : "dd/mm/yy",
	changeMonth : true,
	changeYear : true,
	yearRange: "-1:+2",
	numberOfMonths: 3,
	showButtonPanel: true
});
setupAjaxFormReplace("alert-form","alertformwrapper");
</script>
</div>